
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>
<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>

	<td>
		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
        	<td height="15">
        		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
              	<tr>
                	<td height="100%" class="table-title"><bean:message key="text.exceptionTableTitle" bundle="general"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>
		<tr valign="middle">
			<td height="100%" width="100%">
            	<table width="100%" height="100%" align="center" class="table" cellpadding="0" cellspacing="0">
              	<tr>
                	<td colspan="4" height="15"></td>
              	</tr>
              	<tr>
					<td width="25"></td>
                	<td class="text8b" colspan="2" height="35"><div align="left">

		<logic:messagesPresent message="true">
		   <html:messages id="message" message="true" bundle="exception">
		     <span><c:out value="${message}"/></span><br>
		   </html:messages>
		</logic:messagesPresent>

		<logic:notEmpty name="<%=ExceptionConstants.EXCEPTION_MESSAGE%>" scope="request">
			<bean:write name="<%=ExceptionConstants.EXCEPTION_MESSAGE%>"/>
		</logic:notEmpty>

		 <br><br>


            <logic:empty name="<%=RequestScopeConstants.REQUEST_NOTLOGGED_KEY%>" scope="request">
                        <bean:message key="error.goBack" bundle="exception"/><a href="javascript:history.back();"><font class="textBlue">
                        <bean:message key="error.goBack2" bundle="exception"/></font></a> <bean:message key="error.goBack3" bundle="exception"/>
                        <br><br>
            </logic:empty>

            <logic:notEmpty name="<%=RequestScopeConstants.REQUEST_NOTLOGGED_KEY%>" scope="request">
                        <bean:message key="error.goBack" bundle="exception"/>
                        <html:link action="/logout.do">
                            <bean:message key="error.goBack4" bundle="exception"/>
                        </html:link>
                            <bean:message key="error.gotoLogin2" bundle="exception"/>
                            <bean:message key="error.gotoLogin3" bundle="exception"/>
                            <br><br>
              </logic:notEmpty>
                </div></td>
	      <td width="25" ></td>
              </tr>
              <tr>
                <td colspan="4"></td>
              </tr>
              </table>
            </td>
          </tr>
        </table></td>
