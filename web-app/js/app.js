//create request
$('#dateOfApplication').datepicker({
	dateFormat: "yy-mm-dd",
	minDate:0
});

//request disponabilty
$('#dateSelected').datepicker({
	dateFormat: "yy-mm-dd"
});

//request list
$('#requestFromDate').datepicker({
	dateFormat: "yy-mm-dd"
});

$('#requestToDate').datepicker({
	dateFormat: "yy-mm-dd"
});

$('#popover').popover()