var mid = "";
var mDate = "";

$(function() {
	sendJSONAjaxRequest(true, "data/meterIds?p=watermeter", "", fnPopulateMeterId, null);
	$('#GO').click(function(){
		sendJSONAjaxRequest(true, "data/meterIdData?p=watermeter&mid="+mid+"&date="+mDate, "", fnPopulateMeterIdData, null);
	});
	$('#meters').change(function(){
		mid=$('#meters option:selected').val();
	});
	$('#meteringDate').change(function(){
		mDate=$('#meteringDate').val();
	});

});

function fnPopulateMeterId(responseJson){
	for(key in responseJson){
//		$('#meters').append(new Option(responseJson[key], responseJson[key]));	//THis is also correct
		$('<option>').val(responseJson[key]).text(responseJson[key]).appendTo('#meters');
	}
}


function fnPopulateMeterIdData(responseJson){
	if(responseJson.length>0){
		for(key in responseJson){
			$('#meterData').append('<div class="left">'+responseJson[key].startTime+'</div>');
			$('#meterData').append('<div class="left">'+responseJson[key].duaration+'</div>');
			$('#meterData').append('<div class="right">'+responseJson[key].consumption+'</div>');
		}
	}
	else{
		alert("No Data.");
	}
}
