<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>


<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
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





	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top"> 
        	<td height="15">
        		<table class="table" width="100%" align="center" cellpadding="0" cellspacing="0" >
              	<tr> 
                	<td height="100%" class="table-title"><bean:message key="text.displayResultsTable" bundle="queries"/></td>
              	</tr>
            	</table>
            </td>
        </tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr  valign="top" align="center" valign="top">
			<td  height="100%" width="100%">
				<table width="100%" height="100%" cellpadding="3" class="table">
				<tr class="text8">
					<td valign="top" height="100%" width="100%">	
					
						<bean:define id="resultList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>
		
						<bean:define id="currentQuery" name="<%=RequestScopeConstants.REQUEST_QUERYINFO_KEY%>"/>
						<bean:define id="offset"       name="<%=RequestScopeConstants.REQUEST_OFFSET_KEY%>" type="java.lang.Integer"/>
		
						<input type="hidden" value="<%=offset%>"/>
		                <display:table name="pageScope.resultList" id="resultItem" 			   
		                		       cellpadding="3"             
		                			   cellspacing="1"             
		                			   pagesize="20" 
		                               offset="<%=offset.intValue()%>">
		
		                        <% int counter=0; %>
		                        <logic:iterate id="rowTitle" name="currentQuery" property="queriedColumns">
		
		                        		<bean:define id="columnObject" name="rowTitle" property="column"/>
		                                <bean:define id="columnTitle" name="columnObject" property="displayCaption" type="java.lang.String"/>
		
		                                <display:column property="<%="cell["+(counter++)+"]"%>"
		                                                title="<%=columnTitle%>" 
		                                                headerClass="table-column-title" 
		                                                class="text8"
		                                                decorator="br.com.auster.dware.console.queries.decorator.AllInOneColumnDecorator"/>
		                                                
		                         </logic:iterate>
		
								 <display:setProperty name="paging.banner.placement"   value="bottom"/>
								 <display:setProperty name="paging.banner.group_size"  value="6"/>
		
								 <!-- Removing page indexing, since it doesnt work in a struts-like environment -->						 
								 <display:setProperty name="paging.banner.page.link"        value=""/>
								 <display:setProperty name="paging.banner.page.separator"   value=""/>
								 <display:setProperty name="paging.banner.onepage"          value=""/>
								 <display:setProperty name="paging.banner.last"             value=""/>
								 <display:setProperty name="paging.banner.first"            value=""/>
								 <display:setProperty name="paging.banner.full"             value=""/>
								 <!-- Removing extra page indexing text, since it doesnt work in a struts-like environment -->
								 <display:setProperty name="paging.banner.no_items_found"   value=""/>
								 <display:setProperty name="paging.banner.one_item_found"   value=""/>
								 <display:setProperty name="paging.banner.all_items_found"  value=""/>
								 <display:setProperty name="paging.banner.some_items_found" value=""/>
								 
								 <display:setProperty name="export.types" value="csv xml pdf excel"/>
		
		                </display:table>
					</td>			
				</tr>	
				<tr>
					<td></td>
				</tr>
				<tr height="25" valign="middle">
					<td>
						<!-- restart button -->
						<br>						
						<div align="right">
							<html:link action="/list-queries" styleClass="textBlue"> :: <bean:message key="text.restart" bundle="general"/> :: </html:link>&nbsp;&nbsp;
						</div>
					</td>	
				</tr>
				<tr height="100%" valign="bottom">
					<td>	
						<!-- paging controls -->
						<div align="center">						
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
						</div align="center">
					</td>
				</tr>
				</table>

			</td>
   		</tr>
   		</table>
   	</td>   	
	
	
   	<form name="moveToPage" action="<html:rewrite page="/display-query.do"/>" method="POST">
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>
	</form>	
