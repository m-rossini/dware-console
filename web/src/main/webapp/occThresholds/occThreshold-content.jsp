<jsp:directive.page import="br.com.auster.dware.console.commons.RequestScopeConstants" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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
<script language="Javascript">

function submitForm(objId, type) {
	document.forms['occForm'].elements['<%=RequestScopeConstants.REQUEST_OCCTHRESHOLDS_ID_KEY%>'].value = objId;
	document.forms['occForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;	
	
	if (validateAmounts(document.forms['occForm'].upperAmount, 
				        document.forms['occForm'].lowerAmount, 
					    '<bean:message key="text.occthreshold.invalidUpperLowerDiff" bundle="thresholds"/>')){

	document.forms['occForm'].submit();
					    
	}
}
</script>


<bean:define id="occThreshold" name="<%=RequestScopeConstants.REQUEST_OCCTHRESHOLDS_ID_KEY%>" scope="request"/>
<bean:define id="carrier" name="<%=RequestScopeConstants.REQUEST_CARRIER_ID_KEY%>" scope="request"/>
<bean:define id="carrierList" name="<%=RequestScopeConstants.REQUEST_LISTOFCARRIERS_KEY%>" scope="request"/>

<table height="100%" width="100%">
  <tr>
  <td widht="100%">
  
			<FORM name="occForm" action="occthresholds.do" method="post" id="occForm">
			<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>"/>
			<INPUT type="hidden" name="<%=RequestScopeConstants.REQUEST_OCCTHRESHOLDS_ID_KEY%>" />
				<TABLE class="table" height="100%" width="100%">
					<tr>
					<td widht="100%">
					<table class="table" height="100%" width="100%">
					<tbody>
						<tr class="table-title">
							<td colspan="6"><bean:message key="text.occthreshold.titleInclude" bundle="thresholds"/></td>
						</tr>
						<tr>
						<td class="table-column-title" align="center" width="12%"><bean:message key="text.occthreshold.carrierName" bundle="thresholds"/></td>
						<td class="text8" align="left" width="24%">
						
						<%String carrierUid = (String) request.getAttribute(RequestScopeConstants.REQUEST_CARRIER_ID_KEY);%> 
						<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
							<html:select name="carrier" property="carrier" value="<%=carrierUid%>" styleClass="mandatory-input-field">
								<html:options collection="carrierList" property="uid" labelProperty="customDescription" name="<%=RequestScopeConstants.REQUEST_CARRIER_ID_KEY%>"/>
							</html:select>
						</logic:equal>
						<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
							<html:select name="carrier" property="carrier" value="${carrier.carrierState}"  disabled="true"  styleClass="mandatory-input-field">
								<html:options collection="carrierList" property="uid" labelProperty="customDescription" name="<%=RequestScopeConstants.REQUEST_CARRIER_ID_KEY%>"/>
							</html:select>
							<html:hidden name="carrier" property="carrier" value="carrier.uid"/>
						</logic:notEqual>

						</td>
						<td class="table-column-title" align="center" width="12%"><bean:message key="text.occthreshold.upperLimit" bundle="thresholds"/></td>
						<td class="text8" align="left" width="20%">
						<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
							<input type="text" name="upperAmount" value="" property="upperAmount" class="input-field" size="10" maxlength="10" tabindex="2" />&nbsp;&nbsp;
						</logic:equal>
						<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
							<logic:greaterEqual name="occThreshold" property='upperAmount' value="0.00">
									<input type="text" name="upperAmount" value="<bean:write name="occThreshold" property="upperAmount"/>" property="upperAmount" class="input-field" size="10" maxlength="10" tabindex="2" />&nbsp;&nbsp;								
							</logic:greaterEqual>
							<logic:lessThan name="occThreshold" property='upperAmount' value="0.00">
								<input type="text" name="upperAmount" value="" property="upperAmount" class="input-field" size="10" maxlength="10" tabindex="2" />&nbsp;&nbsp;								
							</logic:lessThan>
							
						</logic:notEqual>
							<a href="javascript:clearField(document.forms[0].upperAmount)"><bean:message key="text.occthreshold.clear" bundle="thresholds"/></a>
							<script language="Javascript">
								document.forms['occForm'].upperAmount.style.textAlign = "right";
								document.forms['occForm'].upperAmount.onkeypress = displayFormattedMonetaryAmount;
								document.forms['occForm'].upperAmount.onkeydown	= storeKeyPressed;
					  		</script>
						</td>
						<td class="table-column-title" align="center" width="12%"><bean:message key="text.occthreshold.lowerLimit" bundle="thresholds"/></td>
						<td class="text8" align="left" width="20%">
						<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
							<input type="text" name="lowerAmount" value="" property="lowerAmount" class="input-field" size="10" maxlength="10" tabindex="3" checked="checked"/>&nbsp;&nbsp;
						</logic:equal>
						<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
							<logic:greaterEqual name="occThreshold" property='lowerAmount' value="0.00">
									<input type="text" name="lowerAmount" value="<bean:write name="occThreshold" property="lowerAmount"/>" property="lowerAmount" class="input-field" size="10" maxlength="10" tabindex="3" />&nbsp;&nbsp;
							</logic:greaterEqual>
							<logic:lessThan name="occThreshold" property='lowerAmount' value="0.00">
								<input type="text" name="lowerAmount" value="" property="lowerAmount" class="input-field" size="10" maxlength="10" tabindex="2" />&nbsp;&nbsp;								
							</logic:lessThan>							
						</logic:notEqual>
							<a href="javascript:clearField(document.forms[0].lowerAmount)"><bean:message key="text.occthreshold.clear" bundle="thresholds"/></a>
							<script language="Javascript">
								document.forms['occForm'].lowerAmount.style.textAlign = "right";
								document.forms['occForm'].lowerAmount.onkeypress = displayFormattedMonetaryAmount;
								document.forms['occForm'].lowerAmount.onkeydown	= storeKeyPressed;
			  				</script>
						</td>
						</tr>
						<tr>
						<td class="table-column-title" align="center" width="12%"><bean:message key="text.occthreshold.message" bundle="thresholds"/></td>
						<td class="text8" align="left" colspan="5">
							<input name="message" type="text" value="<bean:write name="occThreshold" property="hintMessage"/>" class="input-field" tabindex="4" size="96" maxlength="128" />
						</td>
						</tr>
						<tr class="text8" height="30">
						<td colspan="6" align="right">
							<A href="<html:rewrite page="/occthresholds.do"/>"> <bean:message key="text.occthreshold.cancel" bundle="thresholds"/></a>&nbsp;&nbsp;
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<a href="javascript:submitForm(null, '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>')"> <bean:message key="text.occthreshold.confirm" bundle="thresholds"/>&nbsp;&nbsp;
</logic:equal>
<logic:notEqual name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
						<a href="javascript:submitForm('<bean:write name="occThreshold" property="uid"/>', 'update')"> <bean:message key="text.occthreshold.confirm" bundle="thresholds"/>&nbsp;&nbsp;
</logic:notEqual>
							
						</td>
						</tr>
					</tbody>
				</table>
			</form>
		</td>
	</tr>
</table>