<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>


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

function checkEmail(myForm) {
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(myForm.emailAddress.value)){
		return true;
	}
	return false;
}

function submitForm() {

   if (document.forms[0].sendEmail[0].checked) {
      if (document.forms[0].emailAddress.value.length <= 1) {
         window.alert("<bean:message key="text.selectEmailAddress" bundle="requests"/>");
      	 return;
      }
      if (! checkEmail(document.forms[0])) {
      	window.alert("<bean:message key="text.email.malformed" bundle="requests"/>");
      	return;
      }
   }
   document.forms[0].submit();
}

function enableEmailField() {
	document.forms[0].emailAddress.disabled=false;
}

function disableEmailField() {
	document.forms[0].emailAddress.disabled=true;
}

</script>

<bean:define id="userInfo" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"/>


	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%"cellpadding="0" cellspacing="0">
	 	<tr valign="top">
          	<td height="15">
          		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
              	<tr class="text8b">
                	<td height="100%" class="table-title"><bean:message key="text.uploadTableTitle" bundle="general"/></td>
              </tr>
            </table></td>
		</tr>
		 <tr valign="top">
          	<td height="2"></td>
        </tr>
		<tr valign="top">
			<td  height="100%" width="100%">

<!--                                              -->
<!-- Begin of select notification form            -->
<!--                                              -->
<html:form action="/select-notification" method="POST">


				<table width="100%" height="100%" align="center" class="table"  cellpadding="0" cellspacing="0">
			 	<tr>
		        	<td colspan="3" height="35">
		        		<table width="100%" align="right" cellpadding="1" cellspacing="1" valign="middle">
		              	<tr class="text8b" align="right">
		                	<td width="89%"></td>
		                	<td width="1%"><html:img page="/images/ico_step1.jpg"/></td>
		                	<td width="1%" nowrap><bean:message key="text.uploadTableTitle" bundle="requests"/>&nbsp;</td>
		                	<td width="1%"><html:img page="/images/out.gif"/> </td>
		                	<td width="1%"><html:img page="/images/ico_step2.jpg"/></td>
		                	<td width="1%" nowrap><bean:message key="text.selectAccountsTableTitle" bundle="requests"/>&nbsp;</td>
		                	<td width="1%"><html:img page="/images/out.gif"/> </td>
		                	<td width="1%"><html:img page="/images/ico_step3.jpg"/></td>
		                	<td width="1%" nowrap><bean:message key="text.selectOutputsTableTitle" bundle="requests"/>&nbsp;</td>
		                	<td width="1%"><html:img page="/images/out.gif"/> </td>
		                	<td width="1%"><html:img page="/images/ico_step4.jpg"/></div></td></td>
		                	<td width="1%" nowrap class="current-step"><bean:message key="text.selectEmailTableTitle" bundle="requests"/></font>&nbsp;</td>
		              	</tr>
		            	</table>
		            </td>
		        </tr>
		        <tr height="25">
                   <td colspan="3"></td>
                </tr>
                <tr height="20">
					<td width="5%"></td>
                   	<td width="90%" class="text8">
                   		<div align="left">
                   		<html:radio property="sendEmail" value="true" onclick="javascript:enableEmailField()"/>
                        <bean:message key="text.sendEmailAddress" bundle="requests"/>&nbsp;
                   		<!-- email address field  -->
                   		<bean:define id="currentAddress" name="userInfo" property="email" type="java.lang.String"/>
                   		<html:text property="emailAddress" size="50" maxlength="120" styleClass="input-field" value="<%=currentAddress%>"/>
                    	</div>
                   	</td>
				   	<td width="5%"></td>
				</tr>
                <tr height="15">
				   	<td width="5%"></td>
                   	<td width="90%" class="text8">
                   		<div align="left"><html:radio property="sendEmail" value="false" onclick="javascript:disableEmailField()"/> <bean:message key="text.dontSendEmailWhenFinished" bundle="requests"/></div></td>
				   	<td width="5%"></td>
				</tr>
			  <tr height="30">
				<td width="5%"></td>
                <td colspan="2" class="textBlue"><div align="right"><html:link href="javascript:submitForm()">:: <bean:message key="text.finish" bundle="requests"/> ::</html:link>&nbsp;&nbsp;&nbsp;&nbsp;</div></td>
              </tr>
		        <tr>
                   <td colspan="3"></td>
                </tr>
             	</table>

<script>
	// initialize radio button
	document.forms[0].sendEmail[0].checked=true
</script>

</html:form>

			</td>
		</tr>
   		</table>
   	</td>
