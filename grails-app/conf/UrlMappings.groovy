import ni.edu.uccleon.UserService

class UrlMappings {

    UserService userService

    static mappings = { ctx ->
        "/$controller/$action?/$id?"{
            constraints {
                id matches: /\d+/
            }
        }

        '/'(controller: 'request', action: 'activity')

        '/requests/pending'(controller: 'request', action: 'listOfPendingApplications')

        '/requests/user/statistics'(controller: 'request', action: 'userStatistics')

        '/requests/summary'(controller: 'request', action: 'requestsByCoordination')

        "/activities/$dateOfApplication?" {
            controller = 'request'
            action = 'activity'
            constraints {
                dateOfApplication nullable: true
            }
        }

        '/profile'(controller: 'user', action: 'profile')

        '/add/classroom'(controller: 'user', action: 'addClassroom', method: 'POST')

        '/delete/classroom'(controller: 'user', action: 'removeClassroom', method: 'POST')

        '/password'(controller: 'user', action: 'password')

        "/users/create/$role" {
            controller = 'user'
            action = 'create'
            constraints {
                role inList: grailsApplication.config.ni.edu.uccleon.roles
            }
        }

        '/normas'(view: '/normas')

        '500'(view: '/errors/500')
        '404'(view: '/errors/404')
        '403'(view: '/errors/403')
    }
}
