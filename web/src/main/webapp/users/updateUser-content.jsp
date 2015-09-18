<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>
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

function submitLockForm() {
	document.forms['lockUserForm'].submit();
}

function submitUnlockForm() {
	document.forms['unlockUserForm'].submit();
}

function checkEmailFormat(emailAddress) {
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(emailAddress.value)){
		return true;
	}
	return false;
}

function submitForm() {

	form = document.forms['updateUserForm'];
	// just check if both are different, IF PASSWORD WAS SPECIFIED!!!
	// If no password was providaded, it WILL NOT be changed
	if ((form.password.value.length > 0) && (form.password.value != form.repeatPassword.value)) {
		window.alert("<bean:message key="text.user.passwordsDoesNotMatch" bundle="users"/>");
		return;
	}

	<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM1_PROTECTED%>" scope="application">
    	form.elements['custom1'].disabled=false;
    </logic:present>
	<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM2_PROTECTED%>" scope="application">
    	form.elements['custom2'].disabled=false;
    </logic:present>
	<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM3_PROTECTED%>" scope="application">
    	form.elements['custom3'].disabled=false;
    </logic:present>
	form.submit();
}

</script>

<bean:define id="userInfo" name="<%=RequestScopeConstants.REQUEST_USERINFO_KEY%>" scope="request"/>
<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>


	<td>
		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top">
          	<td height="15">
          		<table  class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
              	<tr>
                	<td height="100%" class="table-title"><bean:message key="text.createUserTableTitle" bundle="users"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>
		<tr  valign="middle">
			<td height="100%" width="100%">

<html:form action="/update-user" method="post">

				<table width="100%" height="100%" align="center" class="table" cellpadding="3" cellspacing="0">
				<tr height="45">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
    			<tr height="15">
					<td width="10%"></td>
       				<td width="25%" class="textBlue"> <div align="left"><bean:message key="text.loginColumn" bundle="users"/></div></td>
          			<td width="50%" class="text8"><div align="left"><html:hidden property="login"/><bean:write name="userInfo" property="login"/></div></td>

			       <script language="Javascript">
			       		document.forms['updateUserForm'].elements['login'].value='<bean:write name="userInfo" property="login"/>';
			       </script>
  				</tr>

				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
    			<tr height="15">
    			   <td width="10%"></td>
			       <td width="25%" class="textBlue"> <div align="left"><bean:message key="text.userEmailColumn" bundle="users"/></div></td>
			       <td width="50%" class="text8"><div valign="left" align="left"><html:hidden property="userEmail"/><bean:write name="userInfo" property="email"/></div></td>

			       <script language="Javascript">
			       		document.forms['updateUserForm'].elements['userEmail'].value='<bean:write name="userInfo" property="email"/>';
			       </script>

			  	</tr>
				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
			    <tr height="15">
    			   <td></td>
			       <td class="textBlue"> <div align="left"><bean:message key="text.userNameColumn" bundle="users"/></div></td>
			       <td><div align="left"><html:text property="userFullname" size="40" maxlength="60" styleClass="input-field"/></div></td>

			       <script language="Javascript">
			       		document.forms['updateUserForm'].elements['userFullname'].value='<bean:write name="userInfo" property="userName"/>';
			       </script>
				</tr>
			  	<tr height="15">
			  	   <td colspan="3">&nbsp;</td>
				</tr>

    <% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_USER_UPDATE_OTHERS_KEY)) { %>

    			<tr height="15">
					<td></td>
					<td class="textBlue"><div align="left"><bean:message key="text.permissionGroup" bundle="users"/></div></td>
       				<td class="text8">
       					<div align="left">
       					<bean:define id="groupList" name="<%=RequestScopeConstants.REQUEST_USER_GROUPLIST_KEY%>"/>
						<html:select property="permissionGroupName" styleClass="input-field">
							<html:options collection="groupList" labelProperty="groupName" property="groupName"/>
    					</html:select>
						</div>
       				</td>
  				</tr>

<!-- needed to set the current group as the selected one in the combo-box -->
<script language="Javascript">
<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_USER_CURRGROUP_KEY%>">
	<bean:define id="currGroupName" name="<%=RequestScopeConstants.REQUEST_USER_CURRGROUP_KEY%>" scope="request"/>

   selectObj = document.forms['updateUserForm'].elements['permissionGroupName'];
   for (i=0; i < selectObj.options.length; i++) {
      if (selectObj.options[i].value == '<%=currGroupName%>') {
          selectObj.options[i].selected=true;
      }
   }

		<% if (! user.getAllowedDomains().contains(PermissionConstants.PERMISSION_USER_UPDATE_OTHERS_KEY)) { %>
	selectObj.disabled=true;
		<% } %>

</logic:notEmpty>

</script>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>

	<% } %>

			    <tr height="15">
    			   <td></td>
			       <td class="textBlue"> <div align="left"><bean:message key="text.passwordColumn" bundle="users"/></div></td>
			       <td><div align="left"><html:password property="password" redisplay="false" size="32" maxlength="32" styleClass="input-field"/></div></td>
			  	</tr>
			  	<tr height="15">
			  	   <td colspan="3">&nbsp;</td>
				</tr>
			    <tr height="15" class="text8">
    			   <td></td>
			       <td class="textBlue"> <div align="left"><bean:message key="text.retypePasswordColumn" bundle="users"/></div></td>
			       <td><div align="left"><input type="password" name="repeatPassword" redisplay="false" size="32" maxlength="32" class="input-field"/></div></td>
				</tr>
			  	<tr height="15">
			  	   <td colspan="3">&nbsp;</td>
				</tr>

				<!-- Place for custom fields -->
				<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM1_SHOW%>" scope="application">
    			<tr height="15">
					<td></td>
       				<td class="textBlue"><div align="left"><bean:message key="text.custom1Column" bundle="usersCustom"/></div></td>
       				<td class="text8">
       					<div align="left">
     				    <html:text property="custom1" size="30" maxlength="30" styleClass="input-field"/>
       				    </div></td>

	       				<script language="Javascript">
				       		document.forms['updateUserForm'].elements['custom1'].value='<bean:write name="userInfo" property="custom1"/>';
	       					<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM1_PROTECTED%>" scope="application">
	       						document.forms['updateUserForm'].elements['custom1'].disabled=true;
	       					</logic:present>
				       </script>
				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
				</logic:present>

				<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM2_SHOW%>" scope="application">
    			<tr height="15">
					<td></td>
       				<td class="textBlue"><div align="left"><bean:message key="text.custom2Column" bundle="usersCustom"/></div></td>
       				<td class="text8">
       					<div align="left">
       					<html:text property="custom2" size="30" maxlength="30" styleClass="input-field"/>
       					</div></td>

       				<script language="Javascript">
			       		document.forms['updateUserForm'].elements['custom2'].value='<bean:write name="userInfo" property="custom2"/>';
       					<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM2_PROTECTED%>" scope="application">
       						document.forms['updateUserForm'].elements['custom2'].disabled=true;
       					</logic:present>
			       </script>
				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
				</logic:present>

				<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM3_SHOW%>" scope="application">
    			<tr height="15">
					<td></td>
       				<td class="textBlue"><div align="left"><bean:message key="text.custom3Column" bundle="usersCustom"/></div></td>
       				<td class="text8">
       					<div align="left">
       					<html:text property="custom3" size="30" maxlength="30" styleClass="input-field"/>
       					</div></td>

       				<script language="Javascript">
			       		document.forms['updateUserForm'].elements['custom3'].value='<bean:write name="userInfo" property="custom3"/>';
       					<logic:present name="<%=ApplicationScopeConstants.APPLICATION_USER_CUSTOM3_PROTECTED%>" scope="application">
       						document.forms['updateUserForm'].elements['custom3'].disabled=true;
       					</logic:present>
			       </script>
				</tr>
  				<tr height="15">
  	   				<td colspan="3">&nbsp;</td>
				</tr>
				</logic:present>

				<tr height="25" valign="middle" class="textBlue" >
			  	   	<td colspan="3"><div align="right">
	<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_USER_UPDATE_OTHERS_KEY)) { %>
			 	        <html:link href="javascript:submitLockForm();">:: <bean:message key="text.lockUser" bundle="users"/> ::</html:link>&nbsp;&nbsp;
			 	        <html:link href="javascript:submitUnlockForm();">:: <bean:message key="text.unlockUser" bundle="users"/> ::</html:link>&nbsp;&nbsp;
	<% } %>
			 	        <html:link href="javascript:submitForm();">:: <bean:message key="text.save" bundle="users"/> ::</html:link>&nbsp;&nbsp;
			 	        <html:link href="javascript:history.back();">:: <bean:message key="text.cancel" bundle="users"/> ::</html:link>&nbsp;&nbsp;&nbsp;&nbsp;
			  	   </div></td>
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


<html:form action="/lock-user" method="POST">
	<html:hidden property="login"/>
    <script language="Javascript">
   		document.forms['lockUserForm'].elements['login'].value='<bean:write name="userInfo" property="login"/>';
    </script>
</html:form>


<html:form action="/unlock-user" method="POST">
	<html:hidden property="login"/>
    <script language="Javascript">
   		document.forms['unlockUserForm'].elements['login'].value='<bean:write name="userInfo" property="login"/>';
    </script>
</html:form>

