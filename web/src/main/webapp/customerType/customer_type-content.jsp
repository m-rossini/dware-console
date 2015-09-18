<jsp:directive.page import="br.com.auster.dware.console.commons.RequestScopeConstants"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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
		<SCRIPT language="Javascript">
			function validate() {
				if (validateCustomerTypeForm(document.customerTypeForm)) {
					document.customerTypeForm.submit();
				}
			}
		</SCRIPT>
		<!-- Begin Validator Javascript Function-->
		<html:javascript formName="customerTypeForm"/>
		<!-- End of Validator Javascript Function-->
		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td height="15">
          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
              	<tr>
		<!-- if exists customer type, then list them -->
		<html:form action="/customer.do" method="post" onsubmit="return validateCustomerTypeForm(this);">
			<bean:define id="customer" name="<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>" scope="request"/>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
					<TD height="100%" class="table-title"><bean:message key="title.preFix.inclusion" bundle="customerType"/> <bean:message key="title.suffix.ofCustomerType" bundle="customerType"/></TD>
</logic:equal><logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
					<TD height="100%" class="table-title"><html:hidden name="customer" property="uid"/><bean:message key="title.preFix.alteration" bundle="customerType"/> <bean:message key="title.suffix.ofCustomerType" bundle="customerType"/></TD>
</logic:notEqual>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>
				<TR valign="top">
					<TD widht="100%">
					<table width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
					<TR height="20">
					<TD class="table-column-title" align="center"><bean:message key="code" bundle="customerType"/></TD>
					<TD class="text8" align="left">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:text name="customer" property="customerType" size="3" maxlength="1" styleClass="uppercase-mandatory-input-field"/>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:text readonly="true" name="customer" property="customerType" size="3" styleClass="input-field"/>
</logic:notEqual>
					</TD>
					<TD class="table-column-title" align="center"><bean:message key="description" bundle="customerType"/></TD>
					<TD class="text8" align="left">
						<html:text name="customer" property="typeDescription" size="50" maxlength="40" styleClass="input-field"/>
					</TD>
				</TR>
				<TR class="text8" height="30">
					<TD colspan="7" align="right">
						<A href="<html:rewrite page="/customer.do"/>"><bean:message key="cancel" bundle="customerType"/></A>&nbsp;&nbsp;
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<A href="javascript:onClick=validate()"><bean:message key="submit" bundle="customerType"/></A>&nbsp;&nbsp;
						<% } %>
					</TD>
				</TR>
			</TABLE>
			<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="<bean:write name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>"/>
		</html:form>
		</TD></TR></TABLE>
	</TD>

<script language="Javascript">

<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
	document.forms['customerTypeForm'].customerType.disabled=true;
	document.forms['customerTypeForm'].typeDescription.disabled=true;
<% } %>

</script>