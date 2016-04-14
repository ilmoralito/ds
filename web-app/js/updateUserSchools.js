var UpdateUserSchools = (function() {
    var updateSchoolsTable = $('#updateSchoolsTable'),
        userSchoolsTable = $('#userSchoolsTable'),
        toggleSchools = $('#toggleSchools');

    return {
        init: function() {
            toggleSchools.on('click', function() {
                updateSchoolsTable.toggle();
                userSchoolsTable.toggle();
            });
        },

        update: function(data) {
            $.ajax({
                url: window.ajaxPATH,
                data: data,
                success: function(result) {
                    if (data.checked) {
                        var tr = $('<tr>'),
                            td = $('<td>', {text: result.coordination});

                        tr.append(td);
                        userSchoolsTable.append(tr);
                    } else {
                        userSchoolsTable.find('tr td').each(function() {
                            var context = $(this);

                            if (context.text() == result.coordination) {
                                context.parent().remove();
                            }
                        });
                    }
                },
                error: function(xhr, ajaxOptions, error) {
                    console.error(xhr, ajaxOptions, error);
                }
            });
        }
    }
})();

UpdateUserSchools.init();

$('#updateSchoolsTable').find(':checkbox').on('change', function() {
    var context = $(this),
        data = context.data();

    data.checked = context.is(':checked');

    UpdateUserSchools.update(data);
});
