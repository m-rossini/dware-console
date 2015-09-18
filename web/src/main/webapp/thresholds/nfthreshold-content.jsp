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
<SCRIPT language="JAVASCRIPT" src="js/format.js"></SCRIPT>

</auster:checkLogon>


<!--  main TD -->
<td height="100%">

<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>

<bean:define id="view" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request"/>


<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
	<tr valign="top">
    	<td height="15">
    	<table class="table" width="100%" align="center" cellpadding="0" cellspacing="0" >
			<tr><td height="100%" class="table-title"><bean:message key="text.nfthreshold.title" bundle="thresholds"/></td></tr>
		</table>
        </td>
    </tr>
	<tr valign="top">
    	<td height="2"></td>
    </tr>

    <tr  valign="top">
		<td  height="100%" width="100%">

		<form name="updateNFThresholdsForm" id="updateNFThresholdsForm" action="<html:rewrite page="/update-nf-thresholds.do"/>" method="post">

        <!-- if exists carriers, then list them -->
        <table width="100%" height="100%" class="table" cellpadding="1" cellspacing="0">
		<tr height="100%"><td width="100%" valign="top">

		<table cellpadding="2" cellspacing="0">
        <tr height="20" class="table-column-title">
          <td align="center" width="15%"><bean:message key="text.nfthreshold.nftype" bundle="thresholds"/></td>
          <td align="center" width="20%"><bean:message key="text.upperLimit" bundle="thresholds"/></td>
          <td align="center" width="20%"><bean:message key="text.lowerLimit" bundle="thresholds"/></td>
          <td align="center" width="45%"><bean:message key="text.message" bundle="thresholds"/></td>
        </tr>

        <tr height="20" class="text8">
          <td align="center"><bean:message key="text.nfthreshold.nftype.local" bundle="thresholds"/>
		  	  <!-- empty since it was never added -->
		  	  <input type="hidden" name="idLocal" value=""/>
		  </td>
		  <td align="center">
		  	  <input name="localUpperAmount" class="input-field" tabindex="1" type="text" size="10" maxlength="10"/>
              &nbsp;&nbsp;<a href="javascript:clearField(document.forms['updateNFThresholdsForm'].localUpperAmount)"><bean:message key="text.clearField" bundle="thresholds"/></a>
			  <script language="Javascript">
						document.forms['updateNFThresholdsForm'].localUpperAmount.style.textAlign = "right";
						document.forms['updateNFThresholdsForm'].localUpperAmount.onkeypress = displayFormattedMonetaryAmount;
						document.forms['updateNFThresholdsForm'].localUpperAmount.onkeydown	= storeKeyPressed;
			  </script>
		  </td>
		  <td align="center">
		  	  <input name="localLowerAmount" class="input-field" tabindex="2" type="text" size="10" maxlength="10"/>
			  &nbsp;&nbsp;<a href="javascript:clearField(document.forms['updateNFThresholdsForm'].localLowerAmount)"><bean:message key="text.clearField" bundle="thresholds"/></a>
			  <script language="Javascript">
						document.forms['updateNFThresholdsForm'].localLowerAmount.style.textAlign = "right";
						document.forms['updateNFThresholdsForm'].localLowerAmount.onkeypress = displayFormattedMonetaryAmount;
						document.forms['updateNFThresholdsForm'].localLowerAmount.onkeydown	= storeKeyPressed;
			  </script>
	      </td>
		  <td align="center"><input class="input-field" name="localMessage" tabindex="3" type="text" size="64" maxlength="128"/></td>
		</tr>

        <tr height="20" class="text8">
          <td align="center"><bean:message key="text.nfthreshold.nftype.ld" bundle="thresholds"/>
		  	  <input type="hidden" name="idLD" value="2"/>
		  </td>
		  <td align="center">
		  	  <input name="ldUpperAmount" class="input-field" tabindex="4" type="text" size="10" maxlength="10"/>
			  &nbsp;&nbsp;<a href="javascript:clearField(document.forms['updateNFThresholdsForm'].ldUpperAmount)"><bean:message key="text.clearField" bundle="thresholds"/></a>
			  <script language="Javascript">
						document.forms['updateNFThresholdsForm'].ldUpperAmount.style.textAlign = "right";
						document.forms['updateNFThresholdsForm'].ldUpperAmount.onkeypress = displayFormattedMonetaryAmount;
						document.forms['updateNFThresholdsForm'].ldUpperAmount.onkeydown	= storeKeyPressed;
			  </script>
		  </td>
		  <td align="center">
		  	  <input name="ldLowerAmount" class="input-field" tabindex="5" type="text" size="10" maxlength="10"/>
			  &nbsp;&nbsp;<a href="javascript:clearField(document.forms['updateNFThresholdsForm'].ldLowerAmount)"><bean:message key="text.clearField" bundle="thresholds"/></a>
			  <script language="Javascript">
						document.forms['updateNFThresholdsForm'].ldLowerAmount.style.textAlign = "right";
						document.forms['updateNFThresholdsForm'].ldLowerAmount.onkeypress = displayFormattedMonetaryAmount;
						document.forms['updateNFThresholdsForm'].ldLowerAmount.onkeydown	= storeKeyPressed;
			  </script>
		  </td>
		  <td align="center"><input class="input-field" name="ldMessage" tabindex="6" type="text" size="64" maxlength="128"/></td>
		</tr>

		<script language="Javascript">
			   function validateAndSubmit() {
			       if (validateAmounts(document.forms['updateNFThresholdsForm'].localUpperAmount,
				                       document.forms['updateNFThresholdsForm'].localLowerAmount,
									   '<bean:message key="text.nfthreshold.invalidUpperLowerDiffForLocal" bundle="thresholds"/>') &&
					   validateAmounts(document.forms['updateNFThresholdsForm'].ldUpperAmount,
				                       document.forms['updateNFThresholdsForm'].ldLowerAmount,
									   '<bean:message key="text.nfthreshold.invalidUpperLowerDiffForLd" bundle="thresholds"/>')
					  ) {
				       document.forms['updateNFThresholdsForm'].submit();
				   }
			   }
		</script>
		</table>

		</td></tr>

		<tr height="20" class="text8" valign="bottom" align="right">
			<td colspan="4">
				<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
				<a href="javascript:validateAndSubmit()"><bean:message key="text.confirm" bundle="thresholds"/></a>&nbsp;&nbsp;&nbsp;&nbsp;
				<% } %>
			</td>
		</tr>
        </table>

		</form>
  		</td>
	</tr>

	<tr class="text8" height="40">
		<td colspan="10">&nbsp;</td>
	</tr>


</table>


<script language="Javascript">

<logic:present name="view">
	<logic:match name="view" property="localSet" value="true">
		<logic:match name="view" property="localLowerLimitSet" value="true">
			document.forms['updateNFThresholdsForm'].localLowerAmount.value='<bean:write name="view" property="localLowerLimit" format="###,###,##0.00"/>';
		</logic:match>
		<logic:match name="view" property="localUpperLimitSet" value="true">
			document.forms['updateNFThresholdsForm'].localUpperAmount.value='<bean:write name="view" property="localUpperLimit" format="###,###,##0.00"/>';
		</logic:match>
		document.forms['updateNFThresholdsForm'].localMessage.value='<bean:write name="view" property="localMessage"/>'
	</logic:match>
	<logic:match name="view" property="LDSet" value="true">
		<logic:match name="view" property="LDLowerLimitSet" value="true">
			document.forms['updateNFThresholdsForm'].ldLowerAmount.value='<bean:write name="view" property="LDLowerLimit" format="###,###,##0.00"/>';
		</logic:match>
		<logic:match name="view" property="LDUpperLimitSet" value="true">
			document.forms['updateNFThresholdsForm'].ldUpperAmount.value='<bean:write name="view" property="LDUpperLimit" format="###,###,##0.00"/>';
		</logic:match>
		document.forms['updateNFThresholdsForm'].ldMessage.value='<bean:write name="view" property="LDMessage"/>'
	</logic:match>
</logic:present>




<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
	document.forms['updateNFThresholdsForm'].localLowerAmount.disabled=true;
	document.forms['updateNFThresholdsForm'].localUpperAmount.disabled=true;
	document.forms['updateNFThresholdsForm'].localMessage.disabled=true;
	document.forms['updateNFThresholdsForm'].ldLowerAmount.disabled=true;
	document.forms['updateNFThresholdsForm'].ldUpperAmount.disabled=true;
	document.forms['updateNFThresholdsForm'].ldMessage.disabled=true;
<% } %>


</script>


<!--  closing main TD -->
</td>
