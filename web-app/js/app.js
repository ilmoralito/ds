//create request
$('#dateOfApplication').datepicker({
  dateFormat: 'yy-mm-dd',
  minDate: ($('#type').val() == 'common') ? +3 : 0
});

//request disponabilty
$('#dateSelected').datepicker({
  dateFormat: 'yy-mm-dd'
});

//multiple requests
$('#date').datepicker({
  dateFormat: 'yy-mm-dd',
  minDate:0
});

$('#fromDate').datepicker({
  dateFormat: 'yy-mm-dd',
  minDate:0
});

$('#toDate').datepicker({
  dateFormat: 'yy-mm-dd',
  minDate:0
});

$('.showAndHideDetail').on('click', function() {
  $(this).next('.detail').slideToggle();
});

//request list
$('#requestFromDate').datepicker({
  dateFormat: 'yy-mm-dd'
});

$('#requestToDate').datepicker({
  dateFormat: 'yy-mm-dd'
});

$('#popover').popover({html:true});
