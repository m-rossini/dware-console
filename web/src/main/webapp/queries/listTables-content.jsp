<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
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





	<td>

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top"> 
        	<td height="15">
        		<table class="table" height="100%" width="100%" align="center" cellpadding="0" cellspacing="0" >
              	<tr> 
                	<td height="100%" class="table-title"><bean:message key="text.listTablesTitle" bundle="queries"/></td>
              	</tr>
            	</table>
            </td>
        </tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr  valign="top">
			<td width="100%">	
			
				<table height="100%" width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
				<tr valign="top"> 
		        	<td height="20">&nbsp;</td>
		        </tr>
				<tr valign="top"> 
		        	<td height="40" class="text8b">&nbsp;&nbsp;<bean:message key="text.selectAView" bundle="queries"/></td>
		        </tr>
				
<script language="Javascript">
function submitDetailsForm(tableName) {

	document.forms[0].table.value = tableName;
	document.forms[0].submit();
}
</script>
                 
<html:form action="/select-fields" method="POST">
	<html:hidden property="table" value=""/>
</html:form>



<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">

	<bean:define id="tableList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>

	<logic:iterate name="tableList" id="tableInfo">

				<tr height="20" class="text8b">
      				<td><div align="center">
      					<a href="javascript:submitDetailsForm('<bean:write name="tableInfo" property="name"/>')">
      					<bean:write name="tableInfo" property="displayName"/>
      					</a></div>
      				</td>
				</tr>				
	</logic:iterate>
	
				<tr height="45"> 
			    	<td colspan="5">&nbsp;</td>
			    </tr>
</logic:notEmpty>



<logic:empty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">
				<tr height="45" class="text8b"> 
			    	<td colspan="6"><div align="center"><bean:message key="text.noQueriesFound" bundle="queries"/></div></td>
			    </tr>
</logic:empty>
				<tr height="100%">
					<td colspan="6"></td>
				</tr>
				</table>
			</td>
   		</tr>
   		</table>
   	</td>   	

