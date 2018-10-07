import ni.edu.uccleon.UserService
import ni.edu.uccleon.Helper

class UrlMappings {

    UserService userService
    Helper helper

    static mappings = { ctx ->
        "/$controller/$action?/$id?"{
            constraints {
                id matches: /\d+/
            }
        }

        '/requests'(controller: 'request', action: 'list')

        "/requests/$id/update/status"(controller: 'request', action: 'updateStatus')

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

        "/requests/to/$status/at/$applicationDate" {
            controller = 'request'
            action = 'updateAllStatus'
            constraints {
                status validator: { status -> status in ctx.helper.getEnglishStatusList() }
                applicationDate nullable: false
            }
        }

        '/requests/filter'(controller: 'request', action: 'filter')

        '/requests/filtered'(controller: 'request', action: 'applyFilter')

        '/profile'(controller: 'user', action: 'profile')

        '/add/classroom'(controller: 'user', action: 'addClassroom', method: 'POST')

        '/delete/classroom'(controller: 'user', action: 'removeClassroom', method: 'POST')

        '/password'(controller: 'user', action: 'password')

        '/update/user/fullname'(controller: 'user', action: 'updateProfile')

        "/users/create/$role" {
            controller = 'user'
            action = 'create'
            constraints {
                role inList: grailsApplication.config.ni.edu.uccleon.roles
            }
        }

        "/$status/users" {
            controller = 'user'
            action = 'getUsersByStatus'
            constraints {
                status inList: ['active', 'inactive']
            }
        }

        "/$role/users" {
            controller = 'user'
            action = 'getUsersByRole'
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
