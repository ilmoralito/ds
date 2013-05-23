@artifact.package@class @artifact.name@ {

	static defaultAction = "list"
	static allowedMethods = [list:"GET", create:["GET", "POST"], show:["GET", "POST"], delete:"GET"]

    def list() {
    	def domainClassInstance = DomainClass.list()
    	[domainClassInstance:domainClassInstance]
    }

    def create() {
    	if (request.get) {
    		[domainClassInstance:new domainClassInstance(params)]
    	} else {
    		def domainClassInstance = new DomainClass(params)

    		if (!domainClassInstance.save()) {
    			return [domainClassInstance:domainClassInstance]
    		}

    		flash.message = "data.saved"
    	}
    }

    def show(Integer id) {
    	def domainClassInstance = DomainClass.get(id)

    	if (!domainClassInstance) {
    		response.sendError 404
    		return false
    	}

    	if (request.get) {
    		return [domainClassInstance:domainClassInstance]
    	} else if (request.post) {
    		domainClassInstance.properties = params

    		if (!domainClassInstance.save()) {
    			return [domainClassInstance:domainClassInstance]
    		}

    		flash.message = "data.saved"
    	}
    }

    def delete(Integer id) {
    	def domainClassInstance = DomainClass.get(id)

    	if (!domainClassInstance) {
    		response.sendError 404
    		return false
    	}

    	domainClassInstance.delete()

    	flash.message = "data.deleted"
    	redirect action:"list"
    }

}