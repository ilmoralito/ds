package ni.edu.uccleon

class HourController {

	static defaultAction = "create"
	static allowedMethods = [create:["GET", "POST"]]

    def create() {
        def dateApp = params?.dateOfApplication
        def requests = Request.requestFromTo(dateApp, dateApp).list()

        if (request.post) {
            //datashow
            def datashow = params.int("datashow")
            //requestId
            def requestId = params.int("requestId")
            //blocks
            def blocks = params.findAll {key, value ->
                value == "on"
            }

            if (blocks) {
                def request = Request.get(requestId)

                request.datashow = datashow

                if (!request.save()) {
                    request.errors.allErrors.each {
                        print it
                    }
                }

                //if im editing i first delete blocks added to current request and then just then i
                //add new blocks this this request

                if (params?.flag == "editing") {
                    def l = []

                    l += request.hours.block

                    l.each { block ->
                        def b = request.hours.find { it.block == block }

                        request.removeFromHours(b)
                    }
                }

                blocks.keySet().each {
                    def hour = new Hour(
                        datashow:datashow,
                        block:it.toInteger(),
                        request:request
                    )

                    if(!hour.save()) {
                        hour.errors.allErrors.each {
                            print it
                        }
                    }
                }

                flash.message = "request.saved"
                redirect controller:"request"
            } else {
                flash.message = "please.add.an.hour"
                redirect action:"create", params:[dateOfApplication:dateApp, currentClassroom:params?.currentClassroom, requestId:requestId]
            }

        }

        [requests:requests]
    }

}