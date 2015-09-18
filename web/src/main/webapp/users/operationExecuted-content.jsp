<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
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


	<td class="text8b">
	<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top"> 
        	<td height="15">
        		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
              	<tr> 
                	<td height="100%" class="table-title"><bean:message key="text.userOperationTableTitle" bundle="users"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr valign="middle">
			<td height="100%"width="100%">
            	<table width="100%" height="100%" align="center" class="table" cellpadding="0" cellspacing="0">
              	<tr>
                	<td colspan="4" height="15"></td>
              	</tr>
              	<tr>
					<td width="25" ></td>
                	<td class="text8b" colspan="2" height="35">
                		<div align="left">            
                		<bean:define id="operationKey" name="<%=RequestScopeConstants.REQUEST_CONFIRMOPERATION_TEXT%>" type="java.lang.String"/>
                		<bean:message key="<%=operationKey%>" bundle="users"/>.<br><br>                
                		<logic:notPresent name="<%=RequestScopeConstants.REQUEST_USER_GROUPOPERATION_KEY%>">
	                		<bean:message key="text.users.backToList1" bundle="users"/> <font class="textBlue"><html:link action="/list-users.do"><bean:message key="text.users.backToList2" bundle="users"/></html:link></font> <bean:message key="text.users.backToList3" bundle="users"/>
	                	</logic:notPresent>
                		<logic:present name="<%=RequestScopeConstants.REQUEST_USER_GROUPOPERATION_KEY%>">
	                		<bean:message key="text.users.backToList1" bundle="users"/> <font class="textBlue"><html:link action="/list-groups.do"><bean:message key="text.users.backToList2" bundle="users"/></html:link></font> <bean:message key="text.users.backToList3Group" bundle="users"/>
	                	</logic:present>
	                	
                		</div>
                	</td>
					<td width="25"></td>
              	</tr>
              	<tr>
	                <td colspan="4"></td>
              	</tr>
              	</table>
			</td>
   		</tr>
   		</table>
   	</td>
