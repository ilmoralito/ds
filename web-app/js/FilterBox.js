$("#rosterFilterBox").on("keyup", function(e) {
  var text = $(this).val().toLowerCase();
  var target = $(".target");

  target.each(function(index) {
    var self = $(this);
    var t = self.text().toLowerCase();

    if (t.indexOf(text) === -1) {
      self.closest("tr").hide();
    } else {
      self.closest("tr").show();
    }
  })
});