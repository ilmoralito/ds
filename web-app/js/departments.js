if (!Array.prototype.contains) {
		Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
      if (this[i] === obj) {
        return true;
      }
    }
    return false;
	}
}

(function(){
	$("#departments").on("click", function(e) {
		e.preventDefault();

		var departments = $(this).data("departments").split(",");

		$("#schools option").each (function(index) {
			var _this = $(this);

			if (departments.contains(_this.text())) {
				_this.attr("selected", "selected")
			};
		})
	})
})()
