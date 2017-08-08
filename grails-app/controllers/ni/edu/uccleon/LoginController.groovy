package ni.edu.uccleon

class LoginController {

    static allowedMethods = [index: 'POST']

    def login(final String email, final String password) {
        User user = User.login(email, password).get()

        if (!user) {
            flash.message = 'Credenciales incorrectas'
            redirect controller: 'request', action: 'activity'
            return false
        }

        session.user = user

        redirect controller: 'request', action: user.role == 'admin' ? 'list' : 'listOfPendingApplications'
    }
}
