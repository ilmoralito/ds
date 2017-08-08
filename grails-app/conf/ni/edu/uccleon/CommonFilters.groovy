package ni.edu.uccleon

class CommonFilters {

  def filters = {
    security(controller: '*', action: '*') {
        before = {
            if (controllerName == 'request' && actionName != 'activity' && !session?.user) {
                flash.message = 'Acceso denegado'
                redirect controller: 'request', action: 'activity'
                return false
            }
        }
    }

    requestOnlyAdmin(controller:"request", action:"(requestsBy|listBy|show|updateStatus|updStatus)") {
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
        if (!(session?.user?.role in ["coordinador", "asistente"])) {
          response.sendError 403
          return false
        }
      }
    }
  }
}
