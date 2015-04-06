var SchoolsDepartments = (function() {
	return {
		choose: function() {
			$(this).next().find("option").attr("selected", "selected");
		}
	}
})();

$("#coord, #depart").on("click", SchoolsDepartments.choose);