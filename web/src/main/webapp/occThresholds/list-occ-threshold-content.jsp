<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>
<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>
<%@ page import="br.com.auster.billcheckout.thresholds.BaseThreshold"%>

			
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
<td>

<script language="Javascript">


	function submitForm(objId, type) {
		document.forms['occForm'].elements['<%=RequestScopeConstants.REQUEST_OCCTHRESHOLDS_ID_KEY%>'].value = objId;
		document.forms['occForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;
		

		if (type == '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>') {
			if (!confirm('<bean:message key="message.sureToErase" bundle="thresholds"/>')) {
				return;
			};
		}
		document.forms['occForm'].submit();
	}

</script>

<bean:define id="occthreshold" name="<%=RequestScopeConstants.REQUEST_LISTOFOCCTHRESHOLDS_KEY%>" scope="request"/>
		<TABLE height="100%" width="100%">
			<TR>
				<TD widht="100%" valign="top">
					<!-- if exists carriers, then list them -->
					<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
					<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>
					<TABLE class="table" width="100%">
				        <tr class="table-title">
				          <td colspan="7"><bean:message key="text.occthreshold.title" bundle="thresholds"/></td>
				        </tr>
				        <tr class="table-column-title">
				          <td align="center" width="5%"><bean:message key="text.occthreshold.uid" bundle="thresholds"/></td>
				          <td align="center" width="8%"><bean:message key="text.occthreshold.carrierCode" bundle="thresholds"/></td>
				          <td align="center" width="8%"><bean:message key="text.occthreshold.carrierState" bundle="thresholds"/></td>
				          <td align="center" width="39%"><bean:message key="text.occthreshold.carrierName" bundle="thresholds"/></td>
				          <td align="center" width="15%"><bean:message key="text.occthreshold.upperLimit" bundle="thresholds"/></td>
				          <td align="center" width="15%"><bean:message key="text.occthreshold.lowerLimit" bundle="thresholds"/></td>
				          <td align="center" width="10%"><bean:message key="text.occthreshold.remove" bundle="thresholds"/></td>
				        </tr>
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
								<FORM name="sortingForm" action="<html:rewrite page="/occthresholds.do"/>" method="post">
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
								</FORM>
							</TD>
							<TD align="center">
								<FORM name="occForm" action="<html:rewrite page="/occthresholds.do"/>" method="post" id="occForm">
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OCCTHRESHOLDS_ID_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_LISTOFOCCTHRESHOLDS_KEY%>"/>
									<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>
									
								</FORM>
							</TD>
						</TR>
						<!--                                          -->
						<!-- for each carrier found in bck_carrier_dm -->
						<!--                                          -->
<logic:notEmpty name="occthreshold">						
	<logic:iterate name="occthreshold" id="occ">
							<TR class="text8">
								<TD align="center"><A href="javascript:submitForm('<bean:write name="occ" property="uid"/>', 'detail')"><bean:write name="occ" property="uid"/></A></TD>
								<TD align="center"><bean:write name="occ" property="carrier.carrierCode"/></TD>
								<TD align="center"><bean:write name="occ" property="carrier.carrierState"/></TD>
								<TD align="center"><bean:write name="occ" property="carrier.carrierCompany"/></TD>
								<logic:lessThan name="occ" property='upperAmount' value="0.00">
									<TD align="center"> R$ - </TD>
								</logic:lessThan>
								<logic:greaterEqual name="occ" property='upperAmount' value="0.00">
									<TD align="center"> R$ <bean:write name="occ" property="upperAmount"  format='###,###,###.##'/></TD>
								</logic:greaterEqual>
								
								<logic:lessThan name="occ" property='lowerAmount' value="0.00">
									<TD align="center"> R$ - </TD>
								</logic:lessThan>
								<logic:greaterEqual name="occ" property='lowerAmount' value="0.00">
									<TD align="center"> R$ <bean:write name="occ" property="lowerAmount"  format='###,###,###.##'/></TD>
								</logic:greaterEqual>
								<TD align="center"><A href="javascript:submitForm('<bean:write name="occ" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>')"><IMG src="images/no.jpg" border="0"/></A></TD>
							</TR>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="occthreshold">
	<TR class="text8">
		<TD align="center" colspan="7"><bean:message key="message.operation.nonefound" bundle="thresholds"/></TD>
	</TR>
</logic:empty>

					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD widht="100%"/>
			</TR>
			<TR class="text8" align="center">
				<TD widht="100%">
					<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request" type="java.lang.String"/>
					<bean:define id="totalPages" name="<%=RequestScopeConstants.REQUEST_TOTALPAGES_KEY%>" scope="request" type="java.lang.String"/>
					<bean:define id="displayLength" name="<%=RequestScopeConstants.REQUEST_DISPLAYLEN_KEY%>" scope="request" type="java.lang.String"/>
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
					<FORM name="moveToPage" action="<html:rewrite page="/occthresholds.do"/>" method="post">
						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>

						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
						<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_DISPLAYLEN_KEY%>" value="<%=displayLength%>"/>
					</FORM>
				</TD>
			</TR>
			<TR class="text8" align="right">
				<TD colspan="10"><A href="javascript:submitForm(null, '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')"><bean:message key="text.occthreshold.add" bundle="thresholds"/></A></TD>
			</TR>
		</TABLE>
	</TD>