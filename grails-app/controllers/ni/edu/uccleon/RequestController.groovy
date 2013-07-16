package ni.edu.uccleon

class RequestController {

    def requestService

	static defaultAction = "list"
	static allowedMethods = [
        list:"GET",
        create:["GET", "POST"],
        edit:"GET",
        show:"GET",
        updte:"POST",
        delete:["GET", "POST"],
        enabled:"GET",
        requestsBySchools:["GET", "POST"],
        requestsByClassrooms:["GET", "POST"],
        requestsByUsers:["GET", "POST"],
        disponability:"POST"
    ]

    def list() {
    	def requests
        def user = session?.user
        def role = user?.role

        if (role == "admin") {
            //TODO:display today requests
            requests = Request.todayRequest().list()
            //requests = Request.list(params)
        } else {
            //TODO:requests must be sorted by dateOfApplication
            requests = Request.listByUser(user).list(params)
        }

    	[requests:requests]
    }

    def create() {
    	if (request.get) {
    		[req:new Request(params)]
    	} else {
            def user = User.findByEmail(session?.user?.email)

            //TODO:find a better way to get this
            def dateApp = params?.dateOfApplication

            //validate speakers and screens availability
            if (params?.audio) {
                if ( requestService.getTotal(parseDate(params?.dateOfApplication), "audio") ) {
                    flash.message = "no.more.speakers"
                    redirect action:"create", params:[type:params?.type]
                    return false
                }
            }

            if (params?.screen) {
                if ( requestService.getTotal(parseDate(params?.dateOfApplication), "screen") ) {
                    flash.message = "no.more.screens"
                    redirect action:"create", params:[type:params?.type]
                    return false
                }
            }

            def req = new Request(
                dateOfApplication:parseDate(params?.dateOfApplication),
                classroom:params?.classroom,
                school:params?.school,
                description:params?.description,
                type:params?.type,
                audio:params?.audio,
                screen:params?.screen,
                internet:params?.internet,
                user:session?.user
            )

            if (!req.save()) {
                return [req:req]
            }

            redirect controller:"hour", action:"create", params:[
                dateOfApplication:dateApp,
                requestId:req.id,
                requestType:req.type,
                dayOfApplication:parseDate(params?.dateOfApplication)[Calendar.DAY_OF_WEEK]
            ]

    		flash.message = "data.saved"
    	}
    }

    def show(Integer id) {
        def req = Request.get(id)

        if (!req) {
            response.sendError 404
        }

        [req:req]
    }

    def edit(Integer id) {
    	def req = Request.findByIdAndUser(id, session?.user)

    	if (!req) {
    		response.sendError 404
        }

    	return [req:req]
    }

    def update(Integer id) {
        def req = Request.findByIdAndUser(id, session?.user)

        if (!req) {
            response.sendError 404
        }

        if (req.enabled) {
            response.sendError 403
        }

        def dateApp = params?.dateOfApplication
        params.dateOfApplication = parseDate(params?.dateOfApplication)
        req.properties = params

        if (!req.save()) {
            chain action:"edit", model:[req:req], params:[id:id]
            return false
        }

        if (req.isDirty("dateOfApplication")) {
            redirect controller:"hour", action:"create", params:[
                dateOfApplication:dateApp,
                requestId:req.id,
                requestType:req.type,
                dayOfApplication:params?.dateOfApplication[Calendar.DAY_OF_WEEK],
                flag:"editing"
            ]
        } else {
            redirect action:"edit", id:id
        }

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

    def disponability(String q) {
        def requests

        if (!q) {
            response.sendError 404
        } else {
            requests = Request.requestFromTo(q, q).findAllByEnabled(false)
        }

        [requests:requests]
    }

    //REPORTS
    def requestsBy(String from, String to, String type) {
        def f = parseDate(from)
        def t = parseDate(to)
        List results

        switch(type) {
            case "schools":
                results = (request.get) ? Request.requestsBy("school").list() : Request.requestsBy("school").requestFromTo(f, t).list()
                break
            case "classrooms":
                results = (request.get) ? Request.requestsBy("classroom").list() : Request.requestsBy("classroom").requestFromTo(f, t).list()
                break
            case "users":
                results = (request.get) ? Request.requestsBy("user").listByRole("user").list() : Request.requestsBy("user").listByRole("user").requestFromTo(f, t).list()
                break
            case "blocks":
                results = (request.get) ? Request.requestsByBlocks().list() : Request.requestsByBlocks().requestFromTo(f, t).list()
                break
            case "datashows":
                results = (request.get) ? Request.requestsBy("datashow").list() : Request.requestsBy("datashow").requestFromTo(f, t).list()
                break
        }

        [results:results, total:getTotal(results), type:type]
    }

    //LIST BY
    def listBy(String type) {
        def requests

        switch(type) {
            case "user":
                requests = Request.listByUser(session?.user).list(params)
                break
        }

        [requests:requests]
    }

    private parseDate(String date) {
        Date d

        if (!date) {
            return null
        }

        try {
            d = new Date().parse("yyyy-MM-dd", date)
        }
        catch(Exception e) {
            return null
        }

        d
    }

    private getTotal(List results) {
        def total = 0

        results.each {result ->
            total += result.count
        }

        total
    }

}