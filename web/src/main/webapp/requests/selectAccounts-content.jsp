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

	function checkSelectedBox(accountId) {
	
	form = document.forms['showAccountsForm'];
		if (form.selectedAccount.length==undefined) {
			if (form.selectedAccount.value == accountId) {
				form.selectedAccount.checked=true;
			}
		} else {
			for (i=0;i<form.selectedAccount.length;i++) {
				if (form.selectedAccount[i].value == accountId) {
					form.selectedAccount[i].checked=true;
					return;
				}
			}
		}
	}
	
	
	function selectUnselect() {
	
		form = document.forms['showAccountsForm'];
		if (form.selectedAccount.length==undefined) {
			form.selectedAccount.checked=form.selectedAll.checked;
		} else {
			for (i=0;i<form.selectedAccount.length;i++) {
				form.selectedAccount[i].checked=form.selectedAll.checked;
			}
		}
	}
	
	
	function clearSelection() {
		form = document.forms['clearSelectionForm'];
		form.submit();
	}
	
	
	function markAllAsSelected() {
		form = document.forms['selectAllAccountsForm'];
		form.submit();
	}
	
	function continueWithSelectedAccounts() {
		form = document.forms['showAccountsForm'];
		form.elements['<%=RequestScopeConstants.REQUEST_CONTINUE_KEY%>'].value='true';
		form.submit();
	}
	
	
	
	<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_ERRORFOUND_KEY%>">
		window.alert("<bean:message key="text.selectOneAccount" bundle="requests"/>");
	</logic:notEmpty>

</script>




<!--                            -->
<!-- web-page beans definition  -->
<!--                            -->
<bean:define id="filename" name="<%=SessionScopeConstants.SESSION_UPLOADEDFILE_KEY%>" scope="session" type="java.lang.String"/>


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

            	<table width="100%" height="100%" class="table" cellpadding="3" cellspacing="0" >
			 	<tr> 
					<td colspan="4" height="35">
		        		<table width="100%" align="right" cellpadding="1" cellspacing="1" valign="middle">
		              	<tr class="text8b" align="right"> 
		                	<td width="89%"></td>
		                	<td width="1%"><html:img page="/images/ico_step1.jpg"/></td>
		                	<td width="1%" nowrap><bean:message key="text.uploadTableTitle" bundle="requests"/>&nbsp;</td>
		                	<td width="1%"><html:img page="/images/out.gif"/> </td>
		                	<td width="1%"><html:img page="/images/ico_step2.jpg"/></td>
		                	<td width="1%" class="current-step" nowrap><bean:message key="text.selectAccountsTableTitle" bundle="requests"/></font>&nbsp;</td>
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
                	<td colspan="3">
	                	<bean:message key="text.listOfAccountsFound" bundle="requests"/> <%=filename.substring(filename.lastIndexOf(System.getProperty("file.separator"))+1, filename.length())%> 
					</td>
                </tr>
              	<tr height="40">   
                	<td colspan="4"></td>
              	</tr>


<!--                                    -->
<!--    begin of account page form      -->
<!--                                    -->

<logic:notEmpty name="<%=SessionScopeConstants.SESSION_LISTOFRESULTS_KEY%>">

	<bean:define id="accounts" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>


	<!- ###################################################################### -->
	<!-                               Filtering Form                           -->
	<!- ###################################################################### -->
						   		
   	<form name="filterPage" action="<html:rewrite page="/show-accounts.do"/>" method="POST">
   	
		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="1"/>
		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>
	   		
   		<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" id="filterBy" type="java.lang.String"/>
	   		
              	<tr valign="bottom">
                	<td colspan="4" height="10">
                    	<table width="95%" align="center" cellpadding="0" cellspacing="1">
					   	<tr>
					   		<td>   		
					    		<div align="right" class="text8">
					    		
			<bean:message key="text.labe.filterBy" bundle="requests"/> :&nbsp;&nbsp;
			<auster:selectBox style="text8" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" defaultKey="<%=filterBy%>">
				<auster:selectKey key="key">Account Id</auster:selectKey>
			</auster:selectBox>
			
								&nbsp;&nbsp;
								<input type="text" class="text8" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value=""/>
								<font class="text8b">
								&nbsp;&nbsp;<a href="javascript:document.forms['filterPage'].submit();"> :: <bean:message key="text.button.filter" bundle="requests"/> :: </a>&nbsp;&nbsp;&nbsp;&nbsp;</font>
								</div>
			
			<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>">
				<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterCondition" type="java.lang.String"/>
				<script>
					document.forms['filterPage'].elements['<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>'].value='<%=filterCondition%>'
				</script>
			</logic:notEmpty>
    </form>

                			</td>
                		</tr>
                		</table>
                	</td>
              	</tr> 


	<!- ###################################################################### -->
	<!-                          List of Accounts                              -->
	<!- ###################################################################### -->


	<html:form method="POST" action="/show-accounts">
	
		<bean:define id="offset" name="<%=RequestScopeConstants.REQUEST_OFFSET_KEY%>" scope="request" type="java.lang.String"/>
		<bean:define id="displayLen" name="<%=RequestScopeConstants.REQUEST_DISPLAYLEN_KEY%>" scope="request" type="java.lang.String"/>

				<tr>
					<td></td>
                	<td colspan="2">
            	    	<font class="text8"><input type="checkbox" onChange="javascript:selectUnselect()"  name="selectedAll"><bean:message key="text.selectUnselect" bundle="requests"/></font>
            	    </td>
	       		</tr>

		<logic:iterate name="accounts" id="accountIdAndName" offset="<%=offset%>" length="<%=displayLen%>">
				<tr height="15">
					<td></td>
                	<td colspan="2">
            	    	<font class="text8">
            	    		<bean:define id="accountKey"  name="accountIdAndName" property="key" type="java.lang.String"/>
            	    		<html:checkbox property="selectedAccount" value="<%=accountKey%>">
	            	    		&nbsp;&nbsp;<bean:write name="accountIdAndName" property="key"/> - <bean:write name="accountIdAndName" property="value"/>
	            	    	</html:checkbox>
	            	    	<html:hidden property="displayedAccount" value="<%=accountKey%>"/>
            	    	</font>
            	    </td>
	       		</tr>
		</logic:iterate>              
	
			    <tr  valign="middle">
			    	<td colspan="6" height="25" class="textBlue">
					<div align="right">
					<html:link href="javascript:clearSelection()"> :: <bean:message key="text.clearSelection" bundle="requests"/> :: </html:link>&nbsp;&nbsp;
					<html:link href="javascript:markAllAsSelected()"> :: <bean:message key="text.selectAllAccounts" bundle="requests"/> :: </html:link>&nbsp;&nbsp;
					<html:link href="javascript:continueWithSelectedAccounts()">:: <bean:message key="text.continue" bundle="requests"/> :: </html:link>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
			    	</td>
			    </tr>
	
				<tr height="45"> 
			    	<td colspan="6">&nbsp;</td>
			    </tr>

			<tr height="100%" valign="bottom">
				<td colspan="6"><div align="center">

	<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request" type="java.lang.String"/>
	<bean:define id="totalPages" name="<%=RequestScopeConstants.REQUEST_TOTALPAGES_KEY%>" scope="request" type="java.lang.String"/>
							
	<auster:index pageId="<%=pageId%>" totalPages="<%=totalPages%>" 
				  style="text8b"
	              firstUrl="javascript:move({0})" 
	              previousUrl="javascript:move({0})"
	              pageIndexUrl="javascript:move({0})"
	              nextUrl="javascript:move({0})"
	              lastUrl="javascript:move({0})"/>

				</td>
			</tr>
			
	<script language="Javascript">
	
		function move(toPage) {
			document.forms['showAccountsForm'].elements['<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>'].value = toPage;
			document.forms['showAccountsForm'].submit();
		}
	</script>

	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value="<%=filterBy%>"/>
   		
	<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>">
		<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterCondition" type="java.lang.String"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterCondition%>"/>
	</logic:notEmpty>

	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value=""/>

	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_CONTINUE_KEY%>" value="false"/>
	
            </table>
	</html:form>
</logic:notEmpty>

<logic:empty name="<%=SessionScopeConstants.SESSION_LISTOFRESULTS_KEY%>">
				<tr height="15">
					<td></td>
                	<td colspan="2">
            	    	<font class="text8"><bean:message key="text.noAccountsFound" bundle="requests"/>
            	    </td>
	       		</tr>
</logic:empty>
			

			</td>
   		</tr>
   		</table>
   	</td>


<!-- 
	Action to clear all previously selected accounts 
-->
<form name="clearSelectionForm" method="POST" action="<html:rewrite page="/clear-account-selection.do"/>">
</form>

<!-- 
	Action to mark all (from all pages) accounts as selected
-->
<form name="selectAllAccountsForm" method="POST" action="<html:rewrite page="/select-all-accounts.do"/>">
</form>

   	
<logic:notEmpty name="<%=SessionScopeConstants.SESSION_CACHEDACCOUNTS_KEY%>" scope="session">
	<logic:iterate name="<%=SessionScopeConstants.SESSION_CACHEDACCOUNTS_KEY%>" id="cachedAccount">
		<script> checkSelectedBox('<bean:write name="cachedAccount"/>'); </script>
	</logic:iterate>
</logic:notEmpty>   	
