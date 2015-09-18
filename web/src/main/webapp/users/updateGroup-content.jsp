<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>
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


function submitForm() {
	form = document.forms['updateGroupForm'];
	form.submit();
}


function submitStatusForm(status) {
	form = document.forms['updateGroupStatusForm'];
	form.groupStatus.value=status;
	form.submit();
}

</script>

	<td>
		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top"> 
        	<td height="15">
        		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
	            <tr> 
                	<td height="100%" class="table-title"><bean:message key="text.createGroupTableTitle" bundle="users"/></td>
				</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr  valign="top">
			<td  height="100%" width="100%">

<html:form action="/update-group" method="post">

				<bean:define id="groupName" name="updateGroupForm" property="groupName"/>
				<html:hidden property="groupName" value="<%=groupName.toString()%>"/>

				<table width="100%" height="100%" align="center" class="table" cellpadding="3" cellspacing="0">
			  	<tr height="45">
  	   				<td colspan="5"></td>
				</tr>	
    			<tr>
					<td width="10%"></td>
       				<td width="25%" class="textBlue"> <div align="left"><bean:message key="text.groupNameColumn" bundle="users"/></div></td>
          			<td colspan="3" class="text8b"><div align="left"><bean:write name="groupName"/></div></td>
  				</tr>
  				<tr height="25">
					<td colspan="5">&nbsp;</td>
				</tr>	
				
    			<tr>
					<td>&nbsp;</td>
       				<td width="25%" class="textBlue"> <div align="left"><bean:message key="text.viewRequestsPermissionsColumn" bundle="users"/></div></td>
       				<td width="20%" class="text8"><div align="left"><html:multibox name="updateGroupForm" property="permissionId" value="<%=PermissionConstants.PERMISSION_REQUEST_GROUPVIEW_KEY%>"/></div></td>
       				<td width="25%" class="textBlue"> <div align="left"><bean:message key="text.createRequestPermissionsColumn" bundle="users"/></div></td>
       				<td class="text8"><div align="left"><html:multibox name="updateGroupForm" property="permissionId"  value="<%=PermissionConstants.PERMISSION_REQUEST_CREATE_KEY%>"/></div></td>
				</tr>
				
    			<tr>
					<td></td>
       				<td class="textBlue"> <div align="left"><bean:message key="text.iqPermissionColumn" bundle="users"/></div></td>
       				<td class="text8"><div align="left"><html:multibox name="updateGroupForm" property="permissionId"  value="<%=PermissionConstants.PERMISSION_IQ_SEARCH_KEY%>"/></div></td>
       				<td class="textBlue"> <div align="left"><bean:message key="text.userCreatePermissionColumn" bundle="users"/></div></td>
       				<td class="text8"><div align="left"><html:multibox name="updateGroupForm" property="permissionId"  value="<%=PermissionConstants.PERMISSION_USER_UPDATE_OTHERS_KEY%>"/></div></td>
				</tr>

    			<tr>
					<td></td>
       				<td class="textBlue"> <div align="left"><bean:message key="text.groupViewPermissionColumn" bundle="users"/></div></td>
       				<td class="text8"><div align="left"><html:multibox name="updateGroupForm" property="permissionId"  value="<%=PermissionConstants.PERMISSION_USER_GOUPS_VIEW_KEY%>"/></div></td>
       				<td class="textBlue"> <div align="left"><bean:message key="text.groupCreatePermissionColumn" bundle="users"/></div></td>
       				<td class="text8"><div align="left"><html:multibox name="updateGroupForm" property="permissionId"  value="<%=PermissionConstants.PERMISSION_USER_GOUPS_CREATE_KEY%>"/></div></td>
				</tr>

                              <tr>
                                  <td></td>
                                  <td class="textBlue"> <div align="left"><bean:message key="text.managerRequestPermissionColumn" bundle="users"/></div></td>
                                  <td class="text8"><div align="left"><html:multibox    property="permissionId" value="<%=PermissionConstants.PERMISSION_MANAGER_REQUEST_KEY%>"/></div></td>
                              </tr>
				
  				<tr height="15">
					<td colspan="5">&nbsp;</td>
				</tr>	
				
  				<tr height="25" valign="middle">
  	   				<td colspan="5" class="textBlue">
  	   					<div align="right">
  	   						<logic:match name="updateGroupForm" property="groupStatus" value="<%=RequestScopeConstants.REQUEST_USER_SUSPENDEDGROUP_KEY%>">
	  	         				<a href="javascript:submitStatusForm('<%=RequestScopeConstants.REQUEST_USER_ACTIVEGROUP_KEY%>');">:: <bean:message key="text.enable" bundle="users"/> ::</a>&nbsp;&nbsp;&nbsp;&nbsp;
	  	         			</logic:match>
	  	         			<logic:match name="updateGroupForm" property="groupStatus" value="<%=RequestScopeConstants.REQUEST_USER_ACTIVEGROUP_KEY%>">
	  	         				<a href="javascript:submitStatusForm('<%=RequestScopeConstants.REQUEST_USER_SUSPENDEDGROUP_KEY%>');">:: <bean:message key="text.disable" bundle="users"/> ::</a>&nbsp;&nbsp;&nbsp;&nbsp;
  	         				</logic:match>
  	         				<a href="javascript:submitForm();">:: <bean:message key="text.save" bundle="users"/> ::</a>&nbsp;&nbsp;&nbsp;&nbsp;
  	         				<a href="javascript:history.back();">:: <bean:message key="text.cancel" bundle="users"/> ::</a>&nbsp;&nbsp;
  	   					</div>
  	   				</td>
				</tr>	
				<tr height="100%">
    				<td colspan="2"></td>
				</tr>
</html:form>
				</table>
			</td>
   		</tr>
   		</table>
   	</td>

<html:form action="/update-group-status" method="post">
	<html:hidden property="groupStatus" value=""/>

	<bean:define id="groupName" name="updateGroupForm" property="groupName"/>
	<html:hidden property="groupName" value="<%=groupName.toString()%>"/>
</html:form>
