package ni.edu.uccleon

class CommonFilters {

    def filters = {
        security(controller: '*', action: '*') {
            before = {
                if (!session.user && !(actionName in ['activity', 'login'])) {
                    response.sendError 403
                    return false
                }
            }
        }

        administratorAndSupervisorOnly(controller: 'report', action: '*') {
            before = {
                if (!(session.user.role in ['admin', 'supervisor'])) {
                    response.sendError 403
                    return false
                }
            }
        }

        administratorOnly(controller: 'user', action: '(list|show|create|notification|updateUserRole|updateUserSchools|updateUserEnabledProperty|delete)') {
            before = {
                if (session.user.role != 'admin') {
                    response.sendError 403
                    return false
                }
            }
        }
    }
}
