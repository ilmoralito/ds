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
        String industrial = config.schoolsAndDepartments.schools.find { school ->
            school == 'Industrial'
        }

        String ccee = config.schoolsAndDepartments.schools.find { school ->
            school == 'CCEE'
        }

        String agronomy = config.schoolsAndDepartments.schools.find { school ->
            school == 'Agronomia'
        }

        String systemsAndGraphicDesign = config.schoolsAndDepartments.schools.find { school ->
            school == 'Sistemas y diseno grafico'
        }

        String law = config.schoolsAndDepartments.schools.find { school ->
            school == 'Derecho'
        }

        String architectureAndCivil = config.schoolsAndDepartments.schools.find { school ->
            school == 'Arquitectura y civil'
        }

        String academicDirection = config.schoolsAndDepartments.schools.find { school ->
            school == 'Direccion academica'
        }

        // administrative
        String administracion = config.schoolsAndDepartments.departments[2]
        String soporteTecnico = config.schoolsAndDepartments.departments[14]
        String especializacion = config.schoolsAndDepartments.departments[0]
        String educacionContinua = config.schoolsAndDepartments.departments[1]
        String delegacionDeLaSede = config.schoolsAndDepartments.departments[15]

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

        builder.classNameResolver = 'ni.edu.uccleon'

        users << builder.user(
            email: 'guissella.gonzalez@ucc.edu.ni',
            role: 'coordinador',
            fullName: 'Guissella Gonzales',
            schools: [delegacionDeLaSede],
            classrooms: [mesanini1, mesanini2]
        )

        users << builder.user(
            email: 'marta.torres@ucc.edu.ni',
            role: 'coordinador',
            fullName: 'Marta Torrez',
            schools: [academicDirection],
            classrooms: [mesanini1, mesanini2]
        )

        users << builder.user(
            email: 'marcia.sandino@ucc.edu.ni',
            role: 'administrativo',
            fullName: 'Marcia Sandino',
            schools: [delegacionDeLaSede],
            classrooms: [mesanini1, mesanini2]
        )

        users << builder.user(
            email: 'rosalia.prado@ucc.edu.ni',
            role: 'coordinador',
            fullName: 'Rosalia Prado',
            schools: [law],
            classrooms: [d101, d102, d103, d104]
        )

        users << builder.user(
            email: 'azucena.delgado@ucc.edu.ni',
            role: 'asistente',
            fullName: 'Azucena Delgado',
            schools: [law, agronomy, systemsAndGraphicDesign, architectureAndCivil],
            classrooms: [d101, d102, d103, d104]
        )

        users << builder.user(
            email: 'yesenia.valverde@ucc.edu.ni',
            role: 'asistente',
            fullName: 'Yesenia Valverde',
            schools: [industrial],
            classrooms: [d101, d102, d103, d104]
        )

        users << builder.user(
            email: 'mario.martinez@ucc.edu.ni',
            role: 'admin',
            fullName: 'Mario Martinez',
            schools: [soporteTecnico],
            classrooms: [mesanini1, mesanini2]
        )

        users << builder.user(
            email: 'sergio.lopez@ucc.edu.ni',
            role: 'admin',
            fullName: 'Sergio Lopez',
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
            email: 'firstname.lastname@ucc.edu.ni',
            role: 'asistente',
            fullName: 'FirstName LastName',
            schools: [especializacion, educacionContinua],
            classrooms: [mesanini1, mesanini2, e112],
            enabled: false
        )

        users << builder.user(
            email: 'administrative.user@ucc.edu.ni',
            role: 'administrativo',
            fullName: 'administrative user',
            schools: [administracion],
            classrooms: [mesanini1, mesanini2]
        )

        users << builder.user(
            email: 'user.user@domain.com',
            fullName: 'user user',
            schools: [industrial, ccee],
            classrooms: [d101, d102, d103, d104]
        ) {
            request(
                dateOfApplication: today,
                classroom: d102,
                school: industrial,
                datashow: requestService.getDatashow(industrial, dayOfWeek)[0],
                description: 'Lorem ipsum dolor sit amet',
                audio: true,
                internet: true
            ) {
                hour(block: 0)
            }

            request(
                dateOfApplication: today,
                classroom: d101,
                school: ccee,
                datashow: requestService.getDatashow(ccee, dayOfWeek)[0],
                description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit'
            ) {
                hour(block: 1)
                hour(block: 2)
            }
        }

        users << builder.user(
            email: 'assistant.user@ucc.edu.ni',
            fullName: 'assistant user',
            role: 'asistente',
            schools: [ccee],
            classrooms: [e108, e112, e113]
        ) {
            request(
                dateOfApplication: today,
                classroom: e108,
                school: ccee,
                datashow: requestService.getDatashow(ccee, dayOfWeek)[0],
                description: '!very important'
            ) {
                hour(block: 1)
                hour(block: 2)
            }

            request(
                dateOfApplication: today,
                classroom: e108,
                school: ccee,
                datashow: requestService.getDatashow(ccee, dayOfWeek)[0],
                description: '!important'
            ) {
                hour(block: 0)
            }
        }

        users << builder.user(
            email: 'anotherassistant.user@ucc.edu.ni',
            fullName: 'another assistant user',
            role: 'asistente',
            schools: [ccee, industrial, law],
            classrooms: [e108, e112, e113]
        )

        users << builder.user(
            email: 'coordinator.user@ucc.edu.ni',
            fullName: 'coordinador user',
            role: 'coordinador',
            schools: [ccee],
            classrooms: [d109, e108, e112, e113]
        ) {
            request(
                dateOfApplication: today,
                classroom: d109,
                school: ccee,
                datashow: requestService.getDatashow(ccee, dayOfWeek)[0],
                status: 'attended',
                audio: true
            ) {
                hour(block: 0)
                hour(block: 1)
                hour(block: 2)
            }

            request(
                dateOfApplication: today,
                classroom: e113,
                school: ccee,
                datashow: requestService.getDatashow(ccee, dayOfWeek)[0],
                status: 'canceled',
                description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit'
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