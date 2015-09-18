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
                	<td height="100%" class="table-title"><bean:message key="text.userOperationTableTitle" bundle="thresholds"/></td>
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
                		<bean:message key="text.nfthreshold.done" bundle="thresholds"/><br/><br/>
	                	<a href="<html:rewrite page="/list-nf-thresholds.do"/>"><bean:message key="text.nfthreshold.backToList" bundle="thresholds"/><br/></a>
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
