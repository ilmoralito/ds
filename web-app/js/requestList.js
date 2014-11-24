(function(){
	var trigger = $("#trigger")

	trigger.on("click", function() {
		$(".requests").attr("checked", $(this).is(":checked"))
	})
})()