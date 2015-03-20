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

  def getInfoToAddHours(Date date) {
    def requests = Request.requestFromTo(date, date).list()
    def datashows = grailsApplication.config.ni.edu.uccleon.datashows
    def day = date[Calendar.DAY_OF_WEEK]
    def blocks = {
      if (day == 1) {
        grailsApplication.config.ni.edu.uccleon.sunday.blocks
      } else if (day == 7) {
        grailsApplication.config.ni.edu.uccleon.saturday.blocks
      } else {
        grailsApplication.config.ni.edu.uccleon.blocks
      }
    }

    [requests:requests, datashows:datashows, day:day, blocks:blocks.call()]
  }

  def mergedClassrooms() {
    def cls = grailsApplication.config.ni.edu.uccleon.cls
    def classroomsMerged = cls["B"] + cls["C"] + cls["D"] + cls["E"] + cls["K"] + cls["undefined"]
    def classrooms = classroomsMerged.collect { classroom ->
      if (classroom.containsKey("name")) {
        classroom
      } else {
        [code:classroom.code, name:classroom.code]
      }
    }

    classrooms.sort { it.name }
  }
}
