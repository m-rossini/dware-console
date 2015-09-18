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




<script language="javascript">

function submitForm(accFile) {
    document.forms[0].<%=Constants.ACCFILENAME_KEY%>.value = accFile;
	document.forms[0].submit();
}

</script>


<!--                            -->
<!-- web-page beans definition  -->
<!--                            -->
<bean:define id="userInfo" name="<%=Constants.USERINFO_KEY%>" scope="session" type="br.com.auster.billcheckout.portal.web.logic.user.UserView"/>
<bean:define id="localFiles" name="<%=Constants.LISTOFFILES_KEY%>" scope="request" type="java.util.List"/>


	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		 <tr valign="top"> 
          	<td height="15">
          		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
              	<tr> 
                	<td height="100%" class="table-title"><bean:message key="text.uploadTableTitle" bundle="general"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr  valign="top">
			<td  height="100%"width="100%">

<!--                                    -->
<!--    begin of account page form      -->
<!--                                    -->
				<html:form method="post" action="/ftp-request-create">

	            	<table width="100%" height="100%" class="table" cellpadding="3" cellspacing="0" >
				 	<tr> 
						<td colspan="4" height="35">
			        		<table width="100%" align="right" cellpadding="1" cellspacing="1" valign="middle">
			              	<tr class="text8b" align="right"> 
			                	<td width="89%"></td>
			                	<td width="1%"><html:img page="/images/ico_step1.jpg"/></td>
			                	<td width="1%" class="current-step" nowrap><bean:message key="text.selectFileTableTitle" bundle="requests"/>&nbsp;</td>
			                	<td width="1%"><html:img page="/images/out.gif"/> </td>
			                	<td width="1%"><html:img page="/images/ico_step2_disabled.jpg"/></td>
			                	<td width="1%" nowrap><bean:message key="text.selectAccountsTableTitle" bundle="requests"/></font>&nbsp;</td>
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
	                	<td colspan="4"><div align="center"></div></td>
	              	</tr>
	              	<tr height="15" class="text8b">
						<td width="15"></td>
	                	<td colspan="3"><bean:message key="text.listOfFilesFound" bundle="requests"/></td>
	                </tr>
	              	<tr height="15">                   
	                	<td colspan="4"></td>
	              	</tr>
	
<logic:iterate name="localFiles" id="fileInfo" type="br.com.auster.billcheckout.portal.web.logic.request.RequestFileView">
	              
					<tr height="15">
						<td></td>
	                	<td colspan="2">
	                        <html:radio property="<%=Constants.FILENAME_KEY%>" value="<%=fileInfo.getFilename()%>"/>&nbsp;
	            	    	<font class="text8"><bean:write name="fileInfo" property="fileNameNoPath"/>
	            	    </td>
		       		</tr>
	              
</logic:iterate>              
	
				  	<tr height="40">                  	
	                  	<td height="15">&nbsp;</td>
    	            	<td colspan="3" class="textBlue" nowrap>
    	            		<div align="right">
    	            			<a href="javascript:submitForm('false')" >:: <bean:message key="text.gotoManualAccountSelection" bundle="requests"/> ::</a>&nbsp;&nbsp;
            	    		    <a href="javascript:submitForm('true')">:: <bean:message key="text.gotoAccountFile" bundle="requests"/> ::</a>&nbsp;&nbsp;&nbsp;&nbsp;
        	        		</div>
                		</td>
	              	</tr>
	              	<tr height="100%">
	                	<td colspan="4"><div align="center"></div></td>
	              	</tr>
	              	</table>
	              	
	              	<html:hidden property="<%=Constants.ACCFILENAME_KEY%>" value="false"/>
	              	
				</html:form>
<script>
	// initialize radio button 
	document.forms[0].<%=Constants.FILENAME_KEY%>[0].checked=true
</script>

			</td>
   		</tr>
   		</table>
   	</td>
