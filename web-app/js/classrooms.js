var Classroom = (function() {
	return {
		addOrRemove: function(dataset, self) {
			$.ajax({
				url: window.ajaxURL,
				data: dataset,
				success: function(result) {
					console.log(result)
				},
				error: function(xhr, ajaxOptions, thrownError) {
					console.log(xhr.status, ajaxOptions, thrownError);
				}
			});
		}
	}
})();

$(":checkbox").on("change", function() {
	var _this = $(this),
			dataset = _this.data();

	dataset.state = _this.is(":checked");

	Classroom.addOrRemove(dataset, _this);
});