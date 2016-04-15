package ni.edu.uccleon

import grails.util.Holders

class ClassroomService {
  def transform(requests) {
    def classrooms = Holders.config.ni.edu.uccleon.cls
    def codes = classrooms.keySet() as List

    requests.collect { r ->
      def code = r.classroom[0]
      def k = code in codes[0..4] && r.classroom.size() <= 5 ? code : "undefined"
      def t = classrooms[k].find { it.code == r.classroom }

      r.classroom = t.containsKey("name") ? t.name : t.code
      r.discard()
    }

    requests
  }
}
