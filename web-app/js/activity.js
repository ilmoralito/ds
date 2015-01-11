var drag = function(event) {
	event.dataTransfer.setData("text", event.target.id);
	event.dataTransfer.effectAllowed = "move";
}

var allowDrop = function(event) {
	event.preventDefault();
}

var dragend = function(event) {
	var origin = event.target;

	origin.innerHTML = "";
	origin.classList.remove("currentUser");

	origin.setAttribute("id", "");
	origin.setAttribute("draggable", false);
	origin.setAttribute("ondragstart", "");
	origin.setAttribute("ondragover", "allowDrop(event)");
	origin.setAttribute("ondrop", "drop(event)");
}

var drop = function(event) {
	event.preventDefault();

	var id = event.dataTransfer.getData("text"),
			datashow = event.target.dataset.datashow,
			block = event.target.dataset.block;

	$.ajax({
		url:"todo",
		type:"POST",
		data:{ id:id, datashow:datashow, block:block },
		success:function(data) {
			var target = event.target;

			target.innerHTML = "";
			target.classList.add("currentUser");
			target.setAttribute("id", id);
			target.setAttribute("draggable", true);
			target.setAttribute("ondragstart", "drag(event)");
			target.setAttribute("ondragover", "");
			target.setAttribute("ondrop", "");

			var p = document.createElement("p"),
					fullName = document.createTextNode(data.fullName),
					classroom = document.createTextNode(data.classroom);

			p.appendChild(fullName)
			target.appendChild(p)
			target.insertBefore(classroom, p.nextSibling);
		},
		error:function(xhr, status, error) {
			console.log(xhr, status.status, error.toString())
		}
	})
}
