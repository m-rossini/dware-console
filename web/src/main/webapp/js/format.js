	
var isNav4, isNav, isIE;
var keyCode;

if (parseInt(navigator.appVersion.charAt(0)) >= 4) {
  isNav = (navigator.appName=="Netscape") ? true : false;
  isIE = (navigator.appName.indexOf("Microsoft") != -1) ? true : false;
}

if (navigator.appName=="Netscape") {
	isNav4 = (parseInt(navigator.appVersion.charAt(0))==4);
}


function clearField(field) {
		field.value = '';
}

function removeFormat(str) {
   str2 = str.replace('\.', '');
   str2 = str2.replace(',', '.'); 
   if (str2.length == 0) {
   	   return 'NaN';
   }
   return str2;
}
	
function storeKeyPressed(e){
	keyCode = (isNav) ? e.which : event.keyCode;
	if (isIE) {
		switch (keyCode) {
			// backspace
			case 8:
			// delete
			case 46:
				event.srcElement.value = formatMonetaryAmount(event.srcElement.value.substring(0,event.srcElement.value.length-1));
				return false;
		}
	}
}


//
//  --- Functions to format monetary amounts
//

function displayFormattedMonetaryAmount(e) {
   var obj;
   //verificando se o que foi digitado é um número
   if (!isOnlyNumber(e)){
      return false;
   }
	 //e.style.textAlign	= "right";
	 obj   = (isNav) ? e.target : event.srcElement;
	 keyCd = (isNav) ? e.which : event.keyCode;

	 switch (keyCode) {
	    // backspace
			case 8:
			// delete
			case 46:
			   obj.value = formatMonetaryAmount(obj.value.substring(0,obj.value.length-1));
				 break;
			// tab
			case 9:
			   return true;
				 break;
			default :
			   if ((keyCd>47)&&(keyCd<58)) {
				    if (obj.maxLength>obj.value.length){
						   obj.value = formatMonetaryAmount(obj.value + String.fromCharCode(keyCd));
						}
				 }	
   }
	 return false;
}

function formatMonetaryAmount(str){
   var decimal,inteiro;
   var i,count;
   STR = new String(str);
   STR = removeHeadingZeros(STR);
   inteiro='';
	 if (STR.length == 1){
      inteiro  = '0';
			decimal = '0' + STR;
	 } else { 
      if (STR.length == 2){
			   inteiro  = '0';
				 decimal = STR;
			}	else {
			   decimal = STR.substring(STR.length-2,STR.length);
			   i=3;
			   count=0;
			   while (i<=STR.length){
			      if (count==3) {
					     inteiro = '.' + inteiro;
					     count = 0;
					  }
				    inteiro = STR.charAt(STR.length-i) + inteiro;
					  count++;
					  i++;
				 }
			}
	 }
	 if (inteiro == '') {
      inteiro = '0';
	 }
	 if (decimal == '') {
	    decimal = '00';
	 }
	 return inteiro+','+decimal;
}

//
//  --- Functions to format integer amounts
//


function displayFormattedIntegerAmount(e) {
		var obj;
		//verificando se o que foi digitado é um número
		if (!isOnlyNumber(e)){
			return false;
		}
		obj   = (isNav) ? e.target : event.srcElement;
		keyCd = (isNav) ? e.which : event.keyCode;
		switch (keyCode) {
			case 8:
			case 46:
				obj.value = formatIntegerAmount(obj.value.substring(0,obj.value.length-1));
				break;
			case 9:
				return true;
				break;
			default :
					if ((keyCd>47)&&(keyCd<58)) {
						if (obj.maxLength>obj.value.length){
						obj.value = formatIntegerAmount(obj.value + String.fromCharCode(keyCd));
						}
					}	
		}
		return false;
}

function formatIntegerAmount(str){
  var inteiro;
  var i,count;
  STR = new String(str);
  STR = removeHeadingZeros(STR);
  inteiro='';
  if (STR.length < 4) {
  	 inteiro  = STR;
  } else { 
	  i=1;
  	count=0;
	  while (i <= STR.length){
	    if (count==3) {
		    inteiro = '.' + inteiro;
  		  count = 0;
	    }
      inteiro = STR.charAt(STR.length-i) + inteiro;
      count++;
	    i++;
    }
  }
  if (inteiro == '') {
  	inteiro = '0';
  } 
  return inteiro;
}

//
//  --- Helper functions for formatters
//

function removeHeadingZeros(STR){
	var sAux = '';
	STR = new String(STR);  
	var i = 0;
	while (i < STR.length ){
		if ((STR.charAt(i)!='.') && (STR.charAt(i)!=',')){
			sAux += STR.charAt(i);
		}
		i++
	}
  STR = new String(sAux);
  sAux = '';
  i = 0;
  while (i < STR.length ){
    if (STR.charAt(i) != '0'){
      sAux = STR.substring(i,STR.length)
	  i = STR.length;
	}
    i++;
  }
  return  sAux;
}

function isOnlyNumber(e) {
	var keyNumber = (isIE) ? event.keyCode : e.which;
	if (((keyNumber<48)||(keyNumber>57)) && (keyNumber!=13) && (keyNumber!="0") && (keyNumber!=8)) {
		if (isIE) {
			event.keyCode=0
		}
		return false;
	}
	return true;
}


function validateAmounts(_upperAmount, _lowerAmount, _errorMessage) {
   // checking if upper value limit is a number
   upperValue = new Number(removeFormat(_upperAmount.value));
   lowerValue = new Number(removeFormat(_lowerAmount.value));
   // if both limits are set, then upper > lower
   if ( (!isNaN(lowerValue)) && (!isNaN(upperValue)) ) {
      if (lowerValue > upperValue) {
         window.alert(_errorMessage);
         return false;
      }
   }
   // if all are OK, then update fields with values without format
   if (!isNaN(upperValue)) {						
      _upperAmount.value = upperValue;
   }
   if (!isNaN(lowerValue)) {
      _lowerAmount.value = lowerValue;
   }	
   return true;				
}
				

