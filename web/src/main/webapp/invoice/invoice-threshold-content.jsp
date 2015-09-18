<jsp:directive.page import="br.com.auster.dware.console.commons.RequestScopeConstants"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>
<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>

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

<!--  main TD -->
<td width="100%">

		<SCRIPT language="JAVASCRIPT" src="js/format.js"></SCRIPT>
		<SCRIPT language="Javascript">
			function validate() {
				if (validateInvoiceForm(document.invoiceForm)) {
					//if (validateAmounts(document.invoiceForm.upperAmount, document.invoiceForm.lowerAmount, <bean:message key="message.value.greater" bundle="invoice"/>)) {
					var msg = '<bean:message key="upperAmount" bundle="invoice"/> <bean:message key="message.value.greater" bundle="invoice"/> <bean:message key="lowerAmount" bundle="invoice"/>';
					if (validateAmounts(document.invoiceForm.upperAmount, document.invoiceForm.lowerAmount, msg)) {
						document.invoiceForm.submit();
					}
				}
			}
		</SCRIPT>
		<!-- Begin Validator Javascript Function-->
		<html:javascript formName="invoiceForm"/>
		<!-- End of Validator Javascript Function-->
		<TABLE width="100%"><TR><TD widht="100%">
		<!-- if exists invoice Threshold, then list them -->
		<html:form action="/invoice.do" method="post" onsubmit="return validateInvoiceForm(this);">
		
		
			<bean:define id="invoiceThreshold" name="<%=RequestScopeConstants.REQUEST_INVOICE_ID_KEY%>" scope="request"/>
			<bean:define id="customerTypeList" name="<%=RequestScopeConstants.REQUEST_LISTOFCUSTOMERTYPE_KEY%>" scope="request"/>
			<bean:define id="ufList" name="<%=RequestScopeConstants.REQUEST_LISTOFGEOGRAPHICDM_KEY%>" scope="request"/>
			
			<TABLE class="table" height="100%" width="100%">
				<TR class="table-title">
					<TD colspan="4">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<bean:message key="title.preFix.inclusion" bundle="invoice"/>
						<bean:message key="title.suffix.ofInvoiceThreshold" bundle="invoice"/>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:hidden name="invoiceThreshold" property="uid"/>
						<bean:message key="title.preFix.alteration" bundle="invoice"/>
						<bean:message key="title.suffix.ofInvoiceThreshold" bundle="invoice"/>
</logic:notEqual>
					</TD>
				</TR>
				<TR>
					<TD class="table-column-title" align="center" width="17%">
					<bean:message key="customerType" bundle="invoice"/></TD>
					<TD class="text8" align="left" width="33%">
						<%String customerTypeUid = (String) request.getAttribute(RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY);%>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:select name="invoiceThreshold" property="customerType" value="<%=customerTypeUid%>" tabindex="1" styleClass="input-field">
							<html:option value=""><bean:message key="option.null.value" bundle="invoice"/></html:option>
							<html:options collection="customerTypeList" property="uid" labelProperty="customDescription" name="<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>"/>
						</html:select>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:select name="invoiceThreshold" property="customerType" value="<%=customerTypeUid%>" tabindex="1" disabled="true" styleClass="input-field">
							<html:option value=""><bean:message key="option.null.value" bundle="invoice"/></html:option>
							<html:options collection="customerTypeList" property="uid" labelProperty="customDescription" name="<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>"/>
						</html:select>
</logic:notEqual>
					</TD>
					<TD class="table-column-title" align="center" width="17%"><bean:message key="state" bundle="invoice"/></TD>
					<TD class="text8" align="left" width="33%">
					<!-- list from BCK_GEO_DM -->
						<%String ufUid = (String) request.getAttribute(RequestScopeConstants.REQUEST_GEOGRAPHICDM_ID_KEY);%>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:select name="invoiceThreshold" property="UF" value="<%=ufUid%>" tabindex="2" styleClass="input-field">
							<html:option value=""><bean:message key="option.null.value" bundle="invoice"/></html:option>
							<html:options collection="ufList" property="uid" labelProperty="state" name="<%=RequestScopeConstants.REQUEST_GEOGRAPHICDM_ID_KEY%>"/>
						</html:select>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:select name="invoiceThreshold" property="UF" value="<%=ufUid%>" tabindex="2" disabled="true" styleClass="input-field">
							<html:option value=""><bean:message key="option.null.value" bundle="invoice"/></html:option>
							<html:options collection="ufList" property="uid" labelProperty="state" name="<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>"/>
						</html:select>
</logic:notEqual>
					</TD>
				</TR>
				<TR class="text8">
					<TD class="table-column-title" align="center" width="17%"><bean:message key="upperAmount" bundle="invoice"/></TD>
					<TD class="text8" align="left" width="33%">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">					
						<INPUT name="upperAmount" class="input-field" tabindex="3" size="10" maxlength="10"/>
</logic:equal>						
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">					
				<logic:lessThan name="invoiceThreshold" property='upperAmount' value="0.00">
						<INPUT name="upperAmount" value="" class="input-field" tabindex="3" size="10" maxlength="10"/>
				</logic:lessThan>
				<logic:greaterEqual name="invoiceThreshold" property='upperAmount' value="0.00">
						<INPUT name="upperAmount" value="<bean:write name="invoiceThreshold" property="upperAmount" format="0.00"/>" class="input-field" tabindex="3" size="10" maxlength="10"/>
				</logic:greaterEqual>
</logic:notEqual>						
						&nbsp;&nbsp;<A href="javascript:clearField(document.invoiceForm.upperAmount)"><bean:message key="reset" bundle="invoice"/></A>
						<!-- needed to link Javascript functions to monetary fields -->
						<SCRIPT language="Javascript">
							document.invoiceForm.upperAmount.style.textAlign = "right";
							document.invoiceForm.upperAmount.onkeypress = displayFormattedMonetaryAmount;
							document.invoiceForm.upperAmount.onkeydown	= storeKeyPressed;
						</SCRIPT>
					</TD>
					<TD class="table-column-title" align="center" width="17%"><bean:message key="lowerAmount" bundle="invoice"/></TD>
					<TD class="text8" align="left" width="33%">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">					
						<INPUT name="lowerAmount" class="input-field" tabindex="4" size="10" maxlength="10"/>
</logic:equal>						
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">					
				<logic:lessThan name="invoiceThreshold" property='lowerAmount' value="0.00">
						<INPUT name="lowerAmount" value="" class="input-field" tabindex="3" size="10" maxlength="10"/>
				</logic:lessThan>
				<logic:greaterEqual name="invoiceThreshold" property='lowerAmount' value="0.00">
						<INPUT name="lowerAmount" value="<bean:write name="invoiceThreshold" property="lowerAmount" format="0.00"/>" class="input-field" tabindex="3" size="10" maxlength="10"/>
				</logic:greaterEqual>
</logic:notEqual>						
						&nbsp;&nbsp;<A href="javascript:clearField(document.invoiceForm.lowerAmount)"><bean:message key="reset" bundle="invoice"/></A>
						<!-- needed to link Javascript functions to monetary fields -->
						<SCRIPT language="Javascript">
							document.invoiceForm.lowerAmount.style.textAlign = "right";
							document.invoiceForm.lowerAmount.onkeypress = displayFormattedMonetaryAmount;
							document.invoiceForm.lowerAmount.onkeydown	= storeKeyPressed;
						</SCRIPT>
					</TD>
				</TR>
				<TR>
					<TD class="table-column-title" align="center" width="17%"><bean:message key="hintMessage" bundle="invoice"/></TD>
					<TD class="text8" align="left" colspan="3">
						<html:text name="invoiceThreshold" property="hintMessage" styleClass="input-field" tabindex="5" size="96" maxlength="128"/>
					</TD>
				</TR>
				<TR class="text8" height="30">
					<TD colspan="4" align="right">
						<A href="<html:rewrite page="/invoice.do"/>"><bean:message key="cancel" bundle="invoice"/></A>&nbsp;&nbsp;
						<A href="javascript:onClick=validate()"><bean:message key="submit" bundle="invoice"/></A>&nbsp;&nbsp;&nbsp;&nbsp;
					</TD>
				</TR>
			</TABLE>
			<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="<bean:write name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>"/>
		</html:form>
		</TD></TR></TABLE>
	</TD>