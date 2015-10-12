var Roster = (function() {
  return {
    toggle: function(dataset, self) {
      $.ajax({
        url: 'addingOrRemovingUserCoordinationsOrClassrooms/',
        data: dataset,
        success: function(result) {
          var td = self.parent().parent().find('td:last-child');

          td.find('a').toggleClass('show hide');

          if (result.error) {
            swal('Accion cancelada', 'Usuario posee una coordinacion');
            self.attr('checked', true);
            td.find('a').addClass('show');
          }
        },
        error: function(xhr, ajaxOptions, thrownError) {
          console.log(xhr.status, ajaxOptions, thrownError);
        }
      });
    }
  };
})();

$(':checkbox').on('change', function() {
  var _this = $(this);
  var dataset = _this.data();

  dataset.state = _this.is(':checked');
  Roster.toggle(dataset, _this);
});

$("#rosterFilterBox").on("keyup", function(e) {
  var text = $(this).val().toLowerCase();
  var target = $(".target");

  target.each(function(index) {
    var self = $(this);
    var t = self.text().toLowerCase();

    if (t.indexOf(text) === -1) {
      self.parent().hide();
    } else {
      self.parent().show();
    }
  })
});
