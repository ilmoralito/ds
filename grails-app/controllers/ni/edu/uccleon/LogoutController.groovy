package ni.edu.uccleon

class LogoutController {

    def getOut() {
        session.user = null
        redirect controller: 'request', action: 'activity'
    }
}
