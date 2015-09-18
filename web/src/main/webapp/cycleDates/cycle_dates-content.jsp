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


<script language="Javascript">
	document.write(getCalendarStyles());
	var calendarWindow = new CalendarPopup("calendarDiv");
	calendarWindow.showNavigationDropdowns();

	function validateFields() {

	  	if (document.cycleDatesForm.cycleCode.value=='') {
	  		  window.alert('<bean:message key="cycleCode" bundle="cycleDates"/> <bean:message key="isMandatory" bundle="cycleDates"/>');
	  		  return false;
	  	}
	  	if (document.cycleDatesForm.endDate.value=='') {
	  		  window.alert('<bean:message key="cutDate" bundle="cycleDates"/> <bean:message key="isMandatory" bundle="cycleDates"/>');
	  		  return false;
	  	}
	  	if ((document.cycleDatesForm.dueDate.value=='') && (document.cycleDatesForm.offsetDays.value=='')) {
	  		  window.alert('<bean:message key="dueDate" bundle="cycleDates"/> <bean:message key="isMandatory" bundle="cycleDates"/>');
	  		  return false;
	  	}

		// checking id startDate < cutDate
		if (isAfter(document.cycleDatesForm.startDate.value, document.cycleDatesForm.endDate.value)) {
			window.alert('<bean:message key="cutDate" bundle="cycleDates"/> <bean:message key="message.check.middle" bundle="cycleDates"/> <bean:message key="startDate" bundle="cycleDates"/>');
			return false;
		}
		// checking id cutDate < issueDate
		if (isAfter(document.cycleDatesForm.endDate.value, document.cycleDatesForm.issueDate.value)) {
			window.alert('<bean:message key="issueDate" bundle="cycleDates"/> <bean:message key="message.check.middle" bundle="cycleDates"/> <bean:message key="cutDate" bundle="cycleDates"/>');
			return false;
		}
		// checking id cutDate < refData
		if (isAfter(document.cycleDatesForm.endDate.value, document.cycleDatesForm.referenceDate.value)) {
			window.alert('<bean:message key="referenceDate" bundle="cycleDates"/> <bean:message key="message.check.middle" bundle="cycleDates"/> <bean:message key="cutDate" bundle="cycleDates"/>');
			return false;
		}
		// checking id cutDate < refData
		if (isAfter(document.cycleDatesForm.referenceDate.value, document.cycleDatesForm.dueDate.value)) {
			window.alert('<bean:message key="dueDate" bundle="cycleDates"/> <bean:message key="message.check.middle" bundle="cycleDates"/> <bean:message key="referenceDate" bundle="cycleDates"/>');
			return false;
		}
		return true;
	}

	function isAfter(dt1, dt2) {
		if ((dt1 == '') || (dt2 == '')) {
			return false;
		}
		dt1AsDate = new Date (dt1.substr(6,4), dt1.substr(3,2), dt1.substr(0,2));
		dt2AsDate = new Date (dt2.substr(6,4), dt2.substr(3,2), dt2.substr(0,2));
		return (dt1AsDate.getTime() > dt2AsDate.getTime());
	}

	function validate() {
		if (validateFields()) {
			// re-enabling for UPDATEs so that the UID gets to the servlet
			document.cycleDatesForm.customerType.disabled=false
			document.cycleDatesForm.submit();
		}
	}

	function resetFields(operation) {
		// Feito, pois o mï¿½todo Reset() nï¿½o limpa os campos. Ele somente volta aos valores originais.
		// Update
		document.cycleDatesForm.issueDate.value     = "";
		document.cycleDatesForm.referenceDate.value = "";
		if (operation == '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>') {
			document.cycleDatesForm.cycleCode.value     = "";
			document.cycleDatesForm.endDate.value       = "";
			document.cycleDatesForm.dueDate.value       = "";
			document.cycleDatesForm.offsetDays.value    = "";
		}
	}
</script>

	<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>

	<td widht="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<DIV id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;"></DIV>
		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
		<TD height="15">
		<!-- if exists cycles, then list them -->
		<html:form action="/cycles.do" method="post">
			<bean:define id="cycle" name="<%=RequestScopeConstants.REQUEST_CYCLEDATES_ID_KEY%>" scope="request"/>
			<bean:define id="customerTypeList" name="<%=RequestScopeConstants.REQUEST_LISTOFCUSTOMERTYPE_KEY%>" scope="request"/>
			<TABLE class="table" height="100%" width="100%" cellpadding="0" cellspacing="0">
				<TR class="table-title">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
					<TD height="100%" class="table-title"><bean:message key="title.preFix.inclusion" bundle="cycleDates"/> <bean:message key="title.suffix.datesOfCycles" bundle="cycleDates"/></TD>
</logic:equal><logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
					<TD height="100%" class="table-title"><html:hidden name="cycle" property="uid"/><bean:message key="title.preFix.alteration" bundle="cycleDates"/> <bean:message key="title.suffix.datesOfCycles" bundle="cycleDates"/></TD>
</logic:notEqual>
				</TR>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>
			<TR valign="top">
				<TD widht="100%">
					<table width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
				<TR class="text8">
					<TD class="table-column-title" align="center" width="12%"><bean:message key="cycleCode" bundle="cycleDates"/></TD>
					<TD class="text8" align="left" width="18%">
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:text name="cycle" property="cycleCode" size="15" maxlength="8" styleClass="mandatory-input-field"/>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:text name="cycle" property="cycleCode" size="15" readonly="true" maxlength="8" styleClass="mandatory-input-field"/>
</logic:notEqual>
					</TD>
					<TD class="table-column-title" align="center" width="12%"><bean:message key="clientType" bundle="cycleDates"/></TD>
					<TD class="text8" align="left" colspan="4">
						<%String customerTypeUid = (String) request.getAttribute(RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY);%>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:select name="cycle" property="customerType" value="<%=customerTypeUid%>" styleClass="mandatory-input-field">
							<html:options collection="customerTypeList" property="uid" labelProperty="typeDescription" name="<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>"/>
						</html:select>

						<!-- adding ANY option -->
						<script language="javascript">
							optionList = document.cycleDatesForm.customerType.options;
							len = optionList.length;
							optionList[len] = new Option("<bean:message key="custtype.all" bundle="cycleDates"/>", "");
							optionList[len].selected=true;
						</script>
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<html:select name="cycle" property="customerType" value="<%=customerTypeUid%>" styleClass="mandatory-input-field">
							<html:options collection="customerTypeList" property="uid" labelProperty="typeDescription" name="<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>"/>
						</html:select>
						<!-- due to problems with disabled attribute in html:select -->
						<script language="javascript">
							optionList = document.cycleDatesForm.customerType;
							optionList.disabled=true;
							// setting customer type to correct value
							<logic:notPresent name="<%=RequestScopeConstants.REQUEST_CUSTOMERTYPE_ID_KEY%>">
								optionList = document.cycleDatesForm.customerType.options;
								len = optionList.length;
								optionList[len] = new Option("<bean:message key="custtype.all" bundle="cycleDates"/>", "");
								optionList[len].selected=true;
							</logic:notPresent>
						</script>
</logic:notEqual>

					</TD>
				</TR>
				<TR class="text8">
					<TD class="table-column-title" align="center" width="12%"><bean:message key="startDate" bundle="cycleDates"/></TD>
					<TD class="text8" align="left" width="18%">
						<INPUT name="startDate" readonly="readonly" size="15" maxlength="10" class="readonly-input-field" value="<bean:write name="cycle" property="startDate" format="dd/MM/yyyy"/>"/>
					</TD>
					<TD class="table-column-title" align="center" width="12%"><bean:message key="cutDate" bundle="cycleDates"/></TD>
					<TD class="text8" align="left" width="18%">
						<INPUT name="endDate" readonly="readonly" size="15" maxlength="10" class="mandatory-input-field" value="<bean:write name="cycle" property="endDate" format="dd/MM/yyyy"/>"/>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<IMG src="images/calendar.gif" id="cutDateAnchor" onClick="javascript:calendarWindow.select(document.cycleDatesForm.endDate,'cutDateAnchor','dd/MM/yyyy')"/>
</logic:equal>
					</TD>
					<TD class="table-column-title" align="center" width="12%"><bean:message key="issueDate" bundle="cycleDates"/></TD>
					<TD class="text8" align="left" width="18%">
						<INPUT name="issueDate" readonly="readonly" size="15" maxlength="10" class="input-field" value="<bean:write name="cycle" property="issueDate" format="dd/MM/yyyy"/>"/>
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<IMG src="images/calendar.gif" id="issueDateAnchor" onClick="javascript:calendarWindow.select(document.cycleDatesForm.issueDate,'issueDateAnchor','dd/MM/yyyy')"/>
						<% } %>
					</TD>
					<TD width="1%"/>
				</TR>
				<TR>
					<TD class="table-column-title" align="center"><bean:message key="referenceDate" bundle="cycleDates"/></TD>
					<TD class="text8" align="left">
						<INPUT name="referenceDate" readonly="readonly" size="15" maxlength="10" class="input-field" value="<bean:write name="cycle" property="referenceDate" format="dd/MM/yyyy"/>"/>
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<IMG src="images/calendar.gif" id="refDateAnchor" onClick="javascript:calendarWindow.select(document.cycleDatesForm.referenceDate,'refDateAnchor','dd/MM/yyyy')"/>
						<% } %>
					</TD>
					<TD class="table-column-title" align="center"><bean:message key="dueDate" bundle="cycleDates"/></TD>
					<TD class="text8" align="left" colspan="3">
						<INPUT name="dueDate" readonly="readonly" size="15" maxlength="10" class="mandatory-input-field"value="<bean:write name="cycle" property="dueDate" format="dd/MM/yyyy"/>"/>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<IMG src="images/calendar.gif" id="dueDateAnchor" onClick="javascript:calendarWindow.select(document.cycleDatesForm.dueDate,'dueDateAnchor','dd/MM/yyyy')"/>
						&nbsp;&nbsp;&nbsp;&nbsp; / &nbsp;&nbsp;&nbsp;&nbsp;
						<INPUT type="text" name="offsetDays" size="5" maxlength="2" class="input-field"/>
						<span class="tooltip"> (<bean:message key="offset.days" bundle="cycleDates"/>) <span><bean:message key="message.help.offset.days" bundle="cycleDates"/></span> </span>
</logic:equal>
					</TD>
				</TR>
				<TR class="text8" height="30">
					<TD colspan="7" align="right">
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<A href="javascript:onClick=resetFields('<bean:write name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>')"><bean:message key="reset" bundle="cycleDates"/></A>&nbsp;&nbsp;
						<% } %>
						<A href="<html:rewrite page="/cycles.do"/>"><bean:message key="cancel" bundle="cycleDates"/></A>&nbsp;&nbsp;
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<A href="javascript:onClick=validate()"><bean:message key="submit" bundle="cycleDates"/></A>&nbsp;&nbsp;&nbsp;&nbsp;
						<% } %>
					</TD>
				</TR>
			</TABLE>
			<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="<bean:write name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>"/>
		</html:form>
		</TD></TR></TABLE>
	</TD>