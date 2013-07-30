class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				id matches:/\d+/
			}
		}

		"/"(controller:"user", action:"login")
		"/faqs"(view:"/faqs")

		"/request/create/$type?"(controller:"request", action:"create") {
			constraints {
				type nullable:true
			}
		}

		"/requestsBy/$type" (controller:"request", action:"requestsBy") {
			constraints {
				type inList:["schools", "classrooms", "users", "datashows", "blocks"]
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

		//mapping to response code
		"500"(view:'/errors/500')
		"404"(view:"/errors/404")
		"403"(view:"/errors/403")
	}
}
