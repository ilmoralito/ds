class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		//user
		"/signin"(controller:"user", action:"register")

		"/"(controller:"user", action:"login")
		"/faqs"(view:"/faqs")
		"/register"(view:"/register")
		"500"(view:'/error')

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

	}
}
