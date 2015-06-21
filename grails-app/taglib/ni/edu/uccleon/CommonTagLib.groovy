package ni.edu.uccleon

class CommonTagLib {
	static namespace = "ds"
	def requestService

	def flashMessage = {attrs, body ->
		if (flash.message) {
			out << body()
		}
	}

	def isEnabled = { attrs ->
		if (attrs.status) {
			out << "Habilitado"
		} else {
			out << "Deshabilitado"
		}
	}

	def classroom = { attrs ->
		def classrooms = requestService.mergedClassrooms() as List
		out << classrooms.find { it.code == attrs.room }.name
	}

	def isAdmin = {attrs, body ->
		if (session?.user?.role == "admin") {
			out << body()
		}
	}

	def isUser = { attrs, body ->
		if (session?.user?.role != "admin") {
			out << body()
		}
	}

	def isAcademic = { attrs, body ->
		if (session?.user?.role in ["coordinador", "asistente"]) {
			out << body()
		}
	}

	def requestStatus = {attrs, body ->
		def status = attrs.status

		if (status == "pending") {
			out << "Pendiente"
		} else if (status == "attended") {
			out << "Atendido"
		} else if (status == "absent") {
			out << "Ausente"
		} else if (status == "canceled") {
			out << "Cancelado"
		}
	}

	def renderTitle = { attrs ->
		switch(attrs.title) {
			case "schools":
				out << "Facultades"
			break
			case "classrooms":
				out << "Aulas"
			break
			case "users":
				out << "Usuarios"
			break
			case "datashows":
				out << "Datashows"
			break
			case "blocks":
				out << "Bloques"
			break
		}
	}

	def blockToHour = { attrs, body ->
		def block = attrs.int("block")
		def doapp = attrs.int("doapp") //day of Application

		//sunday
		if (doapp == 1) {
			if (block == 1) {
				out << "08:00 - 09:10"
			}

			if (block == 2) {
				out << "09:10 - 10:40"
			}

			if (block == 3) {
				out << "10:40 - 12:00"
			}
		}

		//saturday
		if (doapp == 7) {
			if (block == 1) {
				out << "08:00 - 10:00"
			}

			if (block == 2) {
				out << "10:00 - 12:00"
			}

			if (block == 3) {
				out << "01:40 - 02:15"
			}

			if (block == 4) {
				out << "02:15 - 03:30"
			}
		}

		//weekday
		if (doapp >= 2 && doapp < 7) {
			if (block == 1) {
				out << "08:00 - 09:10"
			}

			if (block == 2) {
				out << "09:10 - 10:20"
			}

			if (block == 3) {
				out << "10:40 - 11:45"
			}

			if (block == 4) {
				out << "12:00 - 01:15"
			}

			if (block == 5) {
				out << "01:15 - 02:25"
			}

			if (block == 6) {
				out << "02:25 - 03:35"
			}

			if (block == 7) {
				out << "03:40 - 05:00"
			}
		}
	}

	def message = { attrs, body ->
		def req = attrs.request
		def blocks = attrs.blocks

		if (session?.user?.role == "admin") {
			out << "${req.user.fullName}, ${req.classroom}, bloques ${blocks}"
		} else {
			out << "${req.dateOfApplication.format('yyyy-MM-dd')}, ${req.classroom}, ${blocks}"
		}
	}

	def countByStatus = { attrs ->
		def result = Request.listByUser(session?.user).countByStatusNotEqual(attrs.status)

		if (result) {
			out << result
		}
	}

	def dayOfWeek = { attrs ->
		def days = ["Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"]

		out << days[attrs.int("index") - 1]
	}

}