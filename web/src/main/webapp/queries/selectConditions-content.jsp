<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>

<%@ page import="br.com.auster.facelift.queries.interfaces.QueryFunctions"%>

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



<script language="Javascript">



function disableWhenEnabled(htmlElement) {
   htmlElement.disabled = true;
   htmlElement.value = "null";
}

function enableWhenEnabled(htmlElement) {
   htmlElement.disabled = false;
   htmlElement.value="";
}


function findSelectedRow(bodyElement) {
   for (i=0; i < bodyElement.rows.length; i++) {
      if (bodyElement.rows[i].bgColor == '#898989') {
         return i;
      }
   }
}


function highlightRow(bodyElement, rowElement) {
	for (i=0; i < bodyElement.rows.length; i++) {
		bodyElement.rows[i].bgColor = "#FFFFFF";
	}
	rowElement.bgColor = "#898989";
}


function addTableRow(tBodyElement, fieldName, fieldValue) {
	if ((tBodyElement.rows.length == 1) && (tBodyElement.rows[0].cells.length == 1)) {
		tBodyElement.deleteRow(0);
	}
	
	row = tBodyElement.insertRow(0);
    row.height=20; 
    row.onclick=new Function('highlightRow(document.getElementById(\'myTableContent\'), this)');
	cell = row.insertCell(0);
	cell.innerHTML =  "<input type=\"hidden\" name=\"queryCondition\" value=\"" + fieldValue + "\" class=\"input-field\"></input>";
    cell = row.insertCell(1);
    cell.innerHTML = "<div class=\"text8\">&nbsp;&nbsp;&nbsp;" + fieldName + "</div>";
}


function removeTableRow(tBodyElement) {

	pos = findSelectedRow(tBodyElement);
	if (pos < tBodyElement.rows.length)  {
		tBodyElement.deleteRow(pos);   	
	}
   	if (tBodyElement.rows.length == 0) {
   		clearEntireTable(tBodyElement);
   	}
}

function clearEntireTable(tBodyElement) {

  size = tBodyElement.rows.length;
  for (i=0; i < size;i++) {
     tBodyElement.deleteRow(0);
  }

  row = tBodyElement.insertRow(0);
  row.height=20; 
  cell = row.insertCell(0); 
  cell.colSpan="2";
  cell.innerHTML = "<div class=\"text8\">&nbsp;<bean:message key="text.noConditionsSelected" bundle="queries"/></div>";
}

function addField(tBodyElement) {

	matchValue = document.getElementById('matchValue');	
	if (matchValue.value.length <= 0) {
		return;
	}
	matchValue = matchValue.value;

    fieldBox = -1;
    fieldRadioList = document.getElementsByName('selectedField');
    hiddenRadioList = document.getElementsByName('hiddenSelectedField');
    for (i=0; i < fieldRadioList.length; i++) {
        if (fieldRadioList[i].checked) {
                fieldBox = i;
        }
    }
    if (fieldBox < 0) {
        return;
    }

    operationRadioList = document.getElementsByName('operation');
    for (i=0; i < operationRadioList.length; i++) {
        if (operationRadioList[i].checked) {
            fieldValue = operationRadioList[i].value + "$" + fieldRadioList[fieldBox].value + "$" + matchValue;
            fieldName = hiddenRadioList[fieldBox].value + " " + operationRadioList[i].value;
            if (matchValue != "null") {
               fieldName += " " + matchValue;
            }
            addTableRow(tBodyElement, fieldName, fieldValue);
            return;
        }
    }
}


function gotoNextStep(tBodyElement) {

	for (i=0; i < tBodyElement.rows.length; i++) {
    	if (tBodyElement.rows[i].cells.length == 2) {
	    	hiddenbox = tBodyElement.rows[i].cells[0].childNodes[0];
	    	document.forms[0].appendChild(hiddenbox);
      	}
   	}
   	document.forms[0].submit();
}



function moveUp(bodyElement, rowPosition) {

   // do not need to check element at pos = 0
   if (rowPosition > 0) {
      bodyElement.insertBefore(bodyElement.rows[rowPosition], bodyElement.rows[rowPosition-1]);
      highlightRow(bodyElement, bodyElement.rows[rowPosition-1]);
   }
}

function moveDown(bodyElement, rowPosition) {

   // do not need to check element at pos = length
   if (bodyElement.rows.length > (rowPosition-1)) {
      bodyElement.insertBefore(bodyElement.rows[rowPosition+1], bodyElement.rows[rowPosition]);
      highlightRow(bodyElement, bodyElement.rows[rowPosition+1]);
   }
}

function runNow() {
	document.forms['selectOrderByForm'].runNow.value="true";
	gotoNextStep(document.getElementById('myTableContent'));
}

</script>

<body>


	<td height="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
	 	<tr valign="top"> 
        	<td height="15">
        		<table class="table" width="100%" align="center" cellpadding="0" cellspacing="0" >
              	<tr> 
                	<td height="100%" class="table-title"><bean:message key="text.listConditionsTitle" bundle="queries"/></td>
              	</tr>
            	</table>
            </td>
        </tr>
		<tr valign="top"> 
        	<td height="2"></td>
        </tr>
		<tr  valign="top">
			<td  height="100%" width="100%">	
			
				<table width="100%" height="100%" align="center"  class="table" cellpadding="3" cellspacing="0">


	<bean:define id="tableInfo" name="<%=RequestScopeConstants.REQUEST_QUERYTABLE_KEY%>" scope="request"/>

	<html:form action="/select-orderby" method="POST">
	
		<logic:iterate id="previousSelection" name="<%=RequestScopeConstants.REQUEST_QUERYFIELDLIST_KEY%>" scope="request">
			<input type="hidden" name="queryField" value="<bean:write name="previousSelection"/>">
		</logic:iterate>

		<bean:define id="tableName" name="tableInfo" property="name" type="java.lang.String"/>
		<html:hidden property="table" value="<%=tableName%>"/>

				<tr class="text8">
      				<td width="1%">&nbsp;</td>
      				<td>
        				<table width="100%" align="center" cellpadding="0" cellspacing="0">
      				    	<tr valign="top">
      							<td height="60" valign="middle" colspan="5" class="text8">
	      					    	<font class="text8b"><bean:message key="text.selectedTable" bundle="queries"/> : </font><bean:write name="tableInfo" property="displayName"/>
      							</td>
      						</tr>
      				    	<tr valign="top">
      							<td width="35%">      								
			      					<table width="100%" class="table" align="center" cellpadding="0" cellspacing="0">
 			      					    <tr height="20" class="table-column-title">
 			      					    	<td colspan="2"><bean:message key="text.availableFields" bundle="queries"/></td>
 			      					    </tr>
<%
				boolean isFirstColumn = true;
%>			      					
				<logic:iterate id="columnInfo" name="tableInfo" property="columns">
<%
					if (isFirstColumn) {
%>
						<tr>
							<td height="20" width="50%" class="text8">
								<input type="radio" name="selectedField" value="<bean:write name="columnInfo" property="key"/>"> <bean:write name="columnInfo" property="value"/> </input>
								<input type="hidden" name="hiddenSelectedField" value="<bean:write name="columnInfo" property="value"/>">
							</td>
<%
					} else {
%>
							<td height="20" width="50%" class="text8">
								<input type="radio" name="selectedField" value="<bean:write name="columnInfo" property="key"/>"> <bean:write name="columnInfo" property="value"/> </input>
								<input type="hidden" name="hiddenSelectedField" value="<bean:write name="columnInfo" property="value"/>">
							</td>
						</tr>
<%
					} 
					isFirstColumn = !isFirstColumn;
%>
				</logic:iterate>
				
<%
					//Prints last cell even without info to fill it
					if (! isFirstColumn) {
%>				
							<td width="50%" class="text8">
							</td>
						</tr>
<%
					} 
%>
			      					</table>									 
			      				</td>
			      				<td width="2%"></td>
			      				<td  width="35%">
			      					<table width="100%" class="table" align="center" cellpadding="0" cellspacing="0">
 			      					    <tr height="20" class="table-column-title">
 			      					    	<td colspan="3"><bean:message key="text.conditionalOp.title" bundle="queries"/></td>
 			      					    </tr>
			      						<tr height="20" class="text8">
			      							<td width="50%"><input type="radio" name="operation" onclick="javascript:enableWhenEnabled(document.getElementById('matchValue'))" 
			      							                       value="<%=QueryFunctions.NUMERIC_EQUAL%>" checked><bean:message key="text.conditionalOp.equal" bundle="queries"/></input></td>
			      							<td width="50%"><input type="radio" name="operation" onclick="javascript:enableWhenEnabled(document.getElementById('matchValue'))" 
			      							                       value="<%=QueryFunctions.NUMERIC_NOT_EQUAL%>"><bean:message key="text.conditionalOp.notEqual" bundle="queries"/></input></td>
			      						</tr>
			      						<tr height="20" class="text8">
			      							<td><input type="radio" name="operation" onclick="javascript:enableWhenEnabled(document.getElementById('matchValue'))" 
			      							                        value="<%=QueryFunctions.NUMERIC_GREATER%>"><bean:message key="text.conditionalOp.greater" bundle="queries"/></td>
			      							<td><input type="radio" name="operation" onclick="javascript:enableWhenEnabled(document.getElementById('matchValue'))" 
			      							                        value="<%=QueryFunctions.NUMERIC_GREATER_EQUAL%>"><bean:message key="text.conditionalOp.greaterOrEqual" bundle="queries"/></td>
			      						</tr>
			      						<tr height="20" class="text8">
			      							<td><input type="radio" name="operation" onclick="javascript:enableWhenEnabled(document.getElementById('matchValue'))" 
			      							                        value="<%=QueryFunctions.NUMERIC_LOWER%>"><bean:message key="text.conditionalOp.lower" bundle="queries"/></td>
			      							<td><input type="radio" name="operation" onclick="javascript:enableWhenEnabled(document.getElementById('matchValue'))" 
			      							                        value="<%=QueryFunctions.NUMERIC_LOWER_EQUAL%>"><bean:message key="text.conditionalOp.lowerOrEqual" bundle="queries"/></td>
			      						</tr>
			      						<tr height="20" class="text8">
			      							<td><input type="radio" name="operation" onclick="javascript:disableWhenEnabled(document.getElementById('matchValue'))" 
			      							                        value="<%=QueryFunctions.STRING_NULL%>"><bean:message key="text.conditionalOp.null" bundle="queries"/></td>
			      							<td><input type="radio" name="operation" onclick="javascript:disableWhenEnabled(document.getElementById('matchValue'))" 
			      							                        value="<%=QueryFunctions.STRING_NOT_NULL%>"><bean:message key="text.conditionalOp.notNull" bundle="queries"/></td>
			      						</tr>
			      						<tr height="20" class="text8">
			      							<td><input type="radio" name="operation" onclick="javascript:enableWhenEnabled(document.getElementById('matchValue'))" 
			      							                        value="<%=QueryFunctions.STRING_LIKE%>"><bean:message key="text.conditionalOp.like" bundle="queries"/></td>
			      							<td><input type="radio" name="operation" onclick="javascript:enableWhenEnabled(document.getElementById('matchValue'))" 
			      							                        value="<%=QueryFunctions.STRING_NOT_LIKE%>"><bean:message key="text.conditionalOp.notLike" bundle="queries"/></td>
			      						</tr>
			      					</table>
			      				</td>
			      				<td width="2%"></td>
			      				<td width="20%">
			      					<div align="right">
			      					<input type="text" class="input-field" width="100%" name="matchValue" id="matchValue"/><br>
			      					<a href="javascript:addField(document.getElementById('myTableContent'))" class="textBlue"> :: <bean:message key="text.addButton" bundle="queries"/> :: </a>&nbsp;&nbsp;
			      					</div>
			      				</td>			      				
				      		</tr>
				      	</table>
				    </td>
      				<td width="1%">&nbsp;</td>
				</tr>				
	
				<tr height="45"> 
			    	<td colspan="3">&nbsp;</td>
			    </tr>
			    
				<tr height="100%" valign="top"> 
      				<td width="1%">&nbsp;</td>
			    	<td colspan="2">
						<div id="myTableWrapper">
					    <table width="300" class="table" cellpadding="0" cellspacing="0">
   							<thead>
   								<tr class="table-column-title">
   									<td height="20" colspan="2" align="center"><bean:message key="text.whereClauses" bundle="queries"/></td>
   								</tr>
   							</thead>
   							<tbody id="myTableContent">
   							</tbody>
   						</table>
						</div>
						<table width="100%">
						<tr>
							<td valign="top">
								<div align="left">
									<a href="javascript:removeTableRow(document.getElementById('myTableContent'))" class="textBlue"> :: <bean:message key="text.removeButton" bundle="queries"/> :: </a>&nbsp;&nbsp;
									<a href="javascript:clearEntireTable(document.getElementById('myTableContent'))"  class="textBlue"> :: <bean:message key="text.removeAllButton" bundle="queries"/> :: </a>&nbsp;&nbsp;
									<a href="javascript:moveUp(document.getElementById('myTableContent'), findSelectedRow(document.getElementById('myTableContent')))" class="textBlue"> :: <bean:message key="text.moveUpButton" bundle="queries"/> :: </a>&nbsp;&nbsp;
									<a href="javascript:moveDown(document.getElementById('myTableContent'), findSelectedRow(document.getElementById('myTableContent')))" class="textBlue"> :: <bean:message key="text.moveDownButton" bundle="queries"/> :: </a>
								</div>
							</td>
						</tr>
						<tr>
							<td valign="bottom">
								<div align="right">
		  						    <html:link action="/list-queries" styleClass="textBlue"> :: <bean:message key="text.restart" bundle="general"/> :: </html:link>&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="javascript:gotoNextStep(document.getElementById('myTableContent'))" class="textBlue"> :: <bean:message key="text.continueButton" bundle="queries"/> :: </a>&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="javascript:runNow()" class="textBlue"> :: <bean:message key="text.runButton" bundle="queries"/> :: </a>&nbsp;&nbsp;
								</div>
							</td>
						</tr>
						</table>
			    	</td>
			    </tr>
			    
				<html:hidden property="runNow" value="false"/>
	</html:form>

<script language="Javascript">
	clearEntireTable(document.getElementById('myTableContent'));
</script>	

				</table>
			</td>
   		</tr>
   		</table>
   	</td>   	

