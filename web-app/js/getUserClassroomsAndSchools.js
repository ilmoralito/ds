$('#user').on('change', function() {
  var _this = $(this);
  var userEmail = _this.val();

  $.ajax({
    url: window.ajaxURL,
    data: {userEmail: userEmail},
    dataType: 'JSON',
    success: function(data) {
      var userClassrooms = data.classrooms;
      var userSchools = data.schools;
      var classroom = $('#classroom');
      var school = $('#school');

      classroom.find('option').remove();
      school.find('option').remove();

      for (var i = 0; i < userClassrooms.length; i++) {
        var option = $('<option>', {
          value: userClassrooms[i].code,
          text: userClassrooms[i].name
        });

        classroom.append(option);
      }

      for (var j = 0; j < userSchools.length; j++) {
        var opt = $('<option>', {
          value: userSchools[j],
          text: userSchools[j]
        });

        school.append(opt);
      }
    },
    error: function(xhr, ajaxOptions, thrownError) {
      console.log(xhr.status, thrownError);
    }
  });
});
