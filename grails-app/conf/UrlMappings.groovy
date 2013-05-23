class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:"user", action:"login")
		"/faqs"(view:"/faqs")
		"500"(view:'/error')

		//user

	}
}
