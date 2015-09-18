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


<script>

function submitDetailsForm(email) {
	document.forms['userEmailForm'].elements['<%=RequestScopeConstants.REQUEST_USEREMAIL_KEY%>'].value = email;
	document.forms['userEmailForm'].submit();
}

</script>

<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>

<bean:define id="groupList" name="<%=RequestScopeConstants.REQUEST_USER_GROUPLIST_KEY%>" scope="request"/>

	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top">
        	<td height="15">
        		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0" >
              	<tr>
                	<td height="100%" class="table-title"><bean:message key="text.listGroupsTableTitle" bundle="users"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>
		<tr  valign="top">
			<td height="100%" width="100%">
				<table width="100%" height="100%" align="center"  class="table" cellpadding="3" cellspacing="0">
				<tr height="15" class="table-column-title">
					<td width="2%">&nbsp;</td>
					<td width="26%"><div align="left"><bean:message key="text.groupNameColumn" bundle="users"/></div></td>
					<td width="12%"><div align="center"><bean:message key="text.viewRequestsPermissionsColumn" bundle="users"/></div></td>
					<td width="12%"><div align="center"><bean:message key="text.createRequestPermissionsColumn" bundle="users"/></div></td>
					<td width="12%"><div align="center"><bean:message key="text.iqPermissionColumn" bundle="users"/></div></td>
					<td width="12%"><div align="center"><bean:message key="text.userCreatePermissionColumn" bundle="users"/></div></td>
					<td width="12%"><div align="center"><bean:message key="text.groupViewPermissionColumn" bundle="users"/></div></td>
					<td width="12%"><div align="center"><bean:message key="text.groupCreatePermissionColumn" bundle="users"/></div></td>
                    <td width="12%"><div align="center"><bean:message key="text.groupRequestManagerPermissionColumn" bundle="users"/></div></td>
				</tr>

<form action="<html:rewrite page="/update-user-info.do"/>" method="POST" name="userEmailForm">
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_USER_SELECTED_GROUP_KEY%>" value=""/>
</form>

<logic:notEmpty name="groupList">

	<logic:iterate name="groupList" id="groupAtList">

    			<tr height="15" class="text8">
	  				<td>&nbsp;</td>

      				<td width="26%">
      					<div align="left">
      						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_USER_GOUPS_CREATE_KEY)) { %>
   							<html:link action="/load-group" paramId="<%=RequestScopeConstants.REQUEST_USER_CURRGROUP_KEY%>" paramName="groupAtList" paramProperty="groupName">   							
   								<bean:write name="groupAtList" property="groupName"/>
   							</html:link>
   							<% } else { %>	
   								<bean:write name="groupAtList" property="groupName"/>	
							<% } %>   								
      					</div>
      				</td>
					<!-- view group request permission -->
					<bean:define id="groupPermissions" type="java.util.Map" name="groupAtList" property="permissions"/>
					<td>
						<div align="center">
						<% if (groupPermissions.containsKey(PermissionConstants.PERMISSION_REQUEST_GROUPVIEW_KEY)) { %>
							<html:img page="/images/yes.jpg"/>
						<% } else { %>
							<html:img page="/images/no.jpg"/>
						<% } %>
						</div>
					</td>
					<!-- create request permission -->
					<td>
						<div align="center">
						<% if (groupPermissions.containsKey(PermissionConstants.PERMISSION_REQUEST_CREATE_KEY)) { %>
							<html:img page="/images/yes.jpg"/>
						<% } else { %>
							<html:img page="/images/no.jpg"/>
						<% } %>
						</div>
					</td>
					<!-- IQ permission -->
					<bean:define id="groupPermissions" type="java.util.Map" name="groupAtList" property="permissions"/>
					<td>
						<div align="center">
						<% if (groupPermissions.containsKey(PermissionConstants.PERMISSION_IQ_SEARCH_KEY)) { %>
							<html:img page="/images/yes.jpg"/>
						<% } else { %>
							<html:img page="/images/no.jpg"/>
						<% } %>
						</div>
					</td>
					<!-- user management permission -->
					<td>
						<div align="center">
						<% if (groupPermissions.containsKey(PermissionConstants.PERMISSION_USER_UPDATE_OTHERS_KEY)) { %>
							<html:img page="/images/yes.jpg"/>
						<% } else { %>
							<html:img page="/images/no.jpg"/>
						<% } %>
						</div>
					</td>
					<!-- group view permission -->
					<bean:define id="groupPermissions" type="java.util.Map" name="groupAtList" property="permissions"/>
					<td>
						<div align="center">
						<% if (groupPermissions.containsKey(PermissionConstants.PERMISSION_USER_GOUPS_VIEW_KEY)) { %>
							<html:img page="/images/yes.jpg"/>
						<% } else { %>
							<html:img page="/images/no.jpg"/>
						<% } %>
						</div>
					</td>
					<!-- groups management permission -->
					<td>
						<div align="center">
						<% if (groupPermissions.containsKey(PermissionConstants.PERMISSION_USER_GOUPS_CREATE_KEY)) { %>
							<html:img page="/images/yes.jpg"/>
						<% } else { %>
							<html:img page="/images/no.jpg"/>
						<% } %>
						</div>
					</td>
                                                    <!-- groups request management -->
                                                    <td>
                                                        <div align="center">
                                                        <% if (groupPermissions.containsKey(PermissionConstants.PERMISSION_MANAGER_REQUEST_KEY)) { %>
                                                            <html:img page="/images/yes.jpg"/>
                                                        <% } else { %>
                                                            <html:img page="/images/no.jpg"/>
                                                    <% } %>
                                                    </div>
                                                </td>
  				</tr>

	</logic:iterate>

				<tr height="45">
			    	<td colspan="8">&nbsp;</td>
			    </tr>
</logic:notEmpty>


				<tr height="25" valign="middle">
	    			<td colspan="8" class="textBlue"><div align="right">
	    			<html:link action="/list-users">:: <bean:message key="text.showUsers" bundle="users"/> ::</html:link>&nbsp;&nbsp;
			<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_USER_GOUPS_CREATE_KEY)) { %>
        			<html:link page="/users/createGroup.jsp">:: <bean:message key="text.createGroup" bundle="users"/> ::</html:link>&nbsp;&nbsp;&nbsp;&nbsp;
        	<% } %>
        			</div></td>
				</tr>
				<tr>
    				<td colspan="86"></td>
				</tr>
				</table>
			</td>
	   	</tr>
	   	</table>
	</td>

	<form name="updateGroup" method="POST" action="<html:rewrite page="/update-group.do"/>">
		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_USER_SELECTED_GROUP_KEY%>" value=""/>
		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_USER_SELECTED_UPDATEACTION_KEY%>" value=""/>
		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_USER_SELECTED_UPDATEVALUE_KEY%>" value=""/>
	</form>


<script language="Javascript">


function updateGoup(groupId, groupPermission, changeTo) {
	document.forms['updateGroup'].elements['<%=RequestScopeConstants.REQUEST_USER_SELECTED_GROUP_KEY%>'].value=groupId;
	document.forms['updateGroup'].elements['<%=RequestScopeConstants.REQUEST_USER_SELECTED_UPDATEACTION_KEY%>'].value=groupPermission;
	document.forms['updateGroup'].elements['<%=RequestScopeConstants.REQUEST_USER_SELECTED_UPDATEVALUE_KEY%>'].value=changeTo;

	document.forms['updateGroup'].submit();
}
</script>