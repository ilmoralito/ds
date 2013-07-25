package ni.edu.uccleon

import grails.events.Listener

class UserService {

    def addSchoolsAndUserClassrooms(def schools, def classrooms, User user) {
        //schools
        //delete all schools for current user
        def query = School.where {
            user == user
        }

        query.deleteAll()

        //add new schools to current user
        if (schools instanceof String) {
            user.addToSchools(new School(name:schools))
        } else {
            schools.each { school ->
                user.addToSchools(new School(name:school))
            }
        }

        //classrooms
        //delete all userClassrooms instances from current user
        def q = UserClassroom.where {
            user == user
        }

        q.deleteAll()

        //add selected classrooms to current user classrooms set
        if (classrooms instanceof String) {
            user.addToUserClassrooms(new UserClassroom(classroom:classrooms))
        } else {
            classrooms.each { classroom ->
                user.addToUserClassrooms(new UserClassroom(classroom:classroom))
            }
        }
    }

    //registrations
    @Listener(namespace='plugin.emailConfirmation', topic='confirmed')
    def userConfirmedRegsitration(confirmation) {
        def user = User.findByEmail(confirmation.email)

        if (user) {
            user.enabled = true
            return [controller:'user', action:'welcome']
        } else {
            log.error "We don't have any record of this user"
            return [controller:'user', action:'oops']
        }
    }

    @Listener(namespace='plugin.emailConfirmation', topic='invalid')
    def invalidUserConfirmedRegistration(info) {
        log.warn "Invalid confirmation received for token [${info.token}]"
        return [controller:'user', action:'invalid']
    }

    @Listener(namespace='plugin.emailConfirmation', topic='timeout')
    void userConfirmationTimeout(confirmation) {
        log.warn "Confirmation timed out for address [${confirmation.email}] with app id [${confirmation.id}]"
        // You might send an email here to your support team
    }

}