var SchoolsDepartments = (function() {
  return {
    choose: function() {
      $(this).next().find('option').attr('selected', 'selected');
    }
  };
})();

$('#coord, #depart').on('click', SchoolsDepartments.choose);

$('.action-delete').on('click', function(event) {
    if (!confirm('Si eliminas este registro no podras deshacer los cambios. Deseas continura?')) {
        event.preventDefault();
    }
});
