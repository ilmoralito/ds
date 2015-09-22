$("#details").on("click", function(e) {
	e.preventDefault();

	var data = $(this).data();

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
