
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

<script language="javascript">
function submitDetailsForm(requestId, accountId) {
	document.forms['requestDetailsForm'].requestId.value = requestId;
	document.forms['requestDetailsForm'].accountId.value = accountId;
	document.forms['requestDetailsForm'].submit();
}


function submitRequestBundleForm(requestId) {

    document.forms['showForm'].requestId.value = requestId;
    document.forms['showForm'].submit();
}
</script>


<bean:define id="requestInfo" name="<%=RequestScopeConstants.REQUEST_REQINFO_KEY%>" scope="request"/>


<html:form action="/show-request-details" method="post">
	<html:hidden property="requestId" value=""/>
	<html:hidden property="accountId" value=""/>
</html:form>


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
		<tr valign="top">
			<td width="100%">
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
                      		<td width="15%"><bean:message key="text.requestTableColumn" bundle="requests"/></td>
	                      	<td width="20%"><bean:message key="text.requestDateTableColumn" bundle="requests"/></td>
                      		<td width="25%"><bean:message key="text.requestStatusTableColumn" bundle="requests"/></td>
                      		<td width="25%"><bean:message key="text.finishRateTableColumn" bundle="requests"/></td>
                    	</tr>
						<tr class="text8">
                      		<td>
                   				<bean:write name="requestInfo" property="id" format=""/>
                      		</td>
                      		<td><bean:write name="requestInfo" property="startDate" format="dd-MM-yy HH:mm"/></td>
                      		<td  class="<bean:write name="requestInfo" property="statusColorKey"/>"><bean:message name="requestInfo" property="statusBundleKey" bundle="general"/></td>
                      		<td>
                      			<bean:write name="requestInfo" property="finishedCount" format="############"/>/
                      			<bean:write name="requestInfo" property="totalCount" format="############"/>&nbsp;
                      			<bean:message key="text.selectAccountsTableTitle" bundle="requests"/>
                      		</td>
                    	</tr>
						</table>
					</td>
              	</tr>

              	<tr height="40">
              		<td></td>
              	</tr>
              	<tr valign="bottom">
                	<td align="right" class="text8">

					<!- ###################################################################### -->
					<!-                               Filtering Form                           -->
					<!- ###################################################################### -->

				   	<form name="filterPage" action="<html:rewrite page="/show-request-accounts.do"/>" method="POST">

					   	<input type="hidden" name="requestId" value="<bean:write name="requestInfo" property="id" format="############"/>"/>

					   		<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" id="filterBy" type="java.lang.String"/>

	                			<bean:message key="text.labe.filterBy" bundle="requests"/> :&nbsp;&nbsp;
	                			<auster:selectBox style="text8" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" defaultKey="<%=filterBy%>">
	                				<auster:selectKey key="accountId">Account Id</auster:selectKey>
	                			</auster:selectBox>
	                			&nbsp;&nbsp;
	                			<input type="text" class="text8" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value=""/>
	                			<font class="text8b">
	                			&nbsp;&nbsp;<a href="javascript:document.forms['filterPage'].submit();"> :: <bean:message key="text.button.filter" bundle="requests"/> :: </a>&nbsp;&nbsp;&nbsp;&nbsp;</font>

	                			<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>">
	                				<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterCondition" type="java.lang.String"/>
	                				<script>
	                					document.forms['filterPage'].elements['<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>'].value='<%=filterCondition%>'
	                				</script>
	                			</logic:notEmpty>
				    </form>
                	</td>
              	</tr>

			  	<tr valign="top">
                	<td>
                		<div align="center">
                    	<table width="95%" align="center" class="table" cellpadding="0" cellspacing="1">
					   	<tr>
                        	<td height="25" colspan="4" class="table-column-title"><bean:message key="text.accountListTableTitle" bundle="requests"/></div></td>
                      	</tr>
					  	<tr class="textBlue">
                        	<td width="15%"><div align="center"><bean:message key="text.accountTableColumn" bundle="requests"/></td>
                        	<td width="35%"><div align="center"><bean:message key="text.customerNameTableColumn" bundle="requests"/></div></td>
                        	<td width="15%"><div align="center"><bean:message key="text.requestDateTableColumn" bundle="requests"/></div></td>
                        	<td width="15%"><div align="center"><bean:message key="text.requestStatusTableColumn" bundle="requests"/></div></td>
                      	</tr>

						<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
						<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>

						<form name="sortingForm" method="POST" action="<html:rewrite page="/show-request-accounts.do"/>">
						   	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>" value="<bean:write name="requestInfo" property="id" format="############"/>"/>
					   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
					   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
					   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value="<%=filterBy%>"/>

						<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>">
							<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterCondition" type="java.lang.String"/>
						   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterCondition%>"/>
						</logic:notEmpty>
						</form>

						<script language="Javascript">
							function sortBy(field, orientation) {
								document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>'].value=field;
								document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>'].value=orientation;
								document.forms['sortingForm'].submit();
							}
						</script>

					  	<tr height="20" class="text8">
					    	<td>
		                    	<div align="center">
					    		<auster:sortUrl orderKey="proc_request.request_label" currentOrderKey="<%=currentOrderBy%>"
					    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
					    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
					    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
								    <html:img page="/images/ico_sort.jpg" border="0"/>
						    	</auster:sortUrl>
					    		</div>
					    	</td>
		                    <td>&nbsp;</td>
		                    <td>&nbsp;</td>
					    	<td>
					    		<div align="center">
								<auster:sortUrl orderKey="proc_request.latest_status" currentOrderKey="<%=currentOrderBy%>"
					    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
					    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
					    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
								    <html:img page="/images/ico_sort.jpg" border="0"/>
					    		</auster:sortUrl>
					    		</div>
					    	</td>
					  	</tr>

<logic:present name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">

	<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request"/>

	<bean:define id="accountList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>

	<logic:iterate name="accountList" id="accountInfo">

					  	<tr class="text8">
                        	<td><div align="center"><a href="javascript:submitDetailsForm('<bean:write name="requestInfo" property="id" format="############"/>','<bean:write name="accountInfo" property="id" format="############"/>')"><bean:write name="accountInfo" property="accountId"/></a></div></td>
                        	<td><div align="left">&nbsp;&nbsp;<bean:write name="accountInfo" property="customerName"/></div></td>
                      		<td><div align="center"><bean:write name="accountInfo" property="startDate" format="dd-MM-yy HH:mm"/></div></td>
                      		<td class="<bean:write name="accountInfo" property="statusColorKey"/>"><div align="center"><bean:message name="accountInfo" property="statusBundleKey" bundle="general"/></div></td>
                      	</tr>
	</logic:iterate>
</logic:present>
				    	</table>
                  		</div>
                  	</td>
              	</tr>
				<tr>
					<td></td>
				</tr>
			    <tr  valign="middle" class="text8b">
			    	<td height="25">
			    		<div align="right">
			    		<html:link page="/list-requests.do" styleClass="textBlue"> :: <bean:message key="text.backToRequestList" bundle="requests"/> ::</html:link> &nbsp;&nbsp;
			    		<a href="javascript:submitRequestBundleForm('<bean:write name="requestInfo" property="id" format="############"/>')"> :: <bean:message key="text.backToRequest" bundle="requests"/> :: </a>&nbsp;&nbsp;&nbsp;&nbsp;
			    		</div>
			    	</td>
			    </tr>

				<tr height="45">
			    	<td colspan="6">&nbsp;</td>
			    </tr>

				<tr height="100%">
					<td colspan="4" valign="bottom"><div align="center">

<logic:present name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">

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

	<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" id="filterBy" type="java.lang.String"/>

   	<form name="moveToPage" action="<html:rewrite page="/show-request-accounts.do"/>" method="POST">
	   	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>" value="<bean:write name="requestInfo" property="id" format="############"/>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>

   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>

   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value="<%=filterBy%>"/>

	<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>">
		<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterCondition" type="java.lang.String"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterCondition%>"/>
	</logic:notEmpty>
    </form>

</logic:present>
					</div></td>
				</tr>
              	</table>
			</td>
   		</tr>
   		</table>
   	</td>


<form action="<html:rewrite page="/show-request-bundles.do"/>" method="post" name="showForm">
    <input type="hidden" name="requestId" value=""/>
</form>

