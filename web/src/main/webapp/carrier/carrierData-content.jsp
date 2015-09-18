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

	<td widht="100%">
		<SCRIPT>
			function submit(type) {
				document.forms['carrierDataForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;
				document.forms['carrierDataForm'].submit();
			}

			function validate() {
				if (validateCarrierDataForm(document.carrierDataForm)) {
					document.carrierDataForm.submit();
				}
			}
		</SCRIPT>
        <!-- Begin Validator Javascript Function-->
        <html:javascript formName="carrierDataForm"/>
        <!-- End of Validator Javascript Function-->

		<!-- if exists carriers, then list them -->
		<html:form action="/detail.do" method="post" styleId="carrierDataForm" onsubmit="return validateCarrierDataForm(this);">

		<table width="100%" cellpadding="0" cellspacing="0">

		<tr  valign="top">
			<td  height="15">
			<bean:define id="carrier" name="<%=RequestScopeConstants.REQUEST_CARRIER_ID_KEY%>" scope="request"/>
			<bean:define id="carrierLD" name="<%=RequestScopeConstants.REQUEST_CARRIER_LD_ID_KEY%>" scope="request"/>

			<table class="table" width="100%" cellpadding="0" cellspacing="0">
				<tr class="table-title">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
					<td class="table-title"><bean:message key="title.preFix.inclusion" bundle="carriers"/> <bean:message key="carrier.suffix.ofCarrier" bundle="carriers"/></td>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
					<td class="table-title"><bean:message key="title.preFix.alteration" bundle="carriers"/> <bean:message key="carrier.suffix.ofCarrier" bundle="carriers"/></td>
</logic:notEqual>
				</tr>
           	</table>
            </td>
        </tr>

		<tr  valign="top">
			<td width="100%">
				<table width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
				<TR class="table-column-title">
					<TD align="left" width="10%"><bean:message key="csp" bundle="carriers"/></TD>
					<TD align="left" width="10%"><bean:message key="state" bundle="carriers"/></TD>
					<TD align="left" colspan="8"><bean:message key="name.preFix" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD>
						<INPUT type="hidden" name="carrierUid"value="<bean:write name="carrier" property="carrierDimension.uid"/>">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<INPUT type="text" name="carrierCode" value="<bean:write name="carrier" property="carrierDimension.carrierCode"/>" class="mandatory-input-field" size="2" maxlength="2" tabindex="1"/>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<INPUT type="text" name="carrierCode" value="<bean:write name="carrier" property="carrierDimension.carrierCode"/>" readonly="readonly" class="readonly-input-field" size="2" maxlength="2" tabindex="1"/>
</logic:notEqual>
					</TD>
					<TD>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<INPUT type="text" name="carrierState" value="<bean:write name="carrier" property="carrierDimension.carrierState"/>" class="mandatory-input-field" size="2" maxlength="2" tabindex="2" style="text-transform: uppercase;"/>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<INPUT type="text" name="carrierState" value="<bean:write name="carrier" property="carrierDimension.carrierState"/>" readonly="readonly" class="readonly-input-field" size="2" maxlength="2"/>
</logic:notEqual>
					</TD>
					<TD colspan="8">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<INPUT type="text" name="carrierCompany" value="<bean:write name="carrier" property="carrierDimension.carrierCompany"/>" class="input-field" size="48" maxlength="48" tabindex="3"/>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<INPUT type="text" name="carrierCompany" value="<bean:write name="carrier" property="carrierDimension.carrierCompany"/>" readonly="readonly" class="input-field" size="48" maxlength="48"/>
</logic:notEqual>
					</TD>
				</TR>
           		</table>
            </td>
		</tr>
		<tr height="40" valign="top">
        	<td height="2"></td>
        </tr>

		<!--                                   -->
		<!-- LOCAL ADDRESS FOR CURRENT CARRIER -->
		<!--                                   -->

		<tr  valign="top">
			<td  height="15">

				<table width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
				<TR class="table-title">
					<TD class="table-title"><bean:message key="address.preFix" bundle="carriers"/> <bean:message key="carrier.suffix.theCarrier" bundle="carriers"/></TD>
				</TR>
           		</table>
            </td>
		</tr>

		<tr valign="top">
        	<td height="2"></td>
        </tr>

		<tr  valign="top">
			<td width="100%">

				<TABLE width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
				<TR class="table-column-title">
					<TD align="left" colspan="10"><bean:message key="fullName" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="10"><input type="text" name="fullName" class="input-field" value="<bean:write name="carrier" property="fullName"/>" size="64" maxlength="128" tabindex="4"/></TD>
				</TR>
				<TR class="table-column-title">
					<TD align="left" colspan="7"><bean:message key="address.preFix" bundle="carriers"/></TD>
					<TD align="left" colspan="3"><bean:message key="number" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="7"><input type="text" name="addressStreet" class="input-field" value="<bean:write name="carrier" property="addressStreet"/>" size="64" maxlength="128" tabindex="5"/></TD>
					<TD colspan="3"><input type="text" name="addressNumber" class="input-field" value="<bean:write name="carrier" property="addressNumber"/>" size="12" maxlength="32" tabindex="6"/></TD>
				</TR>
				<TR class="table-column-title">
					<TD align="left" colspan="4"><bean:message key="complement" bundle="carriers"/></TD>
					<TD align="left" colspan="2"><bean:message key="zipCode" bundle="carriers"/></TD>
					<TD align="left" colspan="4"><bean:message key="city" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="4"><input type="text" name="addressComplement" class="input-field" value="<bean:write name="carrier" property="addressComplement"/>" size="28" maxlength="32" tabindex="7"/></TD>
					<TD colspan="2"><input type="text" name="addressZip" class="input-field" value="<bean:write name="carrier" property="addressZip"/>" size="9" maxlength="9" tabindex="8"/></TD>
					<TD colspan="4"><input type="text" name="addressCity" class="input-field" value="<bean:write name="carrier" property="addressCity"/>" size="28" maxlength="32" tabindex="9"/></TD>
				</TR>
				<TR class="table-column-title">
					<TD align="left" colspan="4"><bean:message key="taxId" bundle="carriers"/></TD>
					<TD align="left" colspan="3"><bean:message key="stateEnrollNumber" bundle="carriers"/></TD>
					<TD align="left" colspan="3"><bean:message key="cityEnrollNumber" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="4"><input type="text" name="taxId" class="input-field" value="<bean:write name="carrier" property="taxId"/>" size="24" maxlength="24" tabindex="10"/></TD>
					<TD colspan="3"><input type="text" name="stateEnrollNumber" class="input-field" value="<bean:write name="carrier" property="stateEnrollNumber"/>" size="24" maxlength="24" tabindex="11"/></TD>
					<TD colspan="3"><input type="text" name="cityEnrollNumber" class="input-field" value="<bean:write name="carrier" property="cityEnrollNumber"/>" size="24" maxlength="24" tabindex="12"/></TD>
				</TR>
				<TR class="table-column-title">
					<TD align="left" colspan="5"><bean:message key="website" bundle="carriers"/></TD>
					<TD align="left" colspan="5"><bean:message key="address.preFix" bundle="carriers"/> <bean:message key="email.suffix" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="5"><input type="text" name="addressWeb" class="input-field" value="<bean:write name="carrier" property="addressWeb"/>" size="50" maxlength="64" tabindex="13"/></TD>
					<TD colspan="5"><input type="text" name="addressEmail" class="input-field" value="<bean:write name="carrier" property="addressEmail"/>" size="50" maxlength="64" tabindex="14"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="10">&nbsp;</TD>
				</TR>
				</TABLE>

		<!--                                   -->
		<!--   LD ADDRESS FOR CURRENT CARRIER  -->
		<!--                                   -->

		<tr  valign="top">
			<td width="100%">

				<table width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
				<TR class="table-title">
					<TD colspan="10"><bean:message key="addressLD.preFix" bundle="carriers"/> <bean:message key="carrier.suffix.theCarrier" bundle="carriers"/></TD>
				</TR>
           		</table>
            </td>
		</tr>

		<tr  valign="top">
			<td width="100%">

				<TABLE width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
				<TR class="table-column-title">
					<TD align="left" colspan="10"><bean:message key="fullName" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="10"><input type="text" name="ldFullName" class="input-field" value="<bean:write name="carrierLD" property="fullName"/>" size="64" maxlength="128" tabindex="15"/></TD>
				</TR>
				<TR class="table-column-title">
					<TD align="left" colspan="7"><bean:message key="address.preFix" bundle="carriers"/></TD>
					<TD align="left" colspan="3"><bean:message key="number" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="7"><input type="text" name="ldAddressStreet" class="input-field" value="<bean:write name="carrierLD" property="addressStreet"/>" size="64" maxlength="128" tabindex="16"/></TD>
					<TD colspan="3"><input type="text" name="ldAddressNumber" class="input-field" value="<bean:write name="carrierLD" property="addressNumber"/>" size="12" maxlength="32" tabindex="17"/></TD>
				</TR>
				<TR class="table-column-title">
					<TD align="left" colspan="4"><bean:message key="complement" bundle="carriers"/></TD>
					<TD align="left" colspan="2"><bean:message key="zipCode" bundle="carriers"/></TD>
					<TD align="left" colspan="4"><bean:message key="city" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="4"><input type="text" name="ldAddressComplement" class="input-field" value="<bean:write name="carrierLD" property="addressComplement"/>" size="28" maxlength="32" tabindex="18"/></TD>
					<TD colspan="2"><input type="text" name="ldAddressZip" class="input-field" value="<bean:write name="carrierLD" property="addressZip"/>" size="9" maxlength="9" tabindex="19"/></TD>
					<TD colspan="4"><input type="text" name="ldAddressCity" class="input-field" value="<bean:write name="carrierLD" property="addressCity"/>" size="28" maxlength="32" tabindex="20"/></TD>
				</TR>
				<TR class="table-column-title">
					<TD align="left" colspan="4"><bean:message key="taxId" bundle="carriers"/></TD>
					<TD align="left" colspan="3"><bean:message key="stateEnrollNumber" bundle="carriers"/></TD>
					<TD align="left" colspan="3"><bean:message key="cityEnrollNumber" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="4"><input type="text" name="ldTaxId" class="input-field" value="<bean:write name="carrierLD" property="taxId"/>" size="24" maxlength="24" tabindex="21"/></TD>
					<TD colspan="3"><input type="text" name="ldStateEnrollNumber" class="input-field" value="<bean:write name="carrierLD" property="stateEnrollNumber"/>" size="24" maxlength="24" tabindex="22"/></TD>
					<TD colspan="3"><input type="text" name="ldCityEnrollNumber" class="input-field" value="<bean:write name="carrierLD" property="cityEnrollNumber"/>" size="24" maxlength="24" tabindex="23"/></TD>
				</TR>
				<TR class="table-column-title">
					<TD align="left" colspan="5"><bean:message key="website" bundle="carriers"/></TD>
					<TD align="left" colspan="5"><bean:message key="address.preFix" bundle="carriers"/> <bean:message key="email.suffix" bundle="carriers"/></TD>
				</TR>
				<TR class="text8" align="left">
					<TD colspan="5"><input type="text" name="ldAddressWeb" class="input-field" value="<bean:write name="carrierLD" property="addressWeb"/>" size="50" maxlength="64" tabindex="24"/></TD>
					<TD colspan="5"><input type="text" name="ldAddressEmail" class="input-field" value="<bean:write name="carrierLD" property="addressEmail"/>" size="50" maxlength="64" tabindex="25"/></TD>
				</TR>
				<TR class="text8" height="30">
					<TD colspan="10">&nbsp;</TD>
				</TR>

				<TR class="text8" height="30">
					<TD colspan="7">&nbsp;</TD>
					<TD colspan="3" align="right">
						<A href="<html:rewrite page="/carrier.do"/>"><bean:message key="cancel" bundle="carriers"/></A>&nbsp;&nbsp;
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<A href="javascript:onClick=validate()"><bean:message key="submit" bundle="carriers"/></A>&nbsp;&nbsp;&nbsp;&nbsp;
						<% } %>
					</TD>
				</TR>
				</TABLE>

			<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="<bean:write name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>"/>
			<html:hidden name="carrier" property="uid"/>
		</html:form>
		</TD></TR></TABLE>
	</TD>

<script language="Javascript">

<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
	document.forms['carrierDataForm'].fullName.disabled=true;
	document.forms['carrierDataForm'].taxId.disabled=true;
	document.forms['carrierDataForm'].stateEnrollNumber.disabled=true;
	document.forms['carrierDataForm'].cityEnrollNumber.disabled=true;
	document.forms['carrierDataForm'].addressStreet.disabled=true;
	document.forms['carrierDataForm'].addressNumber.disabled=true;
	document.forms['carrierDataForm'].addressComplement.disabled=true;
	document.forms['carrierDataForm'].addressZip.disabled=true;
	document.forms['carrierDataForm'].addressCity.disabled=true;
	document.forms['carrierDataForm'].addressWeb.disabled=true;
	document.forms['carrierDataForm'].addressEmail.disabled=true;
	document.forms['carrierDataForm'].ldFullName.disabled=true;
	document.forms['carrierDataForm'].ldTaxId.disabled=true;
	document.forms['carrierDataForm'].ldStateEnrollNumber.disabled=true;
	document.forms['carrierDataForm'].ldCityEnrollNumber.disabled=true;
	document.forms['carrierDataForm'].ldAddressStreet.disabled=true;
	document.forms['carrierDataForm'].ldAddressNumber.disabled=true;
	document.forms['carrierDataForm'].ldAddressComplement.disabled=true;
	document.forms['carrierDataForm'].ldAddressZip.disabled=true;
	document.forms['carrierDataForm'].ldAddressCity.disabled=true;
	document.forms['carrierDataForm'].ldAddressWeb.disabled=true;
	document.forms['carrierDataForm'].ldAddressEmail.disabled=true;

<% } %>

</script>