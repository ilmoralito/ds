package ni.edu.uccleon

class CommonTagLib {
	static namespace = "ds"

	def flashMessage = {attrs, body ->
		if (flash.message) {
			out << body()
		}
	}

	def isTrue = {attrs, body ->
		def enabled = attrs.enabled

		if (enabled) {
			out << body()
		}
	}

	def isAdmin = {attrs, body ->
		if (session?.user?.role == "admin") {
			out << body()
		}
	}

	def isUser = {attrs, body ->
		if (session?.user?.role == "user") {
			out << body()
		}
	}

	def isRequestEnabled = {attrs, body ->
		def enable = attrs.enabled

		if (enable) {
			out << "Atendido"
		} else {
			out << "Confirmar"
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

	def isAnExpressRequest = {attrs, body ->
		if (attrs.type == "express") {
			out << body()
		}
	}

}