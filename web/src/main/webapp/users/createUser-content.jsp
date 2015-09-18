<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.ApplicationScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>


<auster:checkLogon
   	    sessionKey="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>">
<body>
	<form name="redirectForm" action="<html:rewrite page="/errorPage.do"/>" method="post">
		<input type="hidden" name="<%=ExceptionConstants.USERNOTLOGGED_KEY%>" value="true"/>
	</form>
</body>
<script language="javascript">
	document.forms['redirectForm'].submit();
</script>

</auster:checkLogon>





<script language="javascript">

function checkEmailFormat(emailAddress) {
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(emailAddress.value)){
		return true;
	}
	return false;
}

function submitForm() {

	form = document.forms[0];
	if (form.login.value.length <= 0) {
		window.alert("<bean:message key="text.user.loginNotNull" bundle="users"/>");
		return;
	} else if (form.userEmail.value.length <= 0) {
		window.alert("<bean:message key="text.user.emailNotNull" bundle="users"/>");
		return;
	} else if (! checkEmailFormat(form.userEmail)) {
		window.alert("<bean:message key="text.email.malformed" bundle="users"/>");
		return;
	} else if (form.password.value.length <= 0) {
		window.alert("<bean:message key="text.user.passwordNotNull" bundle="users"/>");
		return;
	} else if (form.repeatPassword.value.length <= 0) {
		window.alert("<bean:message key="text.user.retypePasswordRequired" bundle="users"/>");
		return;
	}  else if (form.password.value != form.repeatPassword.value) {
		window.alert("<bean:message key="text.user.passwordsDoesNotMatch" bundle="users"/>");
		return;
	}
	form.submit();
}

</script>

	<td>
		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top">
        	<td height="15">
        		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
	            <tr>
                	<td height="100%" class="table-title"><bean:message key="text.createUserTableTitle" bundle="users"/></td>
				</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>
		<tr  valign="top">
			<td  height="100%"width="100%">

<html:form action="/create-user" method="post">

				<table width="100%" height="100%" align="center" class="table" cellpadding="3" cellspacing="0">

			  	<tr height="45">
  	   				<td colspan="3"></td>
				</tr>
    			<tr height="15">
					<td width="5%"></td>
       				<td width="25%" class="textBlue"> <div align="left"><bean:message key="text.loginColumn" bundle="users"/></div></td>
          			<td class="text8"><div align="left"><html:text property="login" size="24" maxlength="24" styleClass="mandatory-input-field"/></div></td>
  				</tr>
  				<tr height="15">
					<td width="10%" ></td>
	   				<td class="text8bRed"><html:errors bundle="users" property="login"/></td>
  	   				<td>&nbsp;</td>
				</tr>

    			<tr height="15">
					<td width="5%"></td>
       				<td width="25%" class="textBlue"> <div align="left"><bean:message key="text.userEmailColumn" bundle="users"/></div></td>
          			<td class="text8"><div align="left"><html:text property="userEmail" size="40" maxlength="120" styleClass="mandatory-input-field"/></div></td>
  				</tr>
  				<tr height="15">
					<td width="10%" ></td>
	   				<td class="text8bRed"><html:errors bundle="users" property="userEmail"/></td>
  	   				<td>&nbsp;</td>
				</tr>
    			<tr height="15">
					<td></td>
       				<td class="textBlue"> <div align="left"><bean:message key="text.userNameColumn" bundle="users"/></div></td>
       				<td class="text8"><div align="left"><html:text property="userFullname" size="40" maxlength="60" styleClass="input-field"/></div></td>
				</tr>
			  	<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
    			<tr height="15">
					<td></td>
       				<td class="textBlue"> <div align="left"><bean:message key="text.permissionGroup" bundle="users"/></div></td>
       				<td class="text8">
       					<div align="left">
       					<bean:define id="groupList" name="<%=RequestScopeConstants.REQUEST_USER_GROUPLIST_KEY%>"/>
						<html:select property="permissionGroupName" styleClass="input-field">
							<html:options collection="groupList" labelProperty="groupName" property="groupName"/>
    					</html:select>
						</div>
       				</td>
  				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
    			<tr height="15">
					<td></td>
       				<td class="textBlue"><div align="left"><bean:message key="text.passwordColumn" bundle="users"/></div></td>
       				<td class="text8"><div align="left"><html:password property="password" redisplay="false" size="20" maxlength="32" styleClass="mandatory-input-field"/></div></td>
  				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
    			<tr height="15">
					<td></td>
       				<td class="textBlue"><div align="left"><bean:message key="text.retypePasswordColumn" bundle="users"/></div></td>
       				<td class="text8"><div align="left"><input type="password" name="repeatPassword" size="20" maxlength="32" class="mandatory-input-field"/></div></td>
				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>

				<!-- Place for custom fields -->
				<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM1_SHOW%>" scope="application">
    			<tr height="15">
					<td></td>
       				<td class="textBlue"><div align="left"><bean:message key="text.custom1Column" bundle="usersCustom"/></div></td>
       				<td class="text8"><div align="left"><html:text property="custom1" size="30" maxlength="30" styleClass="input-field"/></div></td>
				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
				</logic:present>

				<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM2_SHOW%>" scope="application">
    			<tr height="15">
					<td></td>
       				<td class="textBlue"><div align="left"><bean:message key="text.custom2Column" bundle="usersCustom"/></div></td>
       				<td class="text8"><div align="left"><html:text property="custom2" size="30" maxlength="30" styleClass="input-field"/></div></td>
				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
				</logic:present>

				<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM3_SHOW%>" scope="application">
    			<tr height="15">
					<td></td>
       				<td class="textBlue"><div align="left"><bean:message key="text.custom3Column" bundle="usersCustom"/></div></td>
       				<td class="text8"><div align="left"><html:text property="custom3" size="30" maxlength="30" styleClass="input-field"/></div></td>
				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
				</logic:present>


				<tr height="25" valign="middle" class="textBlue" >
  	   				<td colspan="3" class="textBlue">
  	   					<div align="right">
  	         				<html:link href="javascript:submitForm();">:: <bean:message key="text.save" bundle="users"/> ::</html:link>&nbsp;&nbsp;&nbsp;&nbsp;
  	         				<html:link href="javascript:history.back();">:: <bean:message key="text.cancel" bundle="users"/> ::</html:link>&nbsp;&nbsp;&nbsp;&nbsp;
  	   					</div>
  	   				</td>
				</tr>
				<tr>
    				<td colspan="2"></td>
				</tr>
</html:form>
				</table>
			</td>
   		</tr>
   		</table>
   	</td>
