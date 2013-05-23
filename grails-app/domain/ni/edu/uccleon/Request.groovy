package ni.edu.uccleon

import org.hibernate.transform.AliasToEntityMapResultTransformer

class Request {

    Date dateOfApplication
    String classroom
    String school
    String description
    Boolean enabled = false

	Date dateCreated
	Date lastUpdated

    static constraints = {
        dateOfApplication blank:false, validator: {val, obj ->
            def minimalDate = new Date() + 3

            return val >= minimalDate
        }
        classroom blank:false
        school blank:false
        description nullable:true, maxSize:10000
    }

    static namedQueries = {
        listByUser {user ->
            eq "user", user
        }

        //TODO:create request by today

        betweenDates {

        }

        //reports
        requestsBySchools {
            projections {
                groupProperty "school", "school"
                count "school", "count"
            }

            resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        }

        requestsByClassrooms {
            projections {
                groupProperty "classroom", "classroom"
                count "classroom", "count"
            }

            resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        }

        requestsByUsers {
            user {
                eq "role", "user"
            }

            projections {
                groupProperty "user", "users"
                count "user", "count"
            }

            resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        }

    }

    static belongsTo = [user:User]

    static mapping = {
    	version false
    }

    String toString() {
        "the $dateOfApplication in $classroom from $user to $school"
    }

}