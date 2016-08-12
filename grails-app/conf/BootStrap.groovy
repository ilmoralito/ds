import ni.edu.uccleon.*
import grails.util.Environment
import grails.util.DomainBuilder

class BootStrap {
    def grailsApplication

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
        DomainBuilder builder = new DomainBuilder()
        List<User> users = []

        builder.classNameResolver = "ni.edu.uccleon"

        users << builder.user(
            email: "admin.user@ucc.edu.ni",
            role: "admin",
            fullName: "admin user",
            schools: [config.schoolsAndDepartments.departments[13]], // Soporte tecnico
            classrooms: [
                config.cls["B"][1]["code"], // Mesanini 1
                config.cls["B"][2]["code"] // Mesanini 2
            ]
        )

        users << builder.user(
            email: "administrative.user@ucc.edu.ni",
            role: "administrativo",
            fullName: "administrative user",
            schools: [config.schoolsAndDepartments.departments[1]], // Administracion
            classrooms: [
                config.cls["B"][1]["code"], // Mesanini 1
                config.cls["B"][2]["code"] // Mesanini 2
            ]
        )

        users << builder.user(
            email: "user.user@domain.com",
            fullName: "user user",
            schools: [
                config.schoolsAndDepartments.schools[0], // CPF, EG, MP
                config.schoolsAndDepartments.schools[1], // AE, AETH, CI, D
                config.schoolsAndDepartments.schools[2] // Arquitectura y civil
            ],
            classrooms: [
                config.cls["D"][0]["code"], // D101
                config.cls["D"][1]["code"], // D102
                config.cls["D"][2]["code"], // D103
                config.cls["D"][3]["code"] // D104
            ]
        ) {
            request(
                dateOfApplication: today,
                classroom: config.cls["D"][1]["code"],
                school: config.schoolsAndDepartments.schools[1],
                datashow: 1,
                description: "Lorem ipsum dolor sit amet",
                audio: true,
                internet: true
            ) {
                hour(block: 0)
            }

            request(
                dateOfApplication: today,
                classroom: config.cls["D"][0]["code"],
                school: config.schoolsAndDepartments.schools[0],
                datashow: 2,
                description: "Lorem ipsum dolor sit amet, consectetur adipisicing elit"
            ) {
                hour(block: 1)
                hour(block: 2)
            }
        }

        users << builder.user(
            email: "assistant.user@ucc.edu.ni",
            fullName: "assistant user",
            schools: [config.schoolsAndDepartments.schools[7]],
            classrooms: [
                config.cls["E"][0]["code"], // E108
                config.cls["E"][1]["code"], // E112
                config.cls["E"][2]["code"]  // E113
            ]
        ) {
            request(
                dateOfApplication: today + 1,
                classroom: config.cls["E"][0]["code"],
                school: config.schoolsAndDepartments.schools[7],
                datashow: 5,
                description: "!very important"
            ) {
                hour(block: 2)
                hour(block: 3)
            }

            request(
                dateOfApplication: today + 1,
                classroom: config.cls["E"][0]["code"],
                school: config.schoolsAndDepartments.schools[7],
                datashow: 5,
                description: "!important"
            ) {
                hour(block: 0)
            }
        }

        users << builder.user(
            email: "coordinador.user@ucc.edu.ni",
            fullName: "coordinador user",
            schools: [config.schoolsAndDepartments.schools[7]], // FESE
            classrooms: [
                config.cls["D"][5]["code"], // D109 Sala de audiovisuales
                config.cls["E"][0]["code"], // E108
                config.cls["E"][1]["code"], // E112
                config.cls["E"][2]["code"] // E113
            ]
        ) {
            request(
                dateOfApplication: today,
                classroom: config.cls["D"][5]["code"],
                school: config.schoolsAndDepartments.schools[7],
                datashow: 5,
                status: "attended",
                audio: true
            ) {
                hour(block: 0)
                hour(block: 1)
                hour(block: 2)
            }

            request(
                dateOfApplication: today,
                classroom: config.cls["E"][2]["code"],
                school: config.schoolsAndDepartments.schools[7],
                datashow: 6,
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