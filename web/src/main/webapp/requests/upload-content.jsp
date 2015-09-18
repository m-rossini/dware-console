<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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

	function submitForm() {
	
	   if (document.forms[0].filepath.value.length < 1) {
	      window.alert("<bean:message key="text.selectSomeFile" bundle="requests"/>");
	   	  return;
	   }
	   document.forms[0].submit();
}


</script>

	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%"cellpadding="0" cellspacing="0">
	 	<tr valign="top"> 
			<td height="15">
          		<table  class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0" >
              	<tr class="text8b"> 
                	<td height="100%" class="table-title"><bean:message key="text.uploadTableTitle" bundle="general"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr valign="top" class="text8b">
			<td height="100%" width="100%">
	
<!--                                              -->
<!-- Begin of upload form                         -->
<!--                                              -->
<html:form action="/upload-file" focus="filepath" enctype="multipart/form-data">

				<table width="100%" height="100%" align="center" class="table" cellpadding="0" cellspacing="0">
			 	<tr> 
		        	<td colspan="4" height="35">
		        		<table border="0" width="100%" align="right" cellpadding="1" cellspacing="1" valign="middle">
		              	<tr class="text8b" align="right"> 
		                	<td></td>
		                	<td width="1%"><html:img page="/images/ico_step1.jpg"/></td>
		                	<td width="1%" class="current-step" nowrap><bean:message key="text.uploadTableTitle" bundle="requests"/>&nbsp;</td>
		                	<td width="1%"><html:img page="/images/out.gif"/> </td>
		                	<td width="1%"><html:img page="/images/ico_step2_disabled.jpg"/></td>
		                	<td width="1%" nowrap><bean:message key="text.selectAccountsTableTitle" bundle="requests"/>&nbsp;</td>
		                	<td width="1%"><html:img page="/images/out.gif"/> </td>
		                	<td width="1%"><html:img page="/images/ico_step3_disabled.jpg"/></td>
		                	<td width="1%" nowrap><bean:message key="text.selectOutputsTableTitle" bundle="requests"/>&nbsp;</td>
		                	<td width="1%"><html:img page="/images/out.gif"/> </td>
		                	<td width="1%"><html:img page="/images/ico_step4_disabled.jpg"/></div></td></td>
		                	<td width="1%" nowrap><bean:message key="text.selectEmailTableTitle" bundle="requests"/>&nbsp;</td>
		              	</tr>
		            	</table>
		            </td>
		        </tr>
        		<tr height="15" class="text8">
                   	<td  colspan="4" height="15"></td>
                </tr>
                <tr height="20" class="text8b">
					<td width="17" ></td>
                  	<td   width="112" height="15"><div align="right"><bean:message key="text.filename" bundle="requests"/>&nbsp;</div></td>
                  	<td width="312" height="15"><div align="left">                  	
						<!-- upload file field  -->					
                    	<html:file property="filepath" size="30" styleClass="input-field"/>
                  		</div>
                  	</td>
                  	<td width="127"></td>
                </tr>
        		<tr height="15" class="text8">
                   	<td  colspan="4" height="15"></td>
                </tr>
                <tr height="30" class="text8">
                  	<td height="15">&nbsp;</td>
                	<td class="textBlue" colspan="3" nowrap>
                		<div align="right"><a href="javascript:submitForm()">:: <bean:message key="text.gotoManualAccountSelection" bundle="requests"/> ::</a>&nbsp;&nbsp;&nbsp;&nbsp;</div>
                	</td>
              	</tr>
				<tr>
                  	<td colspan="4"></td>
                </tr>
              	</table>
              	
</html:form>


			</td>
   		</tr>
   		</table>
   	</td>
