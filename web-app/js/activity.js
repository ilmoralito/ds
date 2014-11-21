(function(){
	var well = $(".well")

	well.on("click", function(e) {
		var block = $(this).data("block")

		well.css({"border-color":"#E3E3E3"})

		$("." + block).css({"border-color":"#f92c73"})
	})
})();