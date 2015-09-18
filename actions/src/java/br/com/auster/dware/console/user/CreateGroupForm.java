/*
 * Copyright (c) 2004 TTI Tecnologia. All Rights Reserved.
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
 * Created on Oct 03, 2004
 */
package br.com.auster.dware.console.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import br.com.auster.dware.console.commons.ServiceConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.facelift.users.interfaces.UserManager;
import br.com.auster.security.base.SecurityException;


/**
 * @author Frederico A Ramos
 * @version $Id: CreateGroupForm.java 640 2008-09-18 13:44:50Z framos $
 */
public class CreateGroupForm extends ActionForm implements Serializable {



	private static final String ACTION_ERROR_GROUP_EXISTS = "text.group.exists";


	private String groupName;
	private List permissions = new ArrayList();



    //########################################
    // instance methods
    //########################################


    public ActionErrors validate(ActionMapping _mapping, HttpServletRequest _request) {

        ActionErrors errors = null;
        try {
			UserManager manager = (UserManager) ServiceLocator.getInstance().getService(ServiceConstants.USERMANAGER_SERVICE);

            if (manager.loadRole(groupName) != null) {
                // if no exception was raised, this user email already exists
                if (errors == null) {
                    errors = new ActionErrors();
                }
                errors.add("groupName", new ActionMessage(ACTION_ERROR_GROUP_EXISTS));
            }
        } catch (SecurityException se) {
            throw new PortalRuntimeException(se);
		}
        return errors;
    }

    public void reset(ActionMapping _mapping, HttpServletRequest _request) {
    	super.reset(_mapping, _request);
    }

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String _name) {
		groupName = _name;
	}

	public String[] getPermissionId() {
		return (String[]) permissions.toArray(new String[] {});
	}

	public void setPermissionId(String[] _perms) {
		permissions = Arrays.asList(_perms);
	}

}
