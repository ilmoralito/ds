package ni.edu.uccleon

class CommonFilters {

    def filters = {
        security(controller:'*', action:'*') {
            before = {
                def publicActions = ["login", "register"]

                if (!session?.user && controllerName != "user" && !publicActions.contains(actionName)) {
                    flash.message = "acceso.denegado"
                    redirect controller:"user"
                    return false
                }
            }
        }

        requestOnlyAdmin(controller:"request", action:"(requestsBySchools|requestsByClassrooms|requestsByUsers|enable|show)") {
            before = {
                if (session?.user?.role != "admin") {
                    response.sendError 403
                    return false
                }
            }
        }

        userOnlyAdmin(controller:"user", action:"(list|resetPassword|delete|show)") {
            before = {
                if (session?.user?.role != "admin") {
                    response.sendError 403
                    return false
                }
            }
        }
    }
}
