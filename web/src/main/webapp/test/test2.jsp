<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>

<auster:checkLogon
			sessionKey="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>">

<script language="javascript">
	window.location='<html:rewrite page="/error.jsp"/>?<%=ExceptionConstants.USERNOTLOGGED_KEY%>=true';
</script>			
			
</auster:checkLogon>


<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
