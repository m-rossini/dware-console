<jsp:directive.page import="br.com.auster.dware.console.commons.RequestScopeConstants"/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
	<TD>
		<bean:define id="cycle" name="<%=RequestScopeConstants.REQUEST_INVOICE_ID_KEY%>" scope="request"/>
		<TABLE width="100%">
			<TR>
				<TD widht="100%">
					<!-- if exists cycleDates, then list them -->
					<TABLE class="table" height="100%" width="100%">
						<TR class="table-title">
							<TD colspan="4"><bean:message key="title.joined.error" bundle="invoice"/></TD>
						</TR>
						<TR height="60">
							<TD width="10%"/>
							<TD colspan="2" class="text8" align="left">
								<bean:message key="error.operation" bundle="invoice"/>
								<BR/><BR/><BR/>

								<logic:messagesPresent message="true">
									<html:messages id="message" message="true" bundle="exception">
										<SPAN><c:out value="${message}"/></SPAN><BR/>
									</html:messages>
								</logic:messagesPresent>

								<STRONG><bean:message key="action" bundle="invoice"/>:</STRONG> inclusão de limite<BR/>
								<STRONG><bean:message key="reason" bundle="invoice"/>:</STRONG> erro conectando com o banco de dados<BR/>
								<STRONG><bean:message key="affected.field" bundle="invoice"/>:</STRONG> ccc
								<BR/><BR/><BR/>
								<A href="<html:rewrite page="/invoice.do"/>"><bean:message key="clickHere.return" bundle="invoice"/></A>
							</TD>
							<TD width="10%"/>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</TD>