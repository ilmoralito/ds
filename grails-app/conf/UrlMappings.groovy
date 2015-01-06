class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				id matches:/\d+/
			}
		}

		"/"(controller:"user", action:"login")
		"/normas"(view:"/normas")

		"/request/create/$type?"(controller:"request", action:"create") {
			constraints {
				type nullable:true
			}
		}

		"/requestsBy/$type" (controller:"request", action:"requestsBy") {
			constraints {
				type inList:["schools", "classrooms", "users", "datashows", "blocks", "resumen"]
			}
		}

		"/hour/create/$dateOfApplication/$requestId/$requestType/$dayOfApplication" (controller:"hour", action:"create") {
			constraints {
				requestId matches:/\d+/
				requestType inList:["common", "express"]
				//dayOfApplication range: 1..7
				//TODO:validate dateOfApplication
			}
		}

		"/request/listBy/$email/$type" (controller:"request", action:"listBy") {
			constraints {
				email email:true
				type inList:["user"]
			}
		}

		"/activity/$dateSeleted?" (controller:"request", action:"activity")

		"/profile" (controller:"user", action:"profile")

		"/password" (controller:"user", action:"password")

		//mapping to response code
		"500"(view:'/errors/500')
		"404"(view:"/errors/404")
		"403"(view:"/errors/403")
	}
}
