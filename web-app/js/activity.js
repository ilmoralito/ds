(function(){
	$(".act").on("click", function(e) {
		e.preventDefault();

		$(this).next(".create-request-from-activity").toggle();
	})
})();