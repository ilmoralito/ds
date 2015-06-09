package ni.edu.uccleon

class CommonFilters {

  def filters = {
    security(controller:'*', action:'*') {
      before = {
        def publicActions = ["login"]

        if (!session?.user && controllerName != "user" && actionName != "activity") {
          flash.message = "acceso.denegado"
          redirect controller:"user"
          return false
        }

        if (!session?.user && controllerName == "user" && !publicActions.contains(actionName)) {
          flash.message = "acceso.denegado"
          redirect controller:"user"
          return false
        }
      }
    }

    requestOnlyAdmin(controller:"request", action:"(requestsBy|listBy|show|updateStatus|updStatus|list)") {
      before = {
        if (session?.user?.role == "user") {
          response.sendError 403
          return false
        }
      }
    }

    userOnlyAdmin(controller:"user", action:"(list|resetPassword|delete|show|create)") {
      before = {
        if (session?.user?.role != "admin") {
          response.sendError 403
          return false
        }
      }
    }

    coordAndAsistOnly(controller:"request", action:"(createRequestFromActivity)") {
      before = {
        if (!(session?.user?.role in grailsApplication.config.ni.edu.uccleon.roles[2..-1])) {
          response.sendError 403
          return false
        }
      }
    }
  }
}
