import ni.edu.uccleon.*
import grails.util.Environment
import grails.util.DomainBuilder
import static java.util.Calendar.*

class BootStrap {
    def grailsApplication
    def requestService

    def init = { servletContext ->
        if (Environment.current == Environment.DEVELOPMENT) {
            development()
        }

        if (Environment.current == Environment.PRODUCTION) {
            production()
        }
    }

    def destroy = {}

    private development() {
        ConfigObject config = grailsApplication.config.ni.edu.uccleon
        Date today = new Date().clearTime()
        Integer dayOfWeek = today[DAY_OF_WEEK]
        DomainBuilder builder = new DomainBuilder()
        List<User> users = []

        // Coordinations
        // academic
        String industrial = config.schoolsAndDepartments.schools[0]
        String cpfegmp = config.schoolsAndDepartments.schools[1]
        String aeaethcid = config.schoolsAndDepartments.schools[2]
        String fese = config.schoolsAndDepartments.schools[7]

        // administrative
        String administracion = config.schoolsAndDepartments.departments[2]
        String soporteTecnico = config.schoolsAndDepartments.departments[14]
        String especializacion = config.schoolsAndDepartments.departments[0]
        String educacionContinua = config.schoolsAndDepartments.departments[1]

        // Schools and offices
        // B
        String mesanini1 = config.cls["B"][1]["code"]
        String mesanini2 = config.cls["B"][2]["code"]

        // D
        String d101 = config.cls["D"][0]["code"]
        String d102 = config.cls["D"][1]["code"]
        String d103 = config.cls["D"][2]["code"]
        String d104 = config.cls["D"][3]["code"]
        String d109 = config.cls["D"][5]["code"]

        // E
        String e108 = config.cls["E"][0]["code"]
        String e112 = config.cls["E"][1]["code"]
        String e113 = config.cls["E"][2]["code"]

        builder.classNameResolver = "ni.edu.uccleon"

        users << builder.user(
            email: "admin.user@ucc.edu.ni",
            role: "admin",
            fullName: "admin user",
            schools: [soporteTecnico],
            classrooms: [mesanini1, mesanini2]
        )

        users << builder.user(
            email: 'karen.alonso@ucc.edu.ni',
            role: 'asistente',
            fullName: 'Karen Alonso',
            schools: [especializacion, educacionContinua],
            classrooms: [mesanini1, mesanini2, d109, e112]
        )

        users << builder.user(
            email: 'yendri.iglesias@ucc.edu.ni',
            role: 'asistente',
            fullName: 'Yendri Iglesias',
            schools: [especializacion, educacionContinua],
            classrooms: [mesanini1, mesanini2, d109, e112]
        )

        users << builder.user(
            email: "administrative.user@ucc.edu.ni",
            role: "administrativo",
            fullName: "administrative user",
            schools: [administracion],
            classrooms: [mesanini1, mesanini2]
        )

        users << builder.user(
            email: "someuser@domain.com",
            role: "user",
            fullName: "someuser user",
            schools: [fese],
            classrooms: [e108, e112, e113],
            enabled: false
        )

        users << builder.user(
            email: 'yetanotheruser@domain.com',
            role: 'user',
            fullName: 'Abigail Castellon',
            schools: [fese],
            classrooms: [e108, e112],
            enabled: true
        )

        users << builder.user(
            email: "user.user@domain.com",
            fullName: "user user",
            schools: [industrial, cpfegmp, aeaethcid, fese],
            classrooms: [d101, d102, d103, d104]
        ) {
            request(
                dateOfApplication: today,
                classroom: d102,
                school: cpfegmp,
                datashow: requestService.getDatashow(cpfegmp, dayOfWeek)[0],
                description: "Lorem ipsum dolor sit amet",
                audio: true,
                internet: true
            ) {
                hour(block: 0)
            }

            request(
                dateOfApplication: today,
                classroom: d101,
                school: cpfegmp,
                datashow: requestService.getDatashow(cpfegmp, dayOfWeek)[0],
                description: "Lorem ipsum dolor sit amet, consectetur adipisicing elit"
            ) {
                hour(block: 1)
                hour(block: 2)
            }
        }

        users << builder.user(
            email: "assistant.user@ucc.edu.ni",
            fullName: "assistant user",
            role: "asistente",
            schools: [fese],
            classrooms: [e108, e112, e113]
        ) {
            request(
                dateOfApplication: today,
                classroom: e108,
                school: fese,
                datashow: requestService.getDatashow(fese, dayOfWeek)[0],
                description: "!very important"
            ) {
                hour(block: 1)
                hour(block: 2)
            }

            request(
                dateOfApplication: today,
                classroom: e108,
                school: fese,
                datashow: requestService.getDatashow(fese, dayOfWeek)[0],
                description: "!important"
            ) {
                hour(block: 0)
            }
        }

        users << builder.user(
            email: "anotherassistant.user@ucc.edu.ni",
            fullName: "another assistant user",
            role: "asistente",
            schools: [fese, industrial],
            classrooms: [e108, e112, e113]
        )

        users << builder.user(
            email: "coordinator.user@ucc.edu.ni",
            fullName: "coordinador user",
            role: "coordinador",
            schools: [fese],
            classrooms: [d109, e108, e112, e113]
        ) {
            request(
                dateOfApplication: today,
                classroom: d109,
                school: fese,
                datashow: requestService.getDatashow(fese, dayOfWeek)[0],
                status: "attended",
                audio: true
            ) {
                hour(block: 0)
                hour(block: 1)
                hour(block: 2)
            }

            request(
                dateOfApplication: today,
                classroom: e113,
                school: fese,
                datashow: requestService.getDatashow(fese, dayOfWeek)[0],
                status: "canceled",
                description: "Lorem ipsum dolor sit amet, consectetur adipisicing elit"
            ) {
                hour(block: 1)
            }
        }

        users.each { user ->
            user.save failOnError: true
        }
    }

    private production() {
        ConfigObject config = grailsApplication.config.ni.edu.uccleon

        if (!User.findByEmail(System.env.GMAIL_USERNAME)) {
            new User (
                email: System.env.GMAIL_USERNAME,
                password: System.env.DEFAULT_ADMIN_PASSWORD,
                role: "admin",
                fullName: config.firstUserInformation.fullName,
                schools: config.firstUserInformation.schools,
                classrooms: config.firstUserInformation.classrooms
            ).save failOnError: true
        }
    }
}