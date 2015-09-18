<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>
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


<logic:present name="<%=RequestScopeConstants.REQUEST_USER_SELECTED_UPDATEACTION_KEY%>" scope="request">
<script language="Javascript">
	window.alert("<bean:message key="text.changePasswordWarning" bundle="login"/>");
</script>
</logic:present>


<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session" type="br.com.auster.security.model.User"/>


	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top">
        	<td height="15">
        		<table class="table" width="100%" align="center" cellpadding="0" cellspacing="0" >
              	<tr>
                	<td height="100%" class="table-title"><bean:message key="text.listTableTitle" bundle="requests"/></td>
              	</tr>
            	</table>
            </td>
        </tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>
		<form name="sortingForm" method="POST" action="<html:rewrite page="/list-requests.do"/>" >
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
		</form>

		<script language="Javascript">
			function sortBy(field, orientation) {
				document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>'].value=field;
				document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>'].value=orientation;
				document.forms['sortingForm'].submit();
			}
		</script>

		<tr  valign="top">
			<td  height="100%" width="100%">

				<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
				<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>

				<table width="100%" height="100%" align="center"  class="table" cellpadding="3" cellspacing="0">

			  	<tr height="20" class="table-column-title">
			    	<td width="7%"><div align="center"><bean:message key="text.requestTableColumn" bundle="requests"/></div></td>
                                        <td width="15%"><div align="center"><bean:message key="text.cycleIdTableColumn" bundle="requests"/></div></td>
			    	<td width="20%"><div align="center"><bean:message key="text.requestDateTableColumn" bundle="requests"/></div></td>
                                        <td width="15%"><div align="center"><bean:message key="text.requestStatusTableColumn" bundle="requests"/></div></td>
			    	<td width="15%"><div align="center">&nbsp;&nbsp;<bean:message key="text.totalAccountsTableColumn" bundle="requests"/></div></td>
                                        <td width="38%"><div align="center">&nbsp;&nbsp;<bean:message key="text.ownerNameTableColumn" bundle="requests"/></div></td>

                                        <% //if (user.hasPermission(PermissionConstants.PERMISSION_MANAGER_REQUEST_KEY)) { %>
                                        <td width="33%"><div align="center"><bean:message key="text.deleteRequest" bundle="requests"/></div></td>
			  							<%//} %>
                                        <%// if (user.hasPermission(PermissionConstants.PERMISSION_REQUEST_CREATE_KEY)) { %>
                                        <td width="33%"><div align="center"><bean:message key="text.remakeReports" bundle="requests"/></div></td>
                                        <%//} %>

                                        </tr>

			  	<tr height="20" class="text8">
			    	<td width="7%">
			    		<div align="center">
			    		<auster:sortUrl orderKey="request_id" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
				    	</auster:sortUrl>
			    		</div>
			    	</td>
                    <td width="5%">&nbsp;</td>
			    	<td width="20%">
			    		<div align="center">
						<auster:sortUrl orderKey="start_date" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
			    		</auster:sortUrl>
			    		</div>
			    	</td>
                    <td width="15%">&nbsp;</td>
                    <td width="15%">&nbsp;</td>
                    <td width="38%">&nbsp;</td>
			  	</tr>

<script>

function submitDetailsForm(requestId) {

    document.forms['showForm'].requestId.value = requestId;
    document.forms['showForm'].submit();
}

function submitDeleteForm(requestId) {

    var answer = confirm("Você esta certo que quer apagar a requisição " + requestId );
    if (answer){
        document.getElementById('reqId').value = requestId;
        document.forms['deleteForm'].submit();
    }
}

function submitRemakeReports(requestId) {

    document.getElementById('reqIdReport').value = requestId;
    document.forms['remakeReportsForm'].submit();
}

</script>

<form action="<html:rewrite page="/show-request-bundles.do"/>" method="post" name="showForm" name="showForm">
    <input type="hidden" name="requestId" value=""/>
</form>

<form action="<html:rewrite page="/delete-request.do"/>" method="POST" name="deleteForm" id="deleteForm">
    <input type="hidden" name="reqId" id="reqId" value=""/>
</form>

<form action="<html:rewrite page="/remake-reports.do"/>" method="POST" name="remakeReportsForm" id="remakeReportsForm">
    <input type="hidden" name="reqIdReport" id="reqIdReport" value=""/>
</form>


<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">

	<bean:define id="requestList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>

	<logic:iterate name="requestList" id="requestInfo">

				<tr height="10" class="text8">
      				<td><div align="center">
      					<a href="javascript:submitDetailsForm('<bean:write name="requestInfo" property="id" format="############"/>')">
      					<bean:write name="requestInfo" property="id" format=""/>
      					</a></div>
      				</td>
      				<td><div align="center"><bean:write name="requestInfo" property="cycleId"/></div></td>
      				<td><div align="center"><bean:write name="requestInfo" property="startDate" format="dd-MM-yy HH:mm"/></div></td>
      				<td class="<bean:write name="requestInfo" property="statusColorKey"/>"><div align="center"><bean:message name="requestInfo" property="statusBundleKey" bundle="general"/></div></td>
      				<td><div align="center"><bean:write name="requestInfo" property="totalCount" format="############"/>&nbsp;&nbsp;</div></td>
      				<td><div align="center">&nbsp;&nbsp;<bean:write name="requestInfo" property="owner"/></div></td>

	                <td>
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_MANAGER_REQUEST_KEY)) { %>
							<div align="center">
		                    	<a href="javascript:submitDeleteForm('<bean:write name="requestInfo" property="id" format="############"/>')"><html:img page="/images/no.jpg"  border="0" />
		                        </a>
		                    </div>
						<%} %>
	                </td>
	                <td>                				
	                	<%if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_MANAGER_REQUEST_KEY)) { %>
							<div align="center">
	                        	<a href="javascript:submitRemakeReports('<bean:write name="requestInfo" property="id" format="############"/>')"><html:img page="/images/report.png"  border="0" />
	                            </a>
	                        </div>
	                	<%} %>
	                </td>
               </tr>
	</logic:iterate>

				<tr height="45">
			    	<td colspan="6">&nbsp;</td>
			    </tr>
				<tr>
					<td colspan="6" valign="bottom"><div align="center">


	<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request" type="java.lang.String"/>
	<bean:define id="totalPages" name="<%=RequestScopeConstants.REQUEST_TOTALPAGES_KEY%>" scope="request" type="java.lang.String"/>

	<auster:index pageId="<%=pageId%>" totalPages="<%=totalPages%>"
				  style="text8b"
	              firstUrl="javascript:move({0})"
	              previousUrl="javascript:move({0})"
	              pageIndexUrl="javascript:move({0})"
	              nextUrl="javascript:move({0})"
	              lastUrl="javascript:move({0})"/>

	<script language="Javascript">

		function move(toPage) {
			document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>'].value = toPage;
			document.forms['moveToPage'].submit();
		}

	</script>

   	<form name="moveToPage" action="<html:rewrite page="/list-requests.do"/>" method="POST">
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>

   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
    </form>

					</div></td>
				</tr>
</logic:notEmpty>



<logic:empty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">
				<tr height="45" class="text8b">
			    	<td colspan="6"><div align="center"><bean:message key="text.noRequestsFound" bundle="requests"/></div></td>
			    </tr>
</logic:empty>

				<tr>
					<td colspan="6">&nbsp;</td>
				</tr>

				</table>
			</td>
   		</tr>
   		</table>
   	</td>

