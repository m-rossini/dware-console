<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<html:html locale="true">
	<HEAD>
		<TITLE>::::<bean:message key="text.title" bundle="general"/> ::::</TITLE>
		<LINK href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css"/>
		<LINK href="<html:rewrite page="/css/data_general.css"/>" rel="stylesheet" type="text/css"/>
	</HEAD>
	<BODY>
		<TABLE width="100%" height="100%" cellspacing="2" cellpadding="0">
			<TR>
				<TD width="182" class="side-firstLine-background" height="5"/>
				<TD width="182" class="center-firstLine-background"/>
				<TD class="center-firstLine-background"/>
				<TD width="189" class="side-firstLine-background" height="5"/>
			</TR>
			<TR valign="middle">
				<TD class="side-background"/>
				<TD height="58"><DIV align="center"><html:img page="/images/logo.jpg" alt="lOGO"/></DIV></TD>
					<tiles:insert attribute="header"/>
				<TD class="side-background"/>
			</TR>
			<TR valign="middle">
				<TD height="5" class="sidebar-background"/>
				<TD height="5" colspan="2" class="centerbar-background"/>
				<TD class="sidebar-background"/>
			</TR>
			<TR valign="top">
				<TD class="side-background" rowspan="2" background="<html:rewrite page="images/bg_dot-fade.gif"/>"/>
					<tiles:insert attribute="menu"/>
					<tiles:insert attribute="body-content"/>
				<TD rowspan="2" class="side-background" background="<html:rewrite page="/images/bg_dot-fade.gif"/>"/>
			</TR>
			<TR valign="top">
				<TD colspan="2"/>
			</TR>
			<TR>
				<TD height="5" class="sidebar-background"/>
				<TD height="5" colspan="2" height="5" class="centerbar-background"/>
				<TD class="sidebar-background"/>
			</TR>
		</TABLE>
	</BODY>
</html:html>