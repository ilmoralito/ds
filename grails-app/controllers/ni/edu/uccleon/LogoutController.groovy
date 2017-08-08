package ni.edu.uccleon

class LogoutController {

    def index() {
        session.user = null
        redirect controller: 'request', action: 'activity'
    }
}
