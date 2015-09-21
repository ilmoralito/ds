package ni.edu.uccleon

class LoginController {
  def index(String email, String password) {
    def user = User.login(email, password).get()
    def successAction = session?.user?.role == "admin" ? "list" : "listOfPendingApplications"
    def accessDeniedAction = "activity"
    def flag = false

    if (!user) {
      flash.message = "Usuario no registrado. Verifica credenciales"
    } else {
      session.user = user
      flag = true
    }

    redirect controller: "request", action: flag ? successAction : accessDeniedAction
    return false
  }
}