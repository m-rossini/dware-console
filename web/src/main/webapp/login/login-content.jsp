<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<script language="Javascript">

	function submitForm() {
		if (document.forms[0].login.value=='') {
			window.alert('<bean:message key="text.login.required" bundle="login"/>');
		} else if (document.forms[0].password.value=='') {
			window.alert('<bean:message key="text.password.required" bundle="login"/>');
		} else {
			document.forms[0].submit();
		}
	}
	
	function openPasswordWindow() {
		window.open("<html:rewrite page="/login/changePassword-popup.jsp"/>", "passwordPopup", "scrollbars,resizable,width=350,height=300");
		return;
	}

</script>

          	<td height="260" width="251" rowspan="3">
          		<table class="table" cellpadding="0" cellspacing="0" height="100%">
              	<tr>
                	<td height="45">
						<table height="100%" width="100%" cellpadding="2" cellspacing="2">
                    	<tr>
                      		<td height="16" class="login-title">
                  				<div align="left">&nbsp;
                  				<html:img page="/images/in.gif" width="12" height="10"/>&nbsp;&nbsp;
                  				<html:img page="/images/in.gif" width="12" height="10"/>&nbsp;&nbsp;
                  				<bean:message key="text.loginbox" bundle="login"/></div>
                  			</td>
                    	</tr>
            			</table>
            		</td>
              	</tr>
	          	<tr valign="middle">
                		<td class="login-central-box" background="<html:rewrite page="/images/login_background_center.jpg"/>" height="255"  width="268">
                		<p align="center">WELCOME</p>
            	      	<p>&nbsp; &nbsp;&nbsp;&nbsp;BIENVENUE</p>
        	          	<p align="right">BENVENUTO&nbsp;</p>
    	              	<p align="center">WILLKOMMEN&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
	                  	<p align="center">BEM-VINDO</p>
                  		<p align="left">&nbsp;BIENVENIDO</p><p><br></p>
                  	</td>
              	</tr>
	          	<tr>

<!--                                              -->
<!--        Form definition for login page        -->
<!--                                              -->
<html:form action="/login" focus="login" method="post">

                	<td height="100">
	                  	<table width="100%" height="100%" cellpadding="0" cellspacing="0">

					<!--     login text field     -->
			          	<tr  valign="bottom">
        	              <td width="25%" class="input-label"><bean:message key="text.login" bundle="login"/>&nbsp;</td>
            	          <td width="44%">
                	         <html:text property="login" maxlength="150" styleClass="input-field"/>
                  			</td>
	                  	</tr>

                    <!-- presenting errors for login -->
			            <tr  valign="bottom" class="text8bRed">
			            	<td colspan="2">
			            	<center><html:errors bundle="login" property="login"/></center>
            	            </td>
                	    </tr>

					<!--    password text field   -->
			            <tr valign="middle">
	                      	<td class="input-label"><bean:message key="text.password" bundle="login"/>&nbsp;</td>
	                      	<td nowrap>
	                      		<html:password property="password" maxlength="40" styleClass="input-field" redisplay="false"/>
		                      	&nbsp;<a href="javascript:submitForm();"><html:img page="/images/login_button.jpg" alt="Login" width="29" height="29" border="0" align="absmiddle"/></a>&nbsp;
	                      	</td>
						</tr>

                    <!-- presenting errors for password -->
			            <tr  valign="bottom" class="text8bRed">
			            	<td colspan="2">
			            	<center><html:errors bundle="login" property="password"/></center>
	                        </td>
	                    </tr>

                    <!-- change password link -->
				        <tr valign="middle" class="input-label"	>
	                      	<td colspan="2">	
 	                      		<center><a href="javascript:openPasswordWindow();"><bean:message key="text.changepassword" bundle="login"/></a></center>
	                      	</td>
	                    </tr>
<!--                                              -->
<!--    End of form definition for login page     -->
<!--                                              -->
</html:form>

	            		</table>
	            	</td>
				</tr>
				</table>
			</td>