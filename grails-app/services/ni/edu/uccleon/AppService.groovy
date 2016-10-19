package ni.edu.uccleon

class AppService {
    def grailsApplication

    String getClassroomCodeOrName(final String classroomCode) {
        final Map classrooms = grailsApplication.config.ni.edu.uccleon.cls
        final String code = classroomCode[0]

        if (classroomCode in classrooms.undefined.code) {
            classroomCode
        } else {
            final Map classroom = classrooms[code].find { it.code == classroomCode }

            classroom.name ?: classroom.code
        }
    }
}
