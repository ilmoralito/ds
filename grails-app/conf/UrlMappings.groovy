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

        '/normas'(view: '/normas')

        '/requests/pending'(controller: 'request', action: 'listOfPendingApplications')

        "/activities/$dateOfApplication?" {
            controller = 'request'
            action = 'activity'
            constraints {
                dateOfApplication nullable: true
            }
        }

        '/profile'(controller: 'user', action: 'profile')

        '/password'(controller: 'user', action: 'password')

        '500'(view: '/errors/500')
        '404'(view: '/errors/404')
        '403'(view: '/errors/403')
    }
}
