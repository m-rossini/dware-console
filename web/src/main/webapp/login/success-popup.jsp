<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>

<html>

<head>
	<title>:::: <bean:message key="text.title" bundle="general"/> ::::</title>
	
	<link href="<html:rewrite page="/css/data_login.css"/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page="/css/data_general.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css"/>
</head>

<script language="Javascript">
 function closeWindow() {
 	window.close();
 	return;
 }
</script>

<body>

		<table width=100% cellpadding="0" cellspacing="0">
		<tr valign="top">
		        	<td colspan="2" height="15" class="table-title">
		        		<bean:message key="text.changepassword" bundle="login"/>
		        	</td>
		</tr>
		
		<tr colspan="2" valign="top">
		      	<td height="10"></td>
		      </tr>
		
		<tr height="15">
			<td class="text8b" height="35">
		          	<div align="center" valign="center">                                                
		           		<bean:define id="operationKey" name="<%=RequestScopeConstants.REQUEST_CONFIRMOPERATION_TEXT%>" type="java.lang.String"/>
		           		<bean:message key="<%=operationKey%>" bundle="users"/>.<br><br>  
		           		 <font class="textBlue"><a href="javascript:closeWindow()"><bean:message key="text.password.backToLoginPage" bundle="login"/></a></font>                          		              	
		           	</div>
		           </td>
		      </tr>
		 </table>

</body>
</html>
