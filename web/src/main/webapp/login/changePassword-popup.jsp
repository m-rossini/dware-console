<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>
<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>


<html>

<head>
	<title>:::: <bean:message key="text.title" bundle="general"/> ::::</title>

	<script language="JAVASCRIPT" src="<html:rewrite page="/js/format.js"/>"></script>
	<script language="JAVASCRIPT" src="<html:rewrite page="/js/calendar.js"/>"></script>

	<link href="<html:rewrite page="/css/data_login.css"/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page="/css/data_general.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css"/>
</head>



<body>

<script language="Javascript">
 function closeWindow() {
 	window.close();
 	return;
 }

  function submitForm() {
  
  	form = document.forms[0];
  	if (validateChangeUserPasswordForm(form)) {
		// just check if both are different, IF PASSWORD WAS SPECIFIED!!!
		// If no password was providaded, it WILL NOT be changed
		if (form.newPassword.value != form.repeatPassword.value) {
			window.alert("<bean:message key="text.passwordsDoesNotMatch" bundle="login"/>");
			return;
		}
		form.submit();
	}
  	return;
  }
</script>

	<!-- Begin Validator Javascript Function-->
	<html:javascript formName="changeUserPasswordForm"/>

	<html:form action="/passwordPopup" focus="login" method="post">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width=100% height=100% cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td colspan="2" height="15" class="table-title">
          		<bean:message key="text.changepassword" bundle="login"/>
          	</td>
		</tr>

		<tr colspan="2" valign="top">
        	<td height="10"></td>
        </tr>

		<tr height="15">
        	<td class="input-label"><bean:message key="text.login" bundle="login"/>&nbsp;</td>        	
        	<td><html:text styleClass="mandatory-input-field" property="login" size="12" maxlength="24" tabindex="1"/></td>
        </tr>

		<tr colspan="2" valign="top">
        	<td height="5"></td>
        </tr>

		<tr height="15">
        	<td class="input-label"><bean:message key="text.password" bundle="login"/>&nbsp;</td>
        	<td><html:password styleClass="mandatory-input-field" property="password" size="12" maxlength="24" tabindex="2"/></td>
        </tr>
		
		<tr colspan="2" valign="top">
        	<td height="5"></td>
        </tr>
        
		<tr height="15">
        	<td class="input-label"><bean:message key="text.newPassword" bundle="login"/>&nbsp;</td>
        	<td><html:password styleClass="mandatory-input-field" property="newPassword" size="12" maxlength="24" tabindex="3"/></td>
        </tr>
		
		<tr colspan="2" valign="top">
        	<td height="5"></td>
        </tr>
        
		<tr height="15">
        	<td class="input-label"><bean:message key="text.retypeNewPassword" bundle="login"/>&nbsp;</td>
        	<td><input type="password" name="repeatPassword" size="12" maxlength="24" class="mandatory-input-field" tabindex="4"/></td>
        </tr>
		
		<tr colspan="2" valign="top">
        	<td height="5"></td>
        </tr>
        
		<tr>
	    	<td class="text8" height="30" colspan="4" align="right">
	    		<a href="javascript:submitForm()">:: <bean:message key="text.confirm" bundle="login"/> :: </a>&nbsp;&nbsp;
	    		<a href="javascript:closeWindow()">:: <bean:message key="text.cancel" bundle="login"/> :: </a>&nbsp;&nbsp;&nbsp;&nbsp;
	    	</td>
	    </tr>
		</table>

	</html:form>

</body>
</html>
