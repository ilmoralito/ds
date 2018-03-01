package ni.edu.uccleon

class LoginController {

    UserService userService

    static allowedMethods = [index: 'POST']

    def auth(final String email, final String password) {
        User user = User.login(email, password).get()

        if (!user) {
            flash.message = 'Credenciales incorrectas'
            redirect controller: 'request', action: 'activity'
            return false
        }

        session.user = user

        if (user.role != 'admin') {
            session.schools = userService.getUserSchools(user.id)
        }

        redirect controller: 'request', action: user.role == 'admin' ? 'list' : 'listOfPendingApplications'
    }
}
