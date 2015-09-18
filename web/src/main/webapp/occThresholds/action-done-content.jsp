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
		<bean:define id="carrier" name="<%=RequestScopeConstants.REQUEST_OCCTHRESHOLDS_ID_KEY%>" scope="request"/>
		<TABLE width="100%">
			<TR>
				<TD widht="100%">
					<!-- if exists carriers, then list them -->
					<TABLE class="table" height="100%" width="100%">
						<TR class="table-title">
							<TD colspan="4"><bean:message key="title.confirmation.operation" bundle="thresholds"/></TD>
						</TR>
						<TR height="60">
							<TD width="10%"/>
							<TD colspan="2" class="text8" align="left">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
								<bean:message key="message.operation.modified" bundle="thresholds"/>
</logic:equal><logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>">
								<bean:message key="message.operation.excluded" bundle="thresholds"/>
</logic:equal><logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
								<bean:message key="message.operation.enclosed" bundle="thresholds"/>
</logic:equal>
								<BR/>
								<bean:message key="message.result.suffix" bundle="thresholds"/>
								<A href="<html:rewrite page="/occthresholds.do"/>"><bean:message key="clickHere.return" bundle="thresholds"/></A>
								<bean:message key="message.result.end" bundle="thresholds"/><BR/>
							</TD>
							<TD width="10%"/>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</TD>