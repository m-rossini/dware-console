<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>
<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>
<%@ page import="br.com.auster.billcheckout.thresholds.InvoiceThreshold"%>


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

	<TD>
		<SCRIPT>
			function submitForm(objId, type) {
				document.forms['invoiceForm'].elements['<%=RequestScopeConstants.REQUEST_INVOICE_ID_KEY%>'].value = objId;
				document.forms['invoiceForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;

				if (type == '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>') {
					if (!confirm('<bean:message key="message.sureToErase" bundle="invoice"/> ' + objId + ' ?')) {
						return;
					};
				}
				document.forms['invoiceForm'].submit();
			}
		</SCRIPT>
		<bean:define id="invoiceList" name="<%=RequestScopeConstants.REQUEST_LISTOFINVOICE_KEY%>" scope="request"/>
		<TABLE height="100%" width="100%">
			<TR>
				<TD widht="100%" valign="top">
					<!-- if exists cycleDate, then list them -->
					<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
					<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>
					<TABLE class="table" width="100%">
						<TR class="table-title">
							<TD colspan="8"><bean:message key="title.listOfInvoiceThreshold" bundle="invoice"/></TD>
						</TR>
						<TR class="table-column-title">
							<TD align="center" width="5%"><bean:message key="number.code" bundle="invoice"/></TD>
							<TD align="center" width="20%"><bean:message key="customerType" bundle="invoice"/></TD>
							<TD align="center" width="10%"><bean:message key="state" bundle="invoice"/></TD>
							<TD align="center" width="20%"><bean:message key="upperAmount" bundle="invoice"/></TD>
							<TD align="center" width="20%"><bean:message key="lowerAmount" bundle="invoice"/></TD>
							<TD align="center" width="10%"><bean:message key="remove" bundle="invoice"/></TD>
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
						<TR class="text8">
							<TD align="center">
								<FORM name="sortingForm" action="<html:rewrite page="/invoice.do"/>" method="post">
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
								</FORM>
							</TD>
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
									orderKey="UF"
									currentOrderKey="<%=currentOrderBy%>"
									currentOrientation="<%=currentOrientation%>"
									url="javascript:sortBy(''{0}'',''{1}'')"
									backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
									forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
									<html:img page="/images/ico_sort.jpg" border="0"/>
								</auster:sortUrl>
							</TD>
							<TD colspan="3">
								<FORM name="invoiceForm" action="<html:rewrite page="/invoice.do"/>" method="post" id="invoiceForm">
									<!-- Submit to new Page -->
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_INVOICE_ID_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>
									<!-- Filter -->
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>"/>
								</FORM>
							</TD>
						</TR>
<logic:notEmpty name="invoiceList">
						<!--                                          -->
						<!-- for each threshold found in              -->
						<!-- bck_parm_cycle_dates                     -->
						<!--                                          -->
<logic:iterate name="invoiceList" id="it">
						<TR class="text8">
							<TD align="center"><A href="javascript:submitForm('<bean:write name="it" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')"><bean:write name="it" property="uid"/></A></TD>
	<logic:empty name="it" property="customerType">
							<TD align="center"><bean:message key="option.null.value" bundle="invoice"/></TD>
	</logic:empty>
	<logic:notEmpty name="it" property="customerType">
							<TD align="center"><bean:write name="it" property="customerType.typeDescription"/></TD>
	</logic:notEmpty>
	<logic:empty name="it" property="UF">
							<TD align="center"><bean:message key="option.null.value" bundle="invoice"/></TD>
	</logic:empty>
	<logic:notEmpty name="it" property="UF">
							<TD align="center"><bean:write name="it" property="UF.state"/></TD>
	</logic:notEmpty>
	<logic:lessThan name="it" property='upperAmount' value="0.00">
							<TD align="right"> R$ - </TD>
	</logic:lessThan>
	<logic:greaterEqual name="it" property='upperAmount' value="0.00">
							<TD align="right"> R$ <bean:write name='it' property='upperAmount' format='0.00'/></TD>
	</logic:greaterEqual>

	<logic:lessThan name="it" property='lowerAmount' value="0.00">
							<TD align="right"> R$ - </TD>
	</logic:lessThan>
	<logic:greaterEqual name="it" property='lowerAmount' value="0.00">
							<TD align="right"> R$ <bean:write name="it" property="lowerAmount" format="0.00"/></TD>
	</logic:greaterEqual>							
							<TD align="center"><A href="javascript:submitForm('<bean:write name="it" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>')"><IMG src="images/no.jpg" border="0"/></A></TD>
						</TR>
</logic:iterate>
</logic:notEmpty>
<logic:empty name="invoiceList">
						<TR height="40">
							<TD colspan="8" class="text8" align="center"><bean:message key="no.data.found" bundle="invoice"/></TD>
						</TR>
</logic:empty>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD widht="100%"/>
			</TR>
<logic:notEmpty name="invoiceList">
			<TR class="text8" align="center">
				<TD widht="100%">
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
					<FORM name="moveToPage" action="<html:rewrite page="/invoice.do"/>" method="post">
						<!-- Sorting -->
						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>
						<!-- Page -->
						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
					</FORM>
				</TD>
			</TR>
</logic:notEmpty>
			<TR class="text8" align="right">
				<TD colspan="10">
					<A href="javascript:submitForm('', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')"><bean:message key="add.InvoiceThreshold" bundle="invoice"/></A>
				</TD>
			</TR>
		</TABLE>
	</TD>