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

	<TD>
		<bean:define id="invoice" name="<%=RequestScopeConstants.REQUEST_INVOICE_ID_KEY%>" scope="request"/>
		<TABLE width="100%">
			<TR>
				<TD widht="100%">
					<TABLE class="table" height="100%" width="100%">
						<TR class="table-title">
							<TD colspan="4"><bean:message key="title.confirmation.operation" bundle="invoice"/></TD>
						</TR>
						<TR height="60">
							<TD width="10%"/>
							<TD colspan="2" class="text8" align="left">
								<bean:message key="message.result.preFix" bundle="invoice"/>
<logic:empty name="invoice" property="customerType">
								<bean:message key="option.null.value" bundle="invoice"/>
</logic:empty><logic:notEmpty name="invoice" property="customerType">
								<bean:write name="invoice" property="customerType.typeDescription"/>
</logic:notEmpty>
								/
<logic:empty name="invoice" property="UF">
								<bean:message key="option.null.value" bundle="invoice"/>
</logic:empty><logic:notEmpty name="invoice" property="UF">
								<bean:write name="invoice" property="UF.state"/>
</logic:notEmpty>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
								<bean:message key="message.operation.enclosed" bundle="invoice"/>
</logic:equal>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
								<bean:message key="message.operation.modified" bundle="invoice"/>
</logic:equal>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>">
								<bean:message key="message.operation.excluded" bundle="invoice"/>
</logic:equal>
								<bean:message key="message.result.suffix" bundle="invoice"/>
								<BR/>
								<A href="<html:rewrite page="/invoice.do"/>"><bean:message key="clickHere.return" bundle="invoice"/></A>
							</TD>
							<TD width="10%"/>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</TD>