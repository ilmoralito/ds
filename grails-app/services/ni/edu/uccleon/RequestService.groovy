package ni.edu.uccleon

import static java.util.Calendar.*

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
    def datashows = grailsApplication.config.ni.edu.uccleon.datashows.size()
    def blocks = getDayOfWeekBlocks(date[Calendar.DAY_OF_WEEK])

    [requests: requests, datashows: datashows, day: day, blocks: blocks]
  }

  Integer getDayOfWeekBlocks(Integer dayOfWeek) {
    if (dayOfWeek == 1) {
      grailsApplication.config.ni.edu.uccleon.sunday.blocks
    } else if (dayOfWeek == 7) {
      grailsApplication.config.ni.edu.uccleon.saturday.blocks
    } else {
      grailsApplication.config.ni.edu.uccleon.blocks
    }
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

  def getUsersInCurrentUserCoordinations(String role, User user, String action) {
    if (role in ["coordinador", "asistente"]) {
      def users = User.findAllByRoleNotEqualAndEnabled("admin", true, [sort: "fullName", order: "asc"])
      def results = users.findAll { u ->
        user.schools.any { u.schools.contains(it) }
      }

      //Set session user in position 0 in users list
      if (action == 'create') {
        results -= user
        results.plus 0, user
      } else {
        results
      }
    }
  }

  List<Integer> getDatashow(String coordination, Integer dayOfWeek) {
    ConfigObject config = grailsApplication.config.ni.edu.uccleon
    def datashow = config.data.find { it.coordination == coordination }.datashow[dayOfWeek]
    List<Integer> list = []

    if (datashow instanceof Integer) {
      list << datashow
    } else {
      datashow
    }
  }
}
