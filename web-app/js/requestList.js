(function(){
    var trigger = $('#trigger');
    var triggers = $('.trigger');

    trigger.on('click', function() {
        $('.requests').attr('checked', $(this).is(':checked'));
    });

    triggers.on('click', function() {
        $('#newStatus').val($(this).data('status'));
    });
})();
