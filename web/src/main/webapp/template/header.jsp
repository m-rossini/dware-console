<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
    <td>
        <table cellpadding="0" width="100%" height="100%" cellspacing="0">
            <tr valign="middle">
<logic:present name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session">
    <bean:define id="userInfo" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"/>
	            <td height="58" class="text8b">
	            	<div align="center"><bean:message key="text.userConnected" bundle="general"/> ::
	                <bean:write name="userInfo" property="fullName"/></div>
	            </td>
                <td>
                	<div align="right"><html:link action="/logout.do">
                	<html:img page="/images/out.gif" align="absmiddle" width="12" height="10" border="0"/>
                	<font class="logoff-text">
                	<bean:message key="text.logout" bundle="general"/></font>
                	</html:link> &nbsp;&nbsp;</div>
                </td>
</logic:present>

<logic:notPresent name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session">
                <td height="58">&nbsp;</td>
                <td><div align="right">&nbsp;&nbsp;</div></td>
</logic:notPresent>

            </tr>
        </table>
    </td>