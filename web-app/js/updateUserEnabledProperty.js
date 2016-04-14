$(function() {
    $('#enabled').on('click', function() {
        var $this = $(this);
        var data = $this.data();
        var url = window.ajaxURI;

        $.ajax({
            url: url,
            data: data,
            success: function(result) {
                if (result.status === 'success') {
                    var messageBox = $("<p>", {class: 'alert alert-info', text: 'Estado actualizado correctamente'});

                    $('.alert').remove();
                    $('.span8').append(messageBox);
                }
            },
            error(xhr, ajaxOptions, error) {
                console.log(xhr, ajaxOptions, error);
            }
        })
    });
});