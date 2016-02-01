var RequestDetail = (function() {
    var trigger = $('#details');
    var targets = {
        user: $('#user'),
        classroom: $('#classroom'),
        internet: $('#internet'),
        audio: $('#audio'),
        splash: $('#screen'),
        description: $('#description')
    };

    return {
        init: function() {
            trigger.on('click', function( event) {
                event.preventDefault();

                data = $(this).data();
                
                targets.user.html(data.user);
                targets.classroom.html(data.classroom);

                if (data.internet) {
                    targets.internet.parent().show()
                    targets.internet.html("<i class='icon-ok'></i>");
                }

                if (data.audio) {
                    targets.audio.parent().show()
                    targets.audio.html("<i class='icon-ok'></i>");
                }

                if (data.screen) {
                    targets.splash.parent().show()
                    targets.splash.html("<i class='icon-ok'></i>");
                }

                if (data.description) {
                    targets.description.parent().show()
                    targets.description.html(data.description);
                }
            });
        }
    }
})();

RequestDetail.init();
