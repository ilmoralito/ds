var DETAIL = (function() {
	return {
		init: function(data) {
			
		}
	}
})();

$("#detailsTrigger").on("click", function(e) {
	e.preventDefault();

	DETAIL.init($(this).data())
});
