<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/login/login-template.jsp" flush="true">
  <tiles:put name="header" value="/login/header.jsp" />
  <tiles:put name="body-content" value="/login/login-content.jsp" /> 
  <tiles:put name="footer" value="/login/footer.jsp" />
</tiles:insert>