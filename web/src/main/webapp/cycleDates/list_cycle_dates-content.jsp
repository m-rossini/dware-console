
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

	<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>

	<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterValue" type="java.lang.String"/>

	<td widht="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->
		<SCRIPT>
			function submitForm(objId, type) {
				document.forms['cycleDatesForm'].elements['<%=RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY%>'].value = objId;
				document.forms['cycleDatesForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;

				if (type == '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>') {
					if (!confirm('<bean:message key="message.sureToErase" bundle="cycleDates"/> ' + objId + ' ?')) {
						return;
					};
				}
				document.forms['cycleDatesForm'].submit();
			}
		</SCRIPT>
		<bean:define id="cycleDateList" name="<%=RequestScopeConstants.REQUEST_LISTOFCYCLEDATES_KEY%>" scope="request"/>
		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td height="15">
				<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
				<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>
          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
              	<tr>
              		<td height="100%" class="table-title"><bean:message key="title.listOfCycles" bundle="cycleDates"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>
			<TR valign="top">
				<TD widht="100%">
					<table height="100%" width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
						<TR height="20" class="table-column-title">
							<TD align="center" width="5%"><bean:message key="number.code" bundle="cycleDates"/></TD>
							<TD align="center" width="10%"><bean:message key="cycleCode" bundle="cycleDates"/></TD>
							<TD align="center" width="24%"><bean:message key="startDate" bundle="cycleDates"/> / <bean:message key="cutDate" bundle="cycleDates"/></TD>
							<TD align="center" width="17%"><bean:message key="clientType" bundle="cycleDates"/></TD>
							<TD align="center" width="12%"><bean:message key="dueDate" bundle="cycleDates"/></TD>
							<TD align="center" width="12%"><bean:message key="issueDate" bundle="cycleDates"/></TD>
							<TD align="center" width="12%"><bean:message key="referenceDate" bundle="cycleDates"/></TD>
							<TD align="center" width="8%"><bean:message key="remove" bundle="cycleDates"/></TD>
						</TR>

						<!--							-->
						<!--		SORTING FORM		-->
						<!--							-->
						<SCRIPT language="Javascript">
							function sortBy(field, orientation) {
								document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>'].value=field;
								document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>'].value=orientation;
								document.forms['sortingForm'].submit();
							}
						</SCRIPT>
						<TR class="text8" height="40">
							<TD colspan="8" align="right"><bean:message key="criteria" bundle="cycleDates"/>:
								<SELECT id="filterCriteria" class="input-field">
									<OPTION value="cycleCode"><bean:message key="cycleCode" bundle="cycleDates"/></OPTION>
								</SELECT>
								<INPUT type="text" class="input-field" id="cycleCodeFilter" maxlength="10" size="10" value="<%=filterValue%>"/>
								<SCRIPT language="Javascript">
									function filter() {
										var filterCriteria  = document.getElementById('filterCriteria').value;
										var cycleCodeFilter = document.getElementById('cycleCodeFilter').value;
										document.forms['cycleDatesForm'].elements['<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>'].value=filterCriteria;
										document.forms['cycleDatesForm'].elements['<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>'].value=cycleCodeFilter;
										document.forms['cycleDatesForm'].submit();
									}
								</SCRIPT>
								&nbsp;&nbsp;<A href="javascript:filter()"><bean:message key="filter" bundle="cycleDates"/></A>&nbsp;&nbsp;&nbsp;&nbsp;
							</TD>
						</TR>
						<TR class="text8">
							<TD align="center">
								<FORM name="sortingForm" action="<html:rewrite page="/cycles.do"/>" method="post">
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
								</FORM>
							</TD>
							<TD align="center">
								<auster:sortUrl
									orderKey="cycleCode"
									currentOrderKey="<%=currentOrderBy%>"
									currentOrientation="<%=currentOrientation%>"
									url="javascript:sortBy(''{0}'',''{1}'')"
									backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
									forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
									<html:img page="/images/ico_sort.jpg" border="0"/>
								</auster:sortUrl>
							</TD>
							<TD align="center">
								<auster:sortUrl
									orderKey="endDate"
									currentOrderKey="<%=currentOrderBy%>"
									currentOrientation="<%=currentOrientation%>"
									url="javascript:sortBy(''{0}'',''{1}'')"
									backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
									forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
									<html:img page="/images/ico_sort.jpg" border="0"/>
								</auster:sortUrl>
							</TD>
							<TD align="center"></TD>
							<TD align="center">
								<auster:sortUrl
									orderKey="dueDate"
									currentOrderKey="<%=currentOrderBy%>"
									currentOrientation="<%=currentOrientation%>"
									url="javascript:sortBy(''{0}'',''{1}'')"
									backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
									forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
									<html:img page="/images/ico_sort.jpg" border="0"/>
								</auster:sortUrl>
							</TD>
							<TD colspan="3">
								<FORM name="cycleDatesForm" action="<html:rewrite page="/cycles.do"/>" method="post" id="cycleDatesForm">
									<!-- Submit to new Page -->
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="unspecified"/>
									<!-- Filter -->
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>"/>
								</FORM>
							</TD>
						</TR>
<logic:notEmpty name="cycleDateList">
						<!--                                          -->
						<!-- for each threshold found in              -->
						<!-- bck_parm_cycle_dates                     -->
						<!--                                          -->
<logic:iterate name="cycleDateList" id="cd">
						<TR class="text8">
							<TD align="center"><A href="javascript:submitForm('<bean:write name="cd" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')"><bean:write name="cd" property="uid"/></A></TD>
							<TD align="center"><bean:write name="cd" property="cycleCode"/></TD>
							<TD align="center"><bean:write name="cd" property="startDate" format="dd/MM/yyyy"/> - <bean:write name="cd" property="endDate" format="dd/MM/yyyy"/></TD>
							<TD align="center">
								<logic:notEmpty name="cd" property="customerType">
									<bean:write name="cd" property="customerType.typeDescription"/>
								</logic:notEmpty>
								<logic:empty name="cd" property="customerType">
									<bean:message key="custtype.all" bundle="cycleDates"/>
								</logic:empty>
							</TD>
							<TD align="center"><bean:write name="cd" property="dueDate" format="dd/MM/yyyy"/></TD>
							<TD align="center"><bean:write name="cd" property="issueDate" format="dd/MM/yyyy"/></TD>
							<TD align="center"><bean:write name="cd" property="referenceDate" format="dd/MM/yyyy"/></TD>
							<TD align="center">
								<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
								<A href="javascript:submitForm('<bean:write name="cd" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>')"><IMG src="images/no.jpg" border="0"/></A>
								<% } %>
							</TD>
						</TR>
</logic:iterate>
</logic:notEmpty>
<logic:empty name="cycleDateList">
						<TR height="40">
							<TD colspan="8" class="text8" align="center"><bean:message key="no.data.found" bundle="cycleDates"/></TD>
						</TR>
</logic:empty>

						<tr height="45">
					    	<td colspan="8">&nbsp;</td>
					    </tr>

						<TR align="center">
							<TD colspan="8" valign="botton">
			<logic:notEmpty name="cycleDateList">
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
								<FORM name="moveToPage" action="<html:rewrite page="/cycles.do"/>" method="post">
									<!-- Sorting -->
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>
									<!-- Page -->
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
									<!-- Filter -->
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value=""/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterValue%>"/>
								</FORM>
			</logic:notEmpty>
								<br/>
								<div align="right">
									<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
									<A href="javascript:submitForm('', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')"><bean:message key="add.date" bundle="cycleDates"/></A>
									<% } %>
								</div>
								<br/>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</TD>



