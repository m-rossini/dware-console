
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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




<bean:define id="requestId" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>" scope="request"/>
<bean:define id="accountInfo" name="<%=RequestScopeConstants.REQUEST_SECTIONINFO_KEY%>" scope="request"/>
<bean:define id="trailsList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>
<bean:define id="filesList" name="<%=RequestScopeConstants.REQUEST_LISTOFFILES_KEY%>" scope="request"/>


	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

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
		<tr valign="middle">
			<td height="100%" width="100%">
            	<table width="100%" height="100%" class="table" cellpadding="0" cellspacing="0">
            	<tr>
            		<td height="5"></td>
            	</tr>
			  	<tr valign="top">
                	<td>
			    <!-- this is the detail information for the selected request -->               
						<table width="95%" align="center" class="table" cellpadding="0" cellspacing="1">
						<tr>
                      		<td height="25" colspan="5" class="table-column-title"><bean:message key="text.requestDetailsSubTableTitle" bundle="requests"/></td>
                    	</tr>
						<tr class="textBlue">
                      		<td width="20%"><bean:message key="text.requestTableColumn" bundle="requests"/></td>
                        	<td width="20%"><bean:message key="text.accountTableColumn" bundle="requests"/></td>
                        	<td width="60%"><bean:message key="text.customerNameTableColumn" bundle="requests"/></td>
                    	</tr>
						<tr class="text8">
                      		<td><bean:write name="requestId"/></td>
                        	<td><bean:write name="accountInfo" property="accountId"/></td>
                        	<td><bean:write name="accountInfo" property="customerName"/></td>
                      	</tr>                                      
						<tr class="textBlue">
                        	<td><bean:message key="text.requestDateTableColumn" bundle="requests"/></td>
                        	<td><bean:message key="text.requestStatusTableColumn" bundle="requests"/></td>
							<td></td>
						</tr>
						<tr class="text8">
                        	<td><bean:write name="accountInfo" property="startDate" format="dd-MM-yy HH:mm"/></td>
                        	<td class="<bean:write name="accountInfo" property="statusColorKey"/>"><bean:message name="accountInfo" property="statusBundleKey" bundle="general"/></td>
							<td></td>
						</tr>
						</table>				
					</td>
              	</tr>

              
              	<tr>                  
                	<td height="10"></td>              
              	</tr> 
			  	<tr valign="top">
                	<td height="15">
                		<div align="center">
                    	<table width="95%" class="table" cellpadding="0" cellspacing="1">
				      	<tr class="text8b">
                        	<td height="25" colspan="3" class="table-column-title"><bean:message key="text.statusHistoryTableTitle" bundle="requests"/> </td>
                      	</tr>
					  	<tr  class="textBlue">
                        	<td width="15%"><bean:message key="text.statusDateTableColumn" bundle="requests"/></td>
                        	<td width="20%"><bean:message key="text.requestStatusTableColumn" bundle="requests"/></td>
                        	<td width="45%"><bean:message key="text.statusMessageTableColumn" bundle="requests"/></td>
						</tr>

	<!-- this is the list of status for the selected request -->
	<logic:iterate name="trailsList" id="trailInfo">
	
					  	<tr class="text8">
                        	<td><bean:write name="trailInfo" property="trailDate" format="dd-MM-yy HH:mm"/></td>
                        	<td class="<bean:write name="trailInfo" property="statusColorKey"/>"><bean:message name="trailInfo" property="statusBundleKey" bundle="general"/></td>
                        	<td><bean:write name="trailInfo" property="message"/></td>                        
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
                	<td height="15">
                		<div align="center">
                    	<table width="95%" class="table" cellpadding="0" cellspacing="1">
				      	<tr class="text8b">
                        	<td height="25" colspan="3" class="table-column-title"><bean:message key="text.outputFileTableTitle" bundle="requests"/> </td>
                      	</tr>
					  	<tr  class="textBlue">
	                    	<td width="15%"><bean:message key="text.outputTypeTableColumn" bundle="requests"/></td>
	                    	<td colspan="2"><bean:message key="text.filename" bundle="requests"/></td>
						</tr>

    <!-- this is the list of output files for the selected request -->                               
	<logic:notEmpty name="filesList">

		<logic:iterate id="fileMap" name="filesList">
					  	<tr class="text8">
                        	<td><bean:message name="fileMap" property="key" bundle="custom"/></td>
                        	<td class="textBlue">
                        	<logic:iterate id="file" name="fileMap" property="value">
	                        	<html:link action="/download-file" paramId="<%=RequestScopeConstants.REQUEST_SELECTEDFILE_KEY%>" paramName="file" paramProperty="filename"><bean:write name="file" property="truncatedFilepath"/></html:link><br>
	                        </logic:iterate>
	                        </td>
                      	</tr>
		</logic:iterate>
		
	</logic:notEmpty>
	
	<logic:empty name="filesList">
		              	<tr>                  
		                	<td colspan="2" class="text8b"><div align="center"><bean:message key="text.noGeneratedFilesFound" bundle="requests"/></div></td>
		              	</tr> 
	</logic:empty>
				    	</table>
                  		</div>
                  	</td>              
              	</tr>

			    <tr  valign="middle">
			    	<td height="25" class="textBlue">
			    		<div align="right"> 
			    		<html:link page="/list-requests.do"> :: <bean:message key="text.backToRequestList" bundle="requests"/> ::</html:link> &nbsp;&nbsp;&nbsp;&nbsp;
			    		<html:link href="javascript:history.back()" styleClass="textButton"> :: <bean:message key="text.backToRequest" bundle="requests"/> ::</html:link> &nbsp;&nbsp;&nbsp;&nbsp;
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
   	</td>
