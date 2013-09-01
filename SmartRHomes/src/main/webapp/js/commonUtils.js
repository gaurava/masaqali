function fnParseIDForJQuery(id){
	var newID = id.replace(/#/g,"\\#");	//Parse #
	newID = newID.replace(/\(/g,"\\(");   	//Parse (
	newID = newID.replace(/\)/g,"\\)");		//Parse )
	return newID;
}

function fnReplaceString(sourceString, stringToReplace, newString){
	return sourceString.replace(new RegExp(stringToReplace, "g"), newString);
}

function fnGetFormattedDate(date, isMonthZeroBased){
	day = date.getDate();
	month = isMonthZeroBased ? (date.getMonth()+ 1) : date.getMonth();
	year = date.getFullYear();
	hour = date.getHours();
	minute = date.getMinutes();
	return day +"-" + month + "-" + year + " " + (hour < 10 ? "0" + hour : hour) + (minute < 10 ? "0" + minute : minute);
}

function fnFormateDateOnlyWithSlash(date){
	day = date.getDate();
	month = date.getMonth()+ 1;
	year = date.getFullYear();
	hour = date.getHours();
	minute = date.getMinutes();
	return day +"/" + month + "/" + year;
}

//parse it to send to server accepted way
function fnFormatCalenderDate(date){
	var newDateComponents = date.split(" ");
	return newDateComponents[0] + " " + newDateComponents[1] + (newDateComponents.length < 3 ? ":00" : ":" + newDateComponents[2]) + ":00";
}

function fnFormatDateWithSeparator(date,dateSperator, timeSeparetaor, dateTimeSeparator, requireSeconds, withCurrentTime, isMonthZeroBased){
	//dateSperator - to seperate date part ---> mm-dd-yy
	//timeSperator - to seperate date part ---> hh:ss
	//dateTimeSeparator - to Separate date and time component
	if(withCurrentTime){
		date = new Date();
	}
	day = date.getDate();
	month = (withCurrentTime || isMonthZeroBased) ? date.getMonth() + 1 : date.getMonth();
	year = date.getFullYear();
	hour = date.getHours();
	minute = date.getMinutes();
	second = date.getSeconds();
	
	dateComponent = day + dateSperator + month + dateSperator + year;
	timeComponent = (hour < 10 ? "0" + hour : hour) + timeSeparetaor + (minute < 10 ? "0" + minute : minute) + (requireSeconds ? (timeSeparetaor + (second < 10 ? "0" + second : second)) : "");
	
	var formattedDate = dateComponent +  dateTimeSeparator + timeComponent;
	return formattedDate;
		
}

function sendJSONAjaxRequest(isGet, url, requestData, callbackSuccessFunction, callbackErrorFunction){
	var argLength = arguments.length;
	var requestArgumets = arguments;
	$.ajax({
		type:isGet ? "GET" : "POST",
		url:url,
		data: requestData,
		dataType: "json",
		success: function (responseJSON) {
			if(responseJSON.requireLogin == "true"){
				showDialog(messages.error,messages.error, messages.timeOut, redirectToLogin, false);
				
			}else{
				//eval(callbackSuccessFunction + "(responseJSON,callBackData)");
				var args = new Array(); 
				args.push(responseJSON);
				
			    for(var i = 5; i < argLength; i++){
			        args.push(requestArgumets[i]);
			    }
				callbackSuccessFunction.apply(null,args);
			}
		},
		error: function(errorMessage){
			if(callbackErrorFunction != null){
				var args = new Array(); 
				args.push(responseJSON);
				
			    for(var i = 5; i < argLength; i++){
			        args.push(requestArgumets[i]);
			    }
			    callbackErrorFunction.apply(null,args);
			}else{
				alert(errorMessage);
			}
		}
	});
}

function redirectToLogin() {
	window.location.replace("");
}

function sendHTMLAjaxRequest(isGet, url, requestData, callbackSuccessFunction, callbackErrorFunction){
	var argLength = arguments.length;
	var requestArgumets = arguments;
	$.ajax({
		type:isGet ? "GET" : "POST",
		url:url,
		data: requestData,
		dataType: "html",
		success: function (response) {
			var args = new Array(); 
			args.push(response);
			
		    for(var i = 5; i < argLength; i++){
		        args.push(requestArgumets[i]);
		    }
			callbackSuccessFunction.apply(null,args);
		},
		error: function(errorMessage){
			if(callbackErrorFunction != null){
				var args = new Array(); 
				args.push(responseJSON);
				
			    for(var i = 5; i < argLength; i++){
			        args.push(requestArgumets[i]);
			    }
			    callbackErrorFunction.apply(null,args);
			}else{
				alert(errorMessage);
			}
		}
	});
}

function fnUpdateTableHeader(tableData, tableHeader){
	
}

/**
 * Function to display dialog message to user.
 * 
 * @param typeOfError
 * @param title
 * @param message
 * @param callbackFunctionName
 * @param requireCancelButton
 */
function showDialog(typeOfError,title,message,callbackFunctionName, requireCancelButton){
	var image = null;
	var classForMezBody = "mezBodyDialog";
	if (typeOfError == null) {
		classForMezBody = "mezBodyDialogSimple";
	}
	
	switch (typeOfError) {
	case messages.error:
		image="images/error.png";
		break;
	case messages.warning:
		image="images/warning.png";
		break;
	case messages.information:
		image="images/info.png";
		break;
	case messages.success:
		image="images/success.png";
		break;
	}
	
	$('#showDialogPopup').remove();
	var argLength = arguments.length;
	var requestArgumets = arguments;
	var dialogDiv=document.createElement("DIV");
	dialogDiv.setAttribute("id", "showDialogPopup");
	$(dialogDiv).dialog({
		modal : 'true',
		title:title,
		width:400
	});
	var messageDiv =document.createElement("div");
	messageDiv.setAttribute("style", "height:90px;width:100%;overflow:auto;");
	var div=document.createElement("img");
	if (typeOfError != null) {
		div.setAttribute("src", image);
		div.setAttribute("style", "float:left; padding:20px; width:30px;");
		messageDiv.appendChild(div);
	}
	div=document.createElement("DIV");
	div.setAttribute("class", classForMezBody);
	div.innerHTML=message;
	messageDiv.appendChild(div);
	dialogDiv.appendChild(messageDiv);
	var divx=document.createElement("DIV");
	divx.setAttribute("style", "height:18px;width:100%;border-top:solid 1px #A19866;");
	dialogDiv.appendChild(divx);
	div=document.createElement("DIV");
	div.setAttribute("style", "float:right;padding-right:10px;padding-top:5px;");
	var element = null;
	if (requireCancelButton) {
		element=document.createElement("Input");
		element.setAttribute("type", "button");
		element.setAttribute("class", "button");
		element.setAttribute("value", "Yes");
		element.style.marginRight="5px";
		element.onclick=function(){
			if(!(callbackFunctionName==null)){
				var args = []; 
			    for(var i = 5; i < argLength; i++){
			        args.push(requestArgumets[i]);
			    }
			    callbackFunctionName.apply(null,args);	
			}
			$(dialogDiv).dialog("close");
		};
		div.appendChild(element);
		element=document.createElement("Input");
		element.setAttribute("type", "button");
		element.setAttribute("class", "button");
		element.setAttribute("value", "No");
		element.onclick=function(){
			$(dialogDiv).dialog("close");
		};
		div.appendChild(element);
	}else {
		element=document.createElement("Input");
		element.setAttribute("type", "button");
		element.setAttribute("class", "button");
		element.setAttribute("value", "OK");
		element.onclick=function(){
		  	if(!(callbackFunctionName==null)){ 
				var args = []; 
			
				for(var i = 5; i < argLength; i++){
					args.push(requestArgumets[i]);
				}
				callbackFunctionName.apply(null,args); 
			}
		  	$(dialogDiv).dialog("close"); 
		};
		div.appendChild(element);
	}
	divx.appendChild(div);
}

function fnDateConvert(dateValue, dateTimeSeparator, dateSeparator, timeSeparator) {
	var finalDate= new Date;
	var subdate=dateValue.split(dateTimeSeparator == undefined ? ' ' : dateTimeSeparator);
	var date=subdate[0].split(dateSeparator == undefined ? '-' : dateSeparator);
	finalDate.setFullYear(date[2], (+date[1]) - 1, date[0]);
	if(timeSeparator == undefined){
		finalDate.setHours(subdate[1].substring(0, 2),subdate[1].substring(2, 4),subdate[1].substring(4, 6), '00');
	}else{
		var time = subdate[1].split(timeSeparator);
		finalDate.setHours(time[0], time[1], time[2], '00');
	}	
	
		
	return finalDate;	
}

function fnGetColumText($columnRef){
	var title = $columnRef.attr('title');
	return (title != undefined && title != null && title.length > 0) ? title : $columnRef.text();
}

//show hoverbox
function hoverbox_show(obj, src, seprator,top,left) {
	var hoverDiv = $('#hoverbox');
    var oTop = 20, oLeft = 40;
    if(top)oTop=top;
    if(left)oLeft=left;
    hoverDiv.css({top : $(obj).offset().top+oTop, left : $(obj).offset().left+oLeft});
		var str='';
		if(seprator){
			for(var key in src.split(seprator))
			{
				str+=src.split(seprator)[key]+'<br>';
			} 
		}else{
		str=src;
		}
		hoverDiv.html(str);
		hoverDiv.css('visibility','visible').show();
	setTimeout('hoverbox_hide()',2000);
}
  
// hide hoverbox (hide div element)
function hoverbox_hide() {
    $('#hoverbox').css('visibility','hidden').hide();
}

/**
 * Function to show loading Image while request is processing.
 * 
 * @param panel
 * @param topPosition
 * @param leftPosition
 */
function setLoadImage(panel, topPosition, leftPosition) {
	var div = document.getElementById(panel);
	div.innerHTML = "";
	img = document.createElement("img");
	img.setAttribute('src', 'images/loading.gif');
	img.style.marginTop = topPosition;
	img.style.marginLeft = leftPosition;
	div.appendChild(img);
}

/**
 * Function to show small Success message to user only for specific time 
 * 
 * @param typeOfError
 * @param message
 * @param time
 * @param mid
 */
function showDialogTimmer(typeOfError,message,time,mid){
	var image = "";
	switch (typeOfError) {
	case messages.success:
		image="images/success.png";
		break;
	}
	var smallDialogDiv=$('<div></div>');
	var contentDiv=$('<div></div>');
	var str="<span style='width: 24px;'>";
	if(image!=null){
	str ="<img style='float:left;padding-left:22px; width:24px;' src='"+image+"'>";
	}
	var y = window.innerHeight - 150;
	var x = window.innerWidth - 1130;
	if(mid!=null){
		y=window.innerHeight/2;
		x=window.innerWidth - 800;
	}
	contentDiv.html(str+'<div style=\'padding-left:60px; padding-top:8px; text-align:left;\'>'+message+'<div>');
	contentDiv.addClass('dialogContentBubble');
	smallDialogDiv.append(contentDiv);
	smallDialogDiv.dialog({
			//show:{effect:'fade',duration:1000},
			//hide:{effect:'fade',duration:1000},
			height: 75,
			//position: ['left','bottom'],
			position: [x,y],
			open : function() {
				smallDialogDiv.parent().find('div:eq(0)').css('background-color','#A49B70');
				smallDialogDiv.parent().find('div:eq(0)').css('height','0px');
				smallDialogDiv.parent().find('div:eq(0)').find('span:eq(0)').remove();
				smallDialogDiv.parent().find('div:eq(0)').find('a:eq(0)').css('margin','0px');
				smallDialogDiv.parent().find('div:eq(0)').find('a:eq(0)').find('span:eq(0)').css('color','black');
				smallDialogDiv.parent().find('div:eq(0)').find('a:eq(0)').find('span:eq(0)').css('background-image','none');
				smallDialogDiv.parent().css('border','3px solid black');
				smallDialogDiv.css('background-color','#A49B70');
				smallDialogDiv.dialog('open');
			}
		});
	
	setTimeout(function() {
		smallDialogDiv.dialog('close');
	}, time);
}

function logout(linkID,href){
	$("#"+linkID).click(function(){window.location.replace(href);});
}