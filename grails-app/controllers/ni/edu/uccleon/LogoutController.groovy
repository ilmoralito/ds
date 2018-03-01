package ni.edu.uccleon

class LogoutController {

    def getOut() {
        session.user = null
        session.schools = null

        redirect controller: 'request', action: 'activity'
    }
}
