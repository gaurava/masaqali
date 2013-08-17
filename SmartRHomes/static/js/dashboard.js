var mid = "";
var mDate = "";
var nid = "";
var nDate = "";

$(function() {
	sendJSONAjaxRequest(true, "data/meterIds?p=watermeter", "", fnPopulateMeterId, null);
	sendJSONAjaxRequest(true, "data/nucliousIds", "", fnPopulateNucliousId, null);
	$('#GetMeterData').click(function(){
		fnReCreateMeterTable();
		sendJSONAjaxRequest(true, "data/meterIdData?p=watermeter&mid="+mid+"&date="+mDate, "", fnPopulateMeterIdData, null);
	});
	$('#meters').change(function(){
		mid=$('#meters option:selected').val();
	});
	$('#meteringDate').change(function(){
		mDate=$('#meteringDate').val();
	});

	$('#GetNucliousData').click(function(){
//		fnReCreateNWMDTable();
		sendJSONAjaxRequest(true, "data/NWMDData?nid="+nid+"&date="+nDate, "", fnPopulateNucliousMeterWideData, null);
	});
	$('#nuclious').change(function(){
		nid=$('#nuclious option:selected').val();
	});
	$('#nucliousDate').change(function(){
		nDate=$('#nucliousDate').val();
	});
	
});

function fnPopulateMeterId(responseJson){
	for(key in responseJson){
//		$('#meters').append(new Option(responseJson[key], responseJson[key]));	//THis is also correct
		$('<option>').val(responseJson[key]).text(responseJson[key]).appendTo('#meters');
	}
}

function fnReCreateMeterTable(){
	$('#meterData').html("");
	$('#meterData').append('<div class="left header">StartTime</div>');
	$('#meterData').append('<div class="left header">Duration</div>');
	$('#meterData').append('<div class="right header">Consumption</div>');
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
		//TODO	Custom message Box
		alert("No Data.");
	}
}

function fnPopulateNucliousId(responseJson){
	for(key in responseJson){
		$('<option>').val(responseJson[key]).text(responseJson[key]).appendTo('#nuclious');
	}
}

function fnRecreateNWMDTable(){
	
}

function fnPopulateNucliousMeterWideData(responseJson){
//	[{"meterId":101,"previousDateTime":null,"latestDateTime":null,"previousQty":0,"latestQty":0,"diff":0,"sumOfConsumption":716}]
	$('#nwmdTable tr:gt(1)').remove();
	if(responseJson.length>0){
		for(key in responseJson){
			fnFillNWMDData(responseJson);
//			fx(responseJson);
		}
	}
	else{
		//TODO Custom message Box
		alert("No Data.");
	}	

}

/*function fx(responseJson){
	$('#nwmdTable').append($('<tr>')
	.append('<td>'+responseJson[key].meterId+'</td>')
	.append('<td width="80">'+responseJson[key].latestDateTime+'</td>')
	.append('<td width="80">'+responseJson[key].previousDateTime+'</td>')
	.append('<td width="80">'+responseJson[key].latestQty+'</td>')
	.append('<td width="80">'+responseJson[key].previousQty+'</td>')
	.append('<td width="80">'+responseJson[key].diff+'</td>')
	.append('<td width="80">'+responseJson[key].sumOfConsumption+'</td>')
	.append('<tr>'));

}*/

function fnFillNWMDData(responseJson) {
	var table=document.getElementById("nwmdTable");
	var newRow=document.createElement("TR");
	
	newCell=document.createElement("TD");
	newCell.innerHTML=responseJson[key].meterId;
	newCell.setAttribute("width", "80");
	newRow.appendChild(newCell);
	
	newCell=document.createElement("TD");
	newCell.innerHTML=responseJson[key].latestDateTime;
	newCell.setAttribute("width", "80");
	newRow.appendChild(newCell);
	
	newCell=document.createElement("TD");
	newCell.innerHTML=responseJson[key].previousDateTime;
	newCell.setAttribute("width", "80");
	newRow.appendChild(newCell);
	
	newCell=document.createElement("TD");
	newCell.innerHTML=responseJson[key].latestQty;
	newCell.setAttribute("width", "80");
	newRow.appendChild(newCell);
	
	newCell=document.createElement("TD");
	newCell.innerHTML=responseJson[key].previousQty;
	newCell.setAttribute("width", "80");
	newRow.appendChild(newCell);
	
	newCell=document.createElement("TD");
	newCell.innerHTML=responseJson[key].diff;
	newCell.setAttribute("width", "80");
	newRow.appendChild(newCell);
	
	newCell=document.createElement("TD");
	newCell.innerHTML=responseJson[key].sumOfConsumption;
	newCell.setAttribute("width", "80");
	newRow.appendChild(newCell);
	
	table.appendChild(newRow);
}

