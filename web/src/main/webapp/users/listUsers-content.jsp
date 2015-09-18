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

<bean:define id="userList" name="<%=RequestScopeConstants.REQUEST_LISTOFUSERS_KEY%>" scope="request"/>


	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top">
        	<td height="15">
        		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0" >
              	<tr>
                	<td height="100%" class="table-title"><bean:message key="text.listUsersTableTitle" bundle="users"/></td>
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
	  			<tr height="20" class="table-column-title" >

				    <td width="35%"> <div align="center"><bean:message key="text.loginColumn" bundle="users"/></div></td>
				    <td width="30%"> <div align="center"><bean:message key="text.userNameColumn" bundle="users"/></div></td>
				    <td width="17%"> <div align="center"><bean:message key="text.userInsertedDateColumn" bundle="users"/></div></td>
				    <td width="18%"> <div align="left"><bean:message key="text.userLoginLockedColumn" bundle="users"/></div></td>
			  	</tr>

	  				<!-- 		                  	-->
	  				<!-- 		SORTING FORM    	-->
	  				<!-- 		                   	-->
					<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
					<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>

					<script language="Javascript">
						function sortBy(field, orientation) {
							document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>'].value=field;
							document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>'].value=orientation;
							document.forms['sortingForm'].submit();
						}
					</script>


				<tr height="20" class="text8">
			    	<td>
						<form name="sortingForm" method="POST" action="<html:rewrite page="/list-users.do"/>" >
					   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
					   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
						</form>

			    		<div align="center">
			    		<auster:sortUrl orderKey="user_login" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
				    	</auster:sortUrl>
			    		</div>
			    	</td>
			    	<td>
			    		<div align="center">
						<auster:sortUrl orderKey="first_name" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
			    		</auster:sortUrl>
			    		</div>
			    	</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
			  	</tr>

<form action="<html:rewrite page="/update-user-info.do"/>" method="POST" name="userEmailForm">
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_USEREMAIL_KEY%>" value=""/>
</form>

<logic:notEmpty name="userList">
	<logic:iterate name="userList" id="userAtList">

    			<tr height="15" class="text8">

	<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_USER_UPDATE_OTHERS_KEY)) { %>

      				<td width="35%">
      					<div align="left">
      					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  						<a href="javascript:submitDetailsForm('<bean:write name="userAtList" property="login"/>')">
  							<bean:write name="userAtList" property="login"/>
      					</a>
	      				</div>
      				</td>

    <% } else { %>

      				<td width="35%">
      					<div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<logic:match name="userAtList" property="login" value="<%=user.getLogin()%>">
  						<a href="javascript:submitDetailsForm('<bean:write name="userAtList" property="login"/>')">
    </logic:match>

  							<bean:write name="userAtList" property="login"/>

	<logic:match name="userAtList" property="login" value="<%=user.getLogin()%>">
      					</a>
    </logic:match>
	      				</div>
      				</td>

    <% } %>

      				<td width="30%" ><div align="left"><bean:write name="userAtList" property="userName"/></div></td>
      				<td width="17%" ><div align="center"><bean:write name="userAtList" property="createDate" format="dd-MM-yyyy"/></div></td>
      				<td width="18%" >
      					<div align="left">
   		<logic:match name="userAtList" property="userLocked" value="true">
   			<bean:message key="text.loginBlocked" bundle="users"/>
   		</logic:match>
	  					</div>
	  				</td>
				</tr>
	</logic:iterate>

				<tr>
					<td colspan="4"><div align="center">
					</div></td>
				</tr>

		<bean:define id="trueString" value="true"/>

				<tr height="25" valign="middle">
					<td colspan="2"></td>
	    			<td colspan="2" class="textBlue"><div align="right">
    <% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_USER_GOUPS_VIEW_KEY)) { %>
	    			<html:link action="/list-groups">:: <bean:message key="text.showGroups" bundle="users"/> ::</html:link>&nbsp;&nbsp;
    <% } %>
    <% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_USER_UPDATE_OTHERS_KEY)) { %>
        			<html:link action="/display-new-user" paramId="<%=RequestScopeConstants.REQUEST_USER_CURRGROUP_KEY%>" paramName="trueString">:: <bean:message key="text.createUser" bundle="users"/> ::</html:link>&nbsp;&nbsp;&nbsp;&nbsp;
    <% } %>
        			</div></td>
				</tr>


				<tr height="45">
			    	<td colspan="5">&nbsp;</td>
			    </tr>
				<tr height="100%" valign="bottom">
					<td colspan="5"><div align="center">
		<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request" type="java.lang.String"/>
		<bean:define id="totalPages" name="<%=RequestScopeConstants.REQUEST_TOTALPAGES_KEY%>" scope="request" type="java.lang.String"/>

		<auster:index pageId="<%=pageId%>" totalPages="<%=totalPages%>"
					  style="text8b"
	    	          firstUrl="javascript:move({0})"
	        	      previousUrl="javascript:move({0})"
	            	  pageIndexUrl="javascript:move({0})"
		              nextUrl="javascript:move({0})"
		              lastUrl="javascript:move({0})"/>

		<script language="Javascript">

			function move(toPage) {
				document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>'].value = toPage;
				document.forms['moveToPage'].submit();
			}

		</script>

	   	<form name="moveToPage" action="<html:rewrite page="/list-users.do"/>" method="POST">
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>

	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
	    </form>

					</div></td>
				</tr>

</logic:notEmpty>

				</table>
			</td>
	   	</tr>
	   	</table>
	</td>