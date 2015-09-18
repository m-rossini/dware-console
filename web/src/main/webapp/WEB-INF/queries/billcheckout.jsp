<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery id="query01" 
                  dataSource="billcheckout/reportDB"
				  catalogUri="/WEB-INF/queries/ep.xml" 
				  dynResolver="br.com.auster.matsya.mondrian.i18n.ResouceEnabledDynamicLocalizer" 
				  dynLocale="pt_BR">
				  
select {[Measures].[ConsequenceCount]} on columns,
  {([Cycle].[All Cycles] , [Geographic].[All States], [Account].[All Accounts], [Rule].[All Rules])} ON rows
from BillcheckoutConsequences
</jp:mondrianQuery>


<c:set var="title01" scope="session">Cr�ticas de Billcheckout</c:set>
