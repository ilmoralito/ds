package ni.edu.uccleon

import org.hibernate.transform.AliasToEntityMapResultTransformer

class Request {

    //def grailsApplication

    Date dateOfApplication
    String classroom
    String school
    String description
    Integer datashow
    String type

    Boolean audio
    Boolean screen
    Boolean internet

    Boolean enabled = false

	Date dateCreated
	Date lastUpdated

    static constraints = {
        dateOfApplication blank:false, validator: {val, obj ->
            def today = new Date()
            def minDate = today + 2

            if (obj.type == "common") {
                return val >= minDate.clearTime()
            } else {
                return val >= today.clearTime()
            }
        }
        classroom blank:false
        school blank:false
        description nullable:true, maxSize:10000
        datashow nullable:true//, range:0..grailsApplication.config.ni.edu.uccleon.datashows
        type inList:["common", "express"], maxSize:255
        audio nullable:true
        screen nullable:true
        internet nullable:true
    }

    static namedQueries = {
        listByUser {user ->
            eq "user", user
        }

        listByRole {role ->
            user {
                eq "role", role
            }
        }

        todayRequest {
            def today = new Date()

            ge "dateOfApplication", today.clearTime()
            le "dateOfApplication", today.clearTime()
        }

        requestFromTo { from, to ->
            def f = new Date().parse("yyyy-MM-dd", from)
            def t = new Date().parse("yyyy-MM-dd", to)

            ge "dateOfApplication", f.clearTime()
            le "dateOfApplication", t.clearTime()
        }

        //reports
        requestsBy { property ->
            projections {
                groupProperty property, "property"
                count property, "count"
            }

            resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        }

        requestsByBlocks {
            projections {
                hours {
                    groupProperty "block", "property"
                    count "block", "count"
                }
            }

            resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        }

    }

    static belongsTo = [user:User]
    static hasMany = [hours:Hour]

    static mapping = {
    	version false
    }

    String toString() {
        "the $dateOfApplication in $classroom from $user to $school"
    }

}