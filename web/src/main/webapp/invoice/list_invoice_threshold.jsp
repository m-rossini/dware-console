<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<tiles:insert page="/template/request-template.jsp" flush="true">
	<tiles:put name="header" value="/template/header.jsp"/>
	<tiles:put name="menu" value="/template/menu.jsp"/>
	<tiles:put name="body-content" value="/invoice/list_invoice_threshold-content.jsp"/>
</tiles:insert>