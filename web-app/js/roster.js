var Roster = (function(){
	return {
		add: function(dataset) {
			$.ajax({
				url: "updateUserCoordination/",
				data: dataset,
				success: function(result) {
					if (result.error === "true") {
						alert("Operacion no permitida. Profesor " + result.fullName + " unica coordinacion: " + dataset.coordination);
					};
				},
				error: function(xhr, ajaxOptions, thrownError) {
					console.log(xhr.status, ajaxOptions, thrownError);
				}
			});
		}
	}
})()

$(":checkbox").on("change", function() {
	var _this = $(this),
			dataset = _this.data();

	dataset.state = _this.is(":checked");
	Roster.add(dataset);
})
