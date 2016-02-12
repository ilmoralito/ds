var UpdateUserRole = (function() {
    return {
        init: function() {
            $(':radio').on('change', function(event) {
                var data = $(this).data();

                $.ajax({
                    url: window.ajaxURL,
                    data: data,
                    dataType: 'json',
                    success: function(result) {
                        if (result.success) {
                            $('.alert').remove();

                            var messageBox = $("<p>", {class: 'alert alert-info', text: 'Role actualizado correctamente'});

                            $('.span8').append(messageBox);
                        }
                    },
                    error: function(xhr, ajaxOptions, error) {
                        console.log(xhr, ajaxOptions, error);
                    }
                });
            });
        }
    }
})();

UpdateUserRole.init();