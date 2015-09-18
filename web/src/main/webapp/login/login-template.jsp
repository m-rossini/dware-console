<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html:html locale="true">
<head>
	<title>:::: <bean:message key="text.title" bundle="general"/> ::::</title>
	<link href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page="/css/data_login.css"/>" rel="stylesheet" type="text/css">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>


<body topmargin="0" leftmargin="0" bgcolor="#FFFFFF">

<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr valign="middle">
	<td width="100%" height="100%">
		<table width="100%" cellspacing="0" cellpadding="0">
        <tr  bgcolor="#ffffff" valign="middle">
        	<td></td>    
			<tiles:insert attribute="header"/>
			<tiles:insert attribute="body-content"/>
			<td width="162">&nbsp;</td>
		</tr>
		<tr>
  			<tiles:insert attribute="footer"/>
		</tr>
        <tr valign="middle">
          <td  height="100" colspan="2"><div align="center">&nbsp;&nbsp;&nbsp;</div></td>
          <td  colspan="2">&nbsp;</td>
        </tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<div align="center"><font class="text7">
	    <br>Copyright&copy; 2004-2005 <a href="http://www.auster.com.br" target="_new">Auster Solutions do Brasil</a></font></div>
	</td>
</tr>
</table>

</body>
</html:html>