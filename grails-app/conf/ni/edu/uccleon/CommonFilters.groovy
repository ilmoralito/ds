package ni.edu.uccleon

class CommonFilters {

    UserService userService

    def filters = {
        security(controller: '*', action: '*') {
            before = {
                if (!session.user && !(actionName in ['activity', 'auth', 'getOut', 'listTodayActivities'])) {
                    flash.message = 'Acceso denegado'
                    redirect controller: 'request', action: 'activity'
                    return false
                }
            }
        }

        administratorAndSupervisorOnly(controller: 'report', action: '*', actionExclude: '(coordinationList|facultySummary|summaryOfTeacherApplicationsInMonth)') {
            before = {
                if (!(session.user.role in ['admin', 'supervisor'])) {
                    response.sendError 403
                    return false
                }
            }
        }

        userAdministratorOnly(controller: 'user', action: '(list|show|create|notification|updateUserEnabledProperty|delete|filter|applyFilter)') {
            before = {
                if (session.user.role != 'admin') {
                    response.sendError 403
                    return false
                }
            }
        }

        requestAdministratorOnly(controller: 'request', action: '(list|filter|applyFilter|changeRequestsStatus|updateStatus|updateAllStatus)') {
            before = {
                if (session.user.role != 'admin') {
                    response.sendError 403
                    return false
                }
            }
        }

        coordinatorOnly(controller: 'report', action: '(coordinationList|facultySummary|summaryOfTeacherApplicationsInMonth)') {
            before = {
                if (session.user.role != 'coordinador') {
                    response.sendError 403
                    return false
                }

                if (actionName != 'coordinationList' && !(params.school in userService.getUserSchools(session.user.id))) {
                    response.sendError 403
                    return false
                }
            }
        }

        editFilter(controller: 'request', action: '(edit|update)') {
            before = {
                if (!(session.user.role in ['coordinador', 'asistente', 'administrativo'])) {
                    response.sendError 403
                    return false
                }

                Request requestInstance = Request.get(params.id)
                List<String> currentUserSchools = userService.getUserSchools(session.user.id)

                if (!(requestInstance.school in currentUserSchools)) {
                    response.sendError 403
                    return false
                }
            }
        }
    }
}
