<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>
<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>


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
	function submitForm(objId, type) {
		document.forms['carrierForm'].elements['<%=RequestScopeConstants.REQUEST_CARRIER_ID_KEY%>'].value = objId;
		document.forms['carrierForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;

		if (type == '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>') {
			if (!confirm('<bean:message key="message.sureToErase" bundle="carriers"/>')) {
				return;
			};
		}
		document.forms['carrierForm'].submit();
	}
</script>

<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>

<bean:define id="carrierList" name="<%=RequestScopeConstants.REQUEST_LISTOFCARRIERS_KEY%>" scope="request"/>
<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterValue" type="java.lang.String"/>

	<td>
		<table height="100%" width="100%" cellpadding="0" cellspacing="0">

			<tr valign="top">
				<td height="15">
          		<table class="table" width="100%" align="center" cellpadding="0" cellspacing="0">
              		<tr class="text8b">
                		<td class="table-title">
                			<bean:message key="title.listOfCarriers" bundle="carriers"/>
                		</td>
              		</tr>
            	</table>
            	</td>
			</tr>

			<tr valign="top">
	        	<td height="2"></td>
	        </tr>

			<tr  valign="top">
				<td width="100%">

				<!-- if exists carriers, then list them -->
				<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
				<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>

				<table height="100%" width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">

				  	<tr height="20" class="table-column-title">
						<td align="center" width="10%"><bean:message key="csp" bundle="carriers"/></td>
						<td align="center" width="10%"><bean:message key="state" bundle="carriers"/></td>
						<td align="center" width="50%"><bean:message key="name.preFix" bundle="carriers"/> <bean:message key="carrier.suffix.theCarrier" bundle="carriers"/></td>
						<!--  td align="center" width="15%"><bean:message key="remove.preFix" bundle="carriers"/> <bean:message key="address.preFix" bundle="carriers"/></td -->
					</tr>
					<tr class="text8" height="40">
						<td colspan="8" align="right"><bean:message key="criteria" bundle="carriers"/>:
							<select id="filterCriteria" class="input-field">
								<option value="carrierCode"><bean:message key="csp" bundle="carriers"/></option>
								<option value="carrierState"><bean:message key="state" bundle="carriers"/></option>
								<option value="carrierCompany"><bean:message key="name.preFix" bundle="carriers"/> <bean:message key="carrier.suffix.theCarrier" bundle="carriers"/></option>
							</select>
							<input type="text" class="input-field" id="cycleCodeFilter" maxlength="10" size="10" value="<%=filterValue%>"/>
							<script language="Javascript">
								function filter() {
									var filterCriteria  = document.getElementById('filterCriteria').value;
									var cycleCodeFilter = document.getElementById('cycleCodeFilter').value;
									document.forms['carrierForm'].elements['<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>'].value=filterCriteria;
									document.forms['carrierForm'].elements['<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>'].value=cycleCodeFilter;
									document.forms['carrierForm'].submit();
								}
							</script>
							&nbsp;&nbsp;<a href="javascript:filter()"><bean:message key="filter" bundle="cycleDates"/></a>&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<!--							-->
					<!--		SORTING FORM		-->
					<!--							-->
					<script language="Javascript">
						function sortBy(field, orientation) {
							document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>'].value=field;
							document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>'].value=orientation;
							document.forms['sortingForm'].submit();
						}
					</script>
					<tr class="text8">
						<td align="center">
							<FORM name="sortingForm" action="<html:rewrite page="/carrier.do"/>" method="post">
								<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
								<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
							</FORM>
							<auster:sortUrl
								orderKey="carrierCode"
								currentOrderKey="<%=currentOrderBy%>"
								currentOrientation="<%=currentOrientation%>"
								url="javascript:sortBy(''{0}'',''{1}'')"
								backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
								forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
								<html:img page="/images/ico_sort.jpg" border="0"/>
							</auster:sortUrl>
						</td>
						<td align="center">
							<auster:sortUrl
								orderKey="carrierState"
								currentOrderKey="<%=currentOrderBy%>"
								currentOrientation="<%=currentOrientation%>"
								url="javascript:sortBy(''{0}'',''{1}'')"
								backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
								forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
								<html:img page="/images/ico_sort.jpg" border="0"/>
							</auster:sortUrl>
						</td>
						<td align="center">
							<auster:sortUrl
								orderKey="carrierCompany"
								currentOrderKey="<%=currentOrderBy%>"
								currentOrientation="<%=currentOrientation%>"
								url="javascript:sortBy(''{0}'',''{1}'')"
								backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
								forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
								<html:img page="/images/ico_sort.jpg" border="0"/>
							</auster:sortUrl>
						<!--
						</td>
						<td align="center">
						 -->
							<FORM name="carrierForm" action="<html:rewrite page="/carrier.do"/>" method="post" id="carrierForm">
								<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_CARRIER_ID_KEY%>"/>
								<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="unspecified"/>
								<!-- Filter -->
								<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>"/>
								<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>"/>
							</FORM>
						</td>
					</tr>

<logic:notEmpty name="carrierList">

					<!--                                          -->
					<!-- for each carrier found in bck_carrier_dm -->
					<!--                                          -->
	<logic:iterate name="carrierList" id="cd">
					<tr class="text8">
						<td align="center"><A href="javascript:submitForm('<bean:write name="cd" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')"><bean:write name="cd" property="carrierCode"/></A></td>
						<td align="center"><bean:write name="cd" property="carrierState"/></td>
						<td><bean:write name="cd" property="carrierCompany"/></td>
						<!--  td align="center"><A href="javascript:submitForm('<bean : write name="cd" property="uid"/>', '< % = RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE % >')"><IMG src="images/no.jpg" border="0"/></A></td -->
					</tr>
	</logic:iterate>

					<tr height="45">
				    	<td colspan="3">&nbsp;</td>
				    </tr>

					<tr>
						<td colspan="3" valign="bottom"><div align="center">

						<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request" type="java.lang.String"/>
						<bean:define id="totalPages" name="<%=RequestScopeConstants.REQUEST_TOTALPAGES_KEY%>" scope="request" type="java.lang.String"/>
						<auster:index
							pageId="<%=pageId%>"
							totalPages="<%=totalPages%>"
							style="text8b"
							firstUrl="javascript:move({0})"
							previousUrl="javascript:move({0})"
							pageIndexUrl="javascript:move({0})"
							nextUrl="javascript:move({0})"
							lastUrl="javascript:move({0})"/>
						<SCRIPT language="Javascript">
							function move(toPage) {
								document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>'].value = toPage;
								var filterCriteria  = document.getElementById('filterCriteria').value;
								var cycleCodeFilter = document.getElementById('cycleCodeFilter').value;
								document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>'].value=filterCriteria;
								document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>'].value=cycleCodeFilter;
								document.forms['moveToPage'].submit();
							}
						</SCRIPT>
						<FORM name="moveToPage" action="<html:rewrite page="/carrier.do"/>" method="post">
							<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
							<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>
							<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
							<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
							<!-- Filter -->
							<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value=""/>
							<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterValue%>"/>
						</FORM>
						</div>
						<br/>
							<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
							<A href="javascript:submitForm(null, '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>')"><bean:message key="include.preFix" bundle="carriers"/> <bean:message key="carrier" bundle="carriers"/></A>
							<% } %>
						<br/>

						</td>
					</tr>

</logic:notEmpty>

<logic:empty name="carrierList">
					<tr height="40">
						<td colspan="3" class="text8" align="center"><bean:message key="no.data.found" bundle="carriers"/></td>
					</tr>
</logic:empty>

				</table>
				</td>
   			</tr>
   		</table>
   	</td>
