package ni.edu.uccleon

class AppService {
    def grailsApplication

    def getClassroomCodeOrName(final String classroomCode) {
        final Map classrooms = grailsApplication.config.ni.edu.uccleon.cls
        final String code = classroomCode[0]
        final Map classroom = classrooms[code].find { it.code == classroomCode }

        classroom.name ?: classroom.code
    }
}
