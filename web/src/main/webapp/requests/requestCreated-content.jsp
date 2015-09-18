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



<bean:define id="requestId" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>"        scope="request" type="java.lang.String"/>

	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top"> 
        	<td height="15">
        		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
              	<tr> 
                	<td height="100%" class="table-title"><bean:message key="text.requestCreatedTableTitle" bundle="requests"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr valign="top">
			<td height="100%" width="100%">
            	<table width="100%" height="15" class="table" cellpadding="0" cellspacing="0">
              	<tr>
	                <td colspan="4" height="15"></td>
              	</tr>
	            <tr>
					<td width="25" ></td>
        	        <td class="text8b" colspan="2" height="35">
        	        
<logic:notEmpty name="<%=SessionScopeConstants.SESSION_UPLOADEDFILE_KEY%>" scope="session">
						<bean:define id="filename"  name="<%=SessionScopeConstants.SESSION_UPLOADEDFILE_KEY%>" scope="session" type="java.lang.String"/>
						<bean:message key="text.file" bundle="requests"/> "<%=filename%>"  <bean:message key="text.uploadedOKandIdIs" bundle="requests"/>
</logic:notEmpty>						

<logic:empty name="<%=SessionScopeConstants.SESSION_UPLOADEDFILE_KEY%>" scope="session">
						<bean:message key="text.noFileButRequestCreated" bundle="requests"/> 
</logic:empty>						
						&nbsp;<bean:message key="text.uploadedOKandIdIs.2" bundle="requests"/> <%=requestId%>. <br><br><br>
						<bean:message key="text.click" bundle="requests"/> <html:link action="/list-requests.do"><font color="#5E5EB0"><bean:message key="text.here" bundle="requests"/></font></html:link> <bean:message key="text.viewYourRequests" bundle="requests"/>
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
