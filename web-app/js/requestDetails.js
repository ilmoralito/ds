$("#details").on("click", function(e) {
	e.preventDefault();

	var data = $(this).data();

	$("#user").html(data.user);

	$("#classroom").html(data.classroom);

	if (data.internet) {
		$("#internet").html("<i class='icon-ok'></i>");
	}

	if (data.audio) {
		$("#audio").html("<i class='icon-ok'></i>");
	}

	if (data.screen) {
		$("#screen").html("<i class='icon-ok'></i>");
	}

	if (data.description) {
		$("#description").html(data.description);
	}
});
