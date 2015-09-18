<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>


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




<bean:define id="requestInfo" name="<%=RequestScopeConstants.REQUEST_REQINFO_KEY%>" scope="request"/>

<script language="javascript">
function gotoAccounts() {
	document.forms[0].requestId.value='<bean:write name="requestInfo" property="id" format="############"/>';
	document.forms[0].submit();
}

</script>

<html:form action="/show-request-accounts" method="post">
	<html:hidden property="requestId" value=""/>
</html:form>

	<td>
		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top"> 
          	<td height="15">
          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
              	<tr> 
                	<td height="100%" class="table-title"><bean:message key="text.requestDetailsTableTitle" bundle="requests"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr valign="top">
			<td height="100%"width="100%">
            	<table width="100%" height="100%" class="table" cellpadding="0" cellspacing="0">
             	<tr>
             		<td height="5"></td>
             	</tr>
			  	<tr valign="top">
                	<td>
						<table width="95%" align="center" class="table" cellpadding="0" cellspacing="1">
						<tr class="table-column-title">
                      		<td  height="25" colspan="4"><bean:message key="text.requestDetailsSubTableTitle" bundle="requests"/></td>
                    	</tr>
						<tr class="textBlue">
                      		<td width="17%"><bean:message key="text.requestTableColumn" bundle="requests"/></td>
	                      	<td width="22%"><bean:message key="text.requestDateTableColumn" bundle="requests"/></td>
                      		<td width="24%"><bean:message key="text.requestStatusTableColumn" bundle="requests"/></td>
                      		<td width="25%"><bean:message key="text.finishRateTableColumn" bundle="requests"/></td>
                      		
                    	</tr>
						<tr class="text8">							    
                      		<td>
                   				<bean:write name="requestInfo" property="id" format=""/>
                      		</td>
                      		<td><bean:write name="requestInfo" property="startDate" format="dd-MM-yy HH:mm"/></td>
                      		<td class="<bean:write name="requestInfo" property="statusColorKey"/>"><bean:message name="requestInfo" property="statusBundleKey" bundle="general"/></td>
                      		<td><bean:write name="requestInfo" property="finishedCount" format="############"/>/<bean:write name="requestInfo" property="totalCount" format="############"/>
                      			<bean:message key="text.selectAccountsTableTitle" bundle="requests"/>
                      		</td>
                    	</tr>				
						<tr class="textBlue">
							<td></td>
	                      	<td><bean:message key="text.requestEndDateTableColumn" bundle="requests"/></td>
                      		<td><bean:message key="text.cycleIdTableColumn" bundle="requests"/></td>
                    	</tr>
						<tr class="text8">							    
							<td></td>
							<td><bean:write name="requestInfo" property="finishDate" format="dd-MM-yy HH:mm"/></td>
							<td><bean:write name="requestInfo" property="cycleId"/></td>
                    	</tr>
						</table>
					</td>
              	</tr>
              	<tr>
                	<td height="10"></td>
              	</tr> 
              	
			  	<tr valign="top">
			  	
			  	
<logic:notEmpty name="requestInfo" property="emailList">

                	<td height="15">
                		<div align="center">
                    	<table width="95%" align="center" class="table" cellpadding="0" cellspacing="1">
					   	<tr>
                        	<td height="25" colspan="2" class="table-column-title"><bean:message key="text.requestNotificationSubTableTitle" bundle="requests"/></div></td>
                      	</tr>
					  	<tr class="textBlue">
                        	<td width="60%"><div align="center"><bean:message key="text.notificationEmailTableColumn" bundle="requests"/></div></td>
                        	<td width="40%"><div align="center"><bean:message key="text.notificationDateTableColumn" bundle="requests"/></div></td>
                      	</tr>

	<logic:iterate name="requestInfo" id="emailInfo" property="emailList">

					  	<tr class="text8">
                      		<td><div align="center"><bean:write name="emailInfo" property="emailAddress"/></div></td>
                        	<td><div align="center">
                        		<logic:notEmpty name="emailInfo" property="sentDatetime">
	                        		<bean:write name="emailInfo" property="sentDatetime" format="dd-MM-yy HH:mm"/>
	                        	</logic:notEmpty>
                        		<logic:empty name="emailInfo" property="sentDatetime">
	                        		<bean:message key="text.emailNotSendYet" bundle="requests"/>
	                        	</logic:empty>
	                        	</div>
                        	</td>
                      	</tr>
	</logic:iterate>
	
				    	</table>
                  		</div>
                  	</td>    
              	</tr>

              	<tr>
                	<td height="10"></td>
              	</tr> 
			  	<tr valign="top">

</logic:notEmpty>	

			    <tr  valign="middle" class="text8b">
			    	<td height="25">
			    		<div align="right"> 
			    		<html:link href="javascript:gotoAccounts()" styleClass="textBlue"> :: <bean:message key="text.gotoAccountList" bundle="requests"/> ::</html:link> &nbsp;&nbsp;
			    		</div>
			    	</td>
			    </tr>

				<tr>
                	<td height="10"></td>
              	</tr> 
<logic:notEmpty name="requestInfo" property="bundleList">


				<!-- NON-EMPTY REPORTS -->
				<tr>
                	<td height="15">
                		<div align="center">
                    	<table width="95%" align="center" class="table" cellpadding="0" cellspacing="0">
					   	<tr>
                        	<td height="25" colspan="4" class="table-column-title"><bean:message key="text.requestBundleDetailsSubTableTitle" bundle="requests"/></div></td>
                      	</tr>
					  	<tr height="20" class="textBlue">
                        	<td width="20%"><div align="center"><bean:message key="text.processedDateTableColumn" bundle="requests"/></div></td>
                        	<td width="50%"><div align="left">&nbsp;&nbsp;&nbsp;<bean:message key="text.requestLabelTableColumn" bundle="requests"/></div></td>
                        	<td width="20%"><div align="center"><bean:message key="text.consequenceCount" bundle="requests"/></td>
                        	<td width="10%"></td>
                      	</tr>

	<bean:define id="bundleList" name="requestInfo" property="bundleList"/>
	<% int bundleCounter = 0; String tdClass="";%>
	<logic:iterate name="bundleList" id="fileInfo">

		<logic:greaterThan name="fileInfo" property="consequenceCount" value="0">
					  	<tr height="15" class="text8">					  	    
			<% 
			   if (bundleCounter%2 == 0) { 
				  tdClass = "class=\"even-row\"";
			   } else { 
		          tdClass = "class=\"odd-row\"";
			   } 
			   bundleCounter++;
			%>					  	    
	                        	<td <%=tdClass%>><div align="center"><bean:write name="fileInfo" property="generationDate" format="dd-MM-yy HH:mm"/></div></td>
	                      		<td <%=tdClass%>><div align="left">&nbsp;&nbsp;&nbsp;<bean:write name="fileInfo" property="message"/></div></td>
	                      		<td <%=tdClass%>><div align="center"><bean:write name="fileInfo" property="consequenceCount"/></div></td>
	                      		<td <%=tdClass%>><div align="center">
		                        	<html:link action="/download-file" paramId="<%=RequestScopeConstants.REQUEST_SELECTEDFILE_KEY%>" 
		                        									   paramName="fileInfo" 
		                        									   paramProperty="filename">	                        									   
			<%  if (bundleCounter%2 == 0) { %>
		                      			<html:img page="/images/download.png" height="20" width="20" border="0"/>
			<% } else { %>
		                      			<html:img page="/images/download_grey.png" height="20" width="20" border="0"/>
			<% } %>
		                      		</html:link>
		                      		</div>
	                      		</td>
	                      	</tr>
		</logic:greaterThan>	                      	
	</logic:iterate>
	
				    	</table>
                  		</div>
                  	</td>              
              	</tr>
              	
              	<tr>
                	<td height="10"></td>
              	</tr> 

				<!-- EMPTY REPORTS -->
				<tr>
                	<td height="15">
                		<div align="center">
                    	<table width="95%" align="center" class="table" cellpadding="0" cellspacing="0">
					   	<tr>
                        	<td height="25" colspan="4" class="table-column-title"><bean:message key="text.requestEmptyBundleDetailsSubTableTitle" bundle="requests"/></div></td>
                      	</tr>
					  	<tr height="20" class="textBlue">
                        	<td width="20%"><div align="center"><bean:message key="text.processedDateTableColumn" bundle="requests"/></div></td>
                        	<td width="50%"><div align="left">&nbsp;&nbsp;&nbsp;<bean:message key="text.requestLabelTableColumn" bundle="requests"/></div></td>
                        	<td width="20%"><div align="center"><bean:message key="text.consequenceCount" bundle="requests"/></td>
                        	<td width="10%"></td>
                      	</tr>

	<bean:define id="bundleList" name="requestInfo" property="bundleList"/>
	<% bundleCounter = 0; 
	   tdClass="";
	%>
	<logic:iterate name="bundleList" id="fileInfo">

		<logic:lessEqual name="fileInfo" property="consequenceCount" value="0">
					  	<tr height="15" class="text8">					  	    
			<% 
			   if (bundleCounter%2 == 0) { 
				  tdClass = "class=\"even-row\"";
			   } else { 
		          tdClass = "class=\"odd-row\"";
			   } 
			   bundleCounter++;
			%>					  	    
	                        	<td <%=tdClass%>><div align="center"><bean:write name="fileInfo" property="generationDate" format="dd-MM-yy HH:mm"/></div></td>
	                      		<td <%=tdClass%>><div align="left">&nbsp;&nbsp;&nbsp;<bean:write name="fileInfo" property="message"/></div></td>
	                      		<td <%=tdClass%>><div align="center"><bean:write name="fileInfo" property="consequenceCount"/></div></td>
	                      		<td <%=tdClass%>><div align="center">
		                        	<html:link action="/download-file" paramId="<%=RequestScopeConstants.REQUEST_SELECTEDFILE_KEY%>" 
		                        									   paramName="fileInfo" 
		                        									   paramProperty="filename">	                        									   
			<%  if (bundleCounter%2 == 0) { %>
		                      			<html:img page="/images/download.png" height="20" width="20" border="0"/>
			<% } else { %>
		                      			<html:img page="/images/download_grey.png" height="20" width="20" border="0"/>
			<% } %>
		                      		</html:link>
		                      		</div>
	                      		</td>
	                      	</tr>
		</logic:lessEqual>	                      	
	</logic:iterate>
	
				    	</table>
                  		</div>
                  	</td>              
              	</tr>

              	
</logic:notEmpty>

<logic:empty name="requestInfo" property="bundleList">
                	<td height="15" class="text8b">
	                	<div align="center"><bean:message key="text.noBundlesFound" bundle="requests"/></div>
                	</td>
	
</logic:empty>
              	</tr>

				<tr>
					<td colspan="4"><div align="center">
					</div></td>
				</tr>


			    <tr  valign="middle" class="text8b">
			    	<td height="25">
			    		<div align="right"> 
			    		<html:link href="javascript:history.back()" styleClass="textBlue"> :: <bean:message key="text.backToRequestList" bundle="requests"/> ::</html:link> &nbsp;&nbsp;&nbsp;&nbsp;
			    		<html:link href="javascript:gotoAccounts()" styleClass="textBlue"> :: <bean:message key="text.gotoAccountList" bundle="requests"/> ::</html:link> &nbsp;&nbsp;
			    		</div>
			    	</td>
			    </tr>
				<tr>
                	<td height="100%" ></td>
              	</tr>
              	</table>  
			</td>
   		</tr>
   		</table>
   		<br>
   	</td>
