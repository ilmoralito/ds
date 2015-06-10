$("#user").on("change", function() {
	var _this = $(this),
			userEmail = _this.val();

	$.ajax({
		url: "getUserClassroomsAndSchools/",
		data: { userEmail: userEmail },
		dataType: "JSON",
		success: function(data) {
			var userClassrooms = data.classrooms;
					userSchools = data.schools,
					classroom = $("#classroom"),
					school = $("#school");

			classroom.find("option").remove();
			school.find("option").remove();

			for (var i = 0; i < userClassrooms.length; i++) {
				var option = $("<option>", { value: userClassrooms[i], text: userClassrooms[i] });

				classroom.append(option);
			};

			for (var i = 0; i < userSchools.length; i++) {
				var option = $("<option>", { value: userSchools[i], text: userSchools[i] });

				school.append(option);
			};
		}
	})
});