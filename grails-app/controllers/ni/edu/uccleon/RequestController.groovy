package ni.edu.uccleon

class RequestController {

	static defaultAction = "list"
	static allowedMethods = [
        list:"GET",
        create:["GET", "POST"],
        edit:"GET",
        show:"GET",
        updte:"POST",
        delete:"GET",
        enabled:"GET",
        requestsBySchools:["GET", "POST"],
        requestsByClassrooms:["GET", "POST"],
        requestsByUsers:["GET", "POST"]
    ]

    def list() {
    	def requests
        def user = session?.user
        def role = user?.role

        if (role == "admin") {
            //TODO:display today requests
            requests = Request.list()
        } else {
            //TODO:requests must be sorted by dateOfApplication
            requests = Request.listByUser(user).list()
        }

    	[requests:requests]
    }

    def create() {
    	if (request.get) {
    		[req:new Request(params)]
    	} else {
            def user = User.findByEmail(session?.user?.email)

            //TODO:find a better way to get this
            params.dateOfApplication = (params.dateOfApplication) ? new Date().parse("yyyy-MM-dd", params?.dateOfApplication) : new Date()
            params.user = user

            def req = new Request(params)

            if (!req.validate()) {
                return [req:req]
            }

            user.addToRequests(req)

    		flash.message = "data.saved"
    	}
    }

    def show(Integer id) {
        def req = Request.get(id)

        if (!req) {
            response.sendError 404
            return false
        }

        def d = "2013-05"
        def today = new Date().parse("yyyy-MM", d)

        //print today

        def criteria = Request.createCriteria()
        def res = criteria.list {
            ge "dateOfApplication", today
        }

        print res

        [req:req]
    }

    def edit(Integer id) {
    	def req = Request.findByIdAndUser(id, session?.user)

    	if (!req) {
    		response.sendError 404
    		return false
    	}

    	return [req:req]
    }

    def update(Integer id) {
        def req = Request.findByIdAndUser(id, session?.user)

        if (!req) {
            response.sendError 404
            return false
        }

        if (req.enabled) {
            response.sendError 403
            return false
        }

        //TODO:find a better way to get this
        params.dateOfApplication = (params.dateOfApplication) ? new Date().parse("yyyy-MM-dd", params?.dateOfApplication) : new Date()
        req.properties = params

        if (!req.save()) {
            chain action:"edit", model:[req:req], params:[id:id]
            return false
        }

        flash.message = "data.saved"
        redirect action:"edit", params:[id:id]
    }

    def delete(Integer id) {
    	def req = Request.findByIdAndUser(id, session?.user)

    	if (!req) {
    		response.sendError 404
    		return false
    	}

        if (req.enabled) {
            response.sendError 403
            return false
        }

    	req.delete()

    	flash.message = "data.deleted"
    	redirect action:"list"
    }

    def enable(Integer id) {
        def req = Request.get(id)

        if (!req) {
            response.sendError 404
            return false
        }

        req.properties["enabled"] = (req.enabled) ? false : true
        req.save()

        flash.message = "data.saved"

        if (params.path) {
            redirect action:"show", params:[id:id]
            return false
        }

        redirect action:"list"
    }

    //REPORTS
    def requestsBySchools() {
        def results

        if (request.get) {
            results = Request.requestsBySchools().list()
        } else {
            //results = Request.requestsBySchools().findAllByDateOfApplicationAnd.
        }

        [results:results]
    }

    def requestsByClassrooms() {
        def results = Request.requestsByClassrooms().list()

        [results:results]
    }

    def requestsByUsers() {
        def results = Request.requestsByUsers().list()

        [results:results]
    }

}