package ni.edu.uccleon

class CommonFilters {

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

        requestAdministratorOnly(controller: 'request', action: '(list|filter|applyFilter|changeRequestsStatus|updateStatus)') {
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

                // Check if the school belongs to the current coordinator school list
                if (actionName != 'coordinationList' && !(params.school in session.user.schools.toArray())) {
                    response.sendError 403
                    return false
                }
            }
        }
    }
}
