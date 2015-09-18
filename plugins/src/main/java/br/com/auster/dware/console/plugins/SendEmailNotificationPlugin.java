/*
 * Copyright (c) 2004 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Apr 25, 2005
 */
package br.com.auster.dware.console.plugins;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import br.com.auster.common.io.IOUtils;
import br.com.auster.common.log.LogFactory;
import br.com.auster.common.mail.MailSender;
import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.plugins.email.EmailContentBuilder;
import br.com.auster.facelift.requests.interfaces.RequestManagerException;
import br.com.auster.facelift.requests.web.interfaces.WebRequestManager;
import br.com.auster.facelift.requests.web.model.NotificationEmail;
import br.com.auster.facelift.requests.web.model.WebRequest;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.services.plugin.FaceliftPlugin;
import br.com.auster.facelift.services.plugin.PluginContext;
import br.com.auster.facelift.services.plugin.PluginRuntimeException;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.persistence.PersistenceException;
import br.com.auster.security.model.User;
import br.com.auster.security.base.SecurityException;



public class SendEmailNotificationPlugin implements FaceliftPlugin {


    private PluginContext context;
    private Map parameters;


	public static final String DEFAULT_EMAIL_BUILDER = "br.com.auster.dware.console.plugins.email.DefaultEmailContentBuilder";

    public static final String CONFIGURATION_EMAIL_XSL = "email.xsl.file";
    public static final String CONFIGURATION_EMAIL_SUBJECT = "email.subject";
    public static final String CONFIGURATION_EMAIL_CONTENT_CLASS = "email.contentBuilder";
    public static final String CONFIGURATION_EMAIL_DATASOURCE = "email.datasource";

    public static final int WEBREQUEST_PARAM_POS = 0;


    private Logger log = LogFactory.getLogger(SendEmailNotificationPlugin.class);



    public PluginContext getContext() {
        return context;
    }

    public void setContext(PluginContext _context) {
        context = _context;
    }

    public void setConfigurationParameters(Map _params) {
        parameters = _params;

    }

    public Map getConfigurationParameters() {
        return parameters;
    }

    public void execute() {

        if (this.getConfigurationParameters() == null) {
            throw new PluginRuntimeException("plugin not configured");
        }
        String emailSubject = (String) getConfigurationParameters().get(CONFIGURATION_EMAIL_SUBJECT);
		if (emailSubject == null) {
			//TODO log : no subject configured
			emailSubject = "";
		}

        String xslLocation = (String) getConfigurationParameters().get(CONFIGURATION_EMAIL_XSL);
		if ((xslLocation == null) || (xslLocation.length() <= 0)) {
			throw new PluginRuntimeException("XML transformation definitions was not properly configured");
		}
        String jndi = (String) getConfigurationParameters().get(CONFIGURATION_EMAIL_DATASOURCE);
		if ((jndi == null) || (jndi.length() <= 0)) {
			throw new PluginRuntimeException("Email service was not properly configured");
		}
        try {
            WebRequest req = getRequest();
            if (req == null) {
				log.warn("web request information not set");
                throw new PluginRuntimeException("no web request set");
            }
            if ((req.getNotifications() == null) || (req.getNotifications().size() <= 0)) {
				log.warn("could not find notitications for web request " + req.getRequestId());
                return;
            }
            // skip notifications in case sent date is not null
            NotificationEmail email = (NotificationEmail) req.getNotifications().iterator().next();
            if (email.getSentDatetime() != null) {
            	log.warn("email notifications for request " + req.getRequestId() + " already sent.");
            	return;
            }
            // loading user info
            UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);
			User owner = manager.loadUser(req.getOwnerId());
            // build email content
            String content = getEmailContent(req, owner, xslLocation);
            // send email
            if (content != null) {
                MailSender.sendMail(getEmailAddresses(req), emailSubject, content, jndi);
	            WebRequestManager requestManager = (WebRequestManager) ServiceLocator.getInstance().getService(ServiceConstants.REQUESTMANAGER_SERVICE);
                requestManager.setAllNotificationsAsSent(req.getRequestId());
            } else {
				log.error("error while generating email content. Email text is null");
            }
		} catch (RequestManagerException rme) {
			log.error("Error while executing request business logic", rme);
			throw new PluginRuntimeException("Error while executing request business logic", rme);
        } catch (SecurityException se) {
			log.error("Error while executing user business logic", se);
            throw new PluginRuntimeException("Error while executing user business logic", se);
        } catch (PersistenceException prae) {
			log.error("Error while acessing persistence layer", prae);
            throw new PluginRuntimeException("Error while acessing persistence layer", prae);
        } catch (MessagingException me) {
            log.error("could not create email message", me);
			throw new PluginRuntimeException("could not create email message", me);
		} catch (NamingException ne) {
			log.error("could not locate component in naming structure", ne);
            throw new PluginRuntimeException("could not locate component in naming structure", ne);
		} catch (Exception e) {
			log.error("general exception", e);
            throw new PluginRuntimeException("general exception", e);
		}
    }

    public void execute(PluginContext _context) {
        setContext(_context);
        execute();
    }

    public void cleanup() {
        context = null;
        parameters = null;
    }



    private WebRequest getRequest() {
        if ((context.getExecutionParameters() == null) ||
            (context.getExecutionParameters().size() <= 0)) {
	        return null;
        }
        return (WebRequest) context.getExecutionParameters().get(0);
    }

    private String[] getEmailAddresses(WebRequest _request) {
        String[] addresses = new String[_request.getNotifications().size()];
        Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());

        Iterator iterator = _request.getNotifications().iterator();
        for(int i=0; iterator.hasNext(); i++) {
            NotificationEmail email = (NotificationEmail) iterator.next();
            addresses[i] = email.getEmailAddress();
            email.setSentDatetime(now);
        }
        return addresses;
    }

    private String getEmailContent(WebRequest _request, User _owner, String _xslFile) throws Exception {

        Document sourceDoc = getContentAsXML(_request, _owner);

        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        dFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
        DOMSource xslDomSource = new DOMSource(dBuilder.parse(IOUtils.openFileForRead(_xslFile)));

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(xslDomSource);

        DOMSource xmlDomSource = new DOMSource(sourceDoc);
        StringWriter  stringOut = new StringWriter();
        StreamResult stringResult = new StreamResult(stringOut);

        transformer.transform(xmlDomSource, stringResult);
        return stringOut.toString();
    }

    private Document getContentAsXML(WebRequest _request, User _owner) throws Exception {
		String klassName = (String) getConfigurationParameters().get(CONFIGURATION_EMAIL_CONTENT_CLASS);
		if ((klassName == null) || (klassName.length() <= 0)) {
			klassName = DEFAULT_EMAIL_BUILDER;
		}
        Class klass = Class.forName(klassName);
        Object instance = klass.newInstance();
        return ((EmailContentBuilder)instance).getContentAsXML(_request, _owner);
    }

}
