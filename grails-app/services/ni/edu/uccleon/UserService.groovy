package ni.edu.uccleon

import grails.events.Listener

class UserService {

    def addSchoolsAndUserClassrooms(Map params, User user) {
    	//schools
        //delete all schools from user
        def query = School.where {
            user == user
        }

		query.deleteAll()

        //get all params with schools in its name
        def schools = params.findAll {key, value ->
            key.indexOf("schools") != -1
        }

        // iterate schools and get value equal on
        schools.each {key, value ->
            if (value == "on") {
            	def school = key.tokenize(".")[1]
             	user.addToSchools(new School(name:school))
            }
        }

        //userClassrooms
        //delete all userClassrooms from user
        def q = UserClassroom.where {
            user == user
        }

        q.deleteAll()

        //get all params with userClassrooms in its name
        def userClassrooms = params.findAll {key, value ->
            key.indexOf("userClassrooms") != -1
        }

        // iterate userClassrooms and get value equal on
        userClassrooms.each {key, value ->
            if (value == "on") {
                def userClassroom = key.tokenize(".")[1]
                user.addToUserClassrooms(new UserClassroom(classroom:userClassroom))
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