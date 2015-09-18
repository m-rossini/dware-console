<%@ page language="java" pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="br.com.auster.dware.console.commons.RequestScopeConstants"/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>
<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>

<auster:checkLogon sessionKey="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>">
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

	<TD widht="100%">
		<SCRIPT>
			function submitForm(objId, type) {
				document.forms['customerTypeForm'].elements['<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>'].value = objId;
				document.forms['customerTypeForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;

				if (type == '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>') {
					if (!confirm('<bean:message key="message.sureToErase" bundle="customerType"/>')) {
						return;
					};
				}
				document.forms['customerTypeForm'].submit();
			}
		</SCRIPT>
		<bean:define id="customerTypeList" name="<%=RequestScopeConstants.REQUEST_LISTOFCUSTOMERTYPE_KEY%>" scope="request"/>

		        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td height="15">
          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
              	<tr>
              		<td height="100%" class="table-title"><bean:message key="title.listOfCustomers" bundle="customerType"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>

			<TR valign="top">
				<TD widht="100%">
					<!-- if exists customerType, then list them -->
					<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
					<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>
					<table height="100%"width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
						<TR height="20" class="table-column-title">
							<TD width="5%"><div align="center" width="5%"><bean:message key="number.code" bundle="customerType"/></div></TD>
							<TD width="15%"><div align="center" ><bean:message key="code" bundle="customerType"/></div></TD>
							<TD width="15%"><div align="center" width="40%"><bean:message key="description" bundle="customerType"/></div></TD>
							<TD width="10%"><div align="center" ><bean:message key="remove" bundle="customerType"/></div></TD>
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
						<TR height="20" class="text8">
							<TD/>
							<TD align="center">
								<auster:sortUrl
									orderKey="customerType"
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
									orderKey="typeDescription"
									currentOrderKey="<%=currentOrderBy%>"
									currentOrientation="<%=currentOrientation%>"
									url="javascript:sortBy(''{0}'',''{1}'')"
									backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
									forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
									<html:img page="/images/ico_sort.jpg" border="0"/>
								</auster:sortUrl>
							</TD>
							<TD>
								<FORM name="sortingForm" action="<html:rewrite page="/customer.do"/>" method="post">
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
								</FORM>
								<FORM name="customerTypeForm" action="<html:rewrite page="/customer.do"/>" method="post" id="customerTypeForm">
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>
								</FORM>
							</TD>
						</TR>
<logic:notEmpty name="customerTypeList">
						<!--                                          -->
						<!-- for each threshold found in              -->
						<!-- bck_parm_cust_type                       -->
						<!--                                          -->
	<logic:iterate name="customerTypeList" id="ct">
						<TR height="10" class="text8">
							<TD align="center"><A href="javascript:submitForm('<bean:write name="ct" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')"><bean:write name="ct" property="uid"/></A></TD>
							<TD align="center"><bean:write name="ct" property="customerType"/></TD>
							<TD align="left"><bean:write name="ct" property="typeDescription"/></TD>
							<TD align="center">
								<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
								<A href="javascript:submitForm('<bean:write name="ct" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>')"><IMG src="images/no.jpg" border="0"/></A>
								<% } %>
							</TD>
						</TR>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="customerTypeList">
						<TR height="40">
							<TD colspan="8" class="text8" align="center"><bean:message key="no.data.found" bundle="customerType"/></TD>
						</TR>
</logic:empty>

					<tr height="45">
				    	<td colspan="8">&nbsp;</td>
				    </tr>


						<TR align="center">
							<TD colspan="8" valign="bottom">
			<logic:notEmpty name="customerTypeList">
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
										document.forms['moveToPage'].submit();
									}
								</SCRIPT>
								<FORM name="moveToPage" action="<html:rewrite page="/customer.do"/>" method="post">
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>

									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
								</FORM>
			</logic:notEmpty>
								<br/>
								<div class="text8" align="right">
										<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
										<A href="javascript:submitForm('', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')"><bean:message key="add.customerType" bundle="customerType"/></A>
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