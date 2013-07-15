package ni.edu.uccleon

class RequestService {

	static transactional = false

	def grailsApplication

    def getTotal(Date date, opt) {
    	def criteria = Request.createCriteria()
    	def total = criteria.count() {
    		eq "dateOfApplication", date
    		eq opt, true
    	}

    	def max = (opt == "audio") ? grailsApplication.config.ni.edu.uccleon.speakers : grailsApplication.config.ni.edu.uccleon.screens

    	return (total == max) ? true : false
    }
}
