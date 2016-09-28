$(document).ready(function() {
    var user = $('#user');
    var classroom = $('#classroom');
    var internet = $('#internet');
    var media = $('#media');
    var trigger = $('.trigger');

    var hasClassroomWifi = function(wifi) {
        if (wifi === false) {
            internet.parent().hide();
            internet.prop('checked', false);
        } else {
            internet.parent().show();
        }
    }

    user.on('change', function() {
        var option = $(this).find('option:selected');
        var classrooms = option.data().classrooms;

        classroom.find('option').remove();

        $.each(classrooms, function(index, element) {
            var option = $('<option>', { value: element.code, text: element.name || element.code, 'data-wifi': element.wifi || false });

            classroom.append(option);
        });

        hasClassroomWifi(classroom.find('option').first().data().wifi);
    });

    classroom.on('change', function() {
        var wifi = $(this).find('option:selected').data().wifi;

        hasClassroomWifi(wifi);
    });

    hasClassroomWifi(classroom.find('option').first().data().wifi);

    trigger.on('click', function(e) {
        $('#datashow').val($(this).data().datashow)
    })
});
