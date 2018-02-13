package ni.edu.uccleon

import org.hibernate.transform.AliasToEntityMapResultTransformer

class Request implements Serializable {
    Date dateOfApplication
    String classroom
    String school
    String description
    Integer datashow
    String type = "express"
    Boolean audio
    Boolean screen
    Boolean internet
    Boolean pointer
    Boolean cpu
    String status = "pending"

    Date dateCreated
    Date lastUpdated

    static constraints = {
        dateOfApplication nullable: false, validator: { dateOfApplication ->
            Date today = new Date()

            dateOfApplication >= today.clearTime()
        }
        classroom blank: false, maxSize: 255
        school blank: false
        description nullable: true, maxSize: 10000
        datashow blank: false
        type inList: ["common", "express"], maxSize:255
        audio nullable: true
        screen nullable: true
        internet nullable: true
        pointer nullable: true
        cpu nullable: true
        status inList: ["pending", "attended", "absent", "canceled"]
        hours nullable: false, minSize: 1
    }

    static namedQueries = {
        filter { users, schools, departments, classrooms, status = null ->
            if (users) {
                user {
                    "in" "email", users
                }
            }

            if (schools || departments) {
                "in" "school", schools + departments
            }

            if (classrooms) {
                "in" "classroom", classrooms
            }

            if (status) {
                "in" "status", status
            }
        }

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
            ge "dateOfApplication", from.clearTime()
            le "dateOfApplication", to.clearTime()
        }

        // Reports
        requestsBy { property ->
            projections {
                groupProperty property, "property"
                count property, "count"
            }

            order("count", "desc")
            resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        }
    }

    static belongsTo = [user: User]

    static hasMany = [hours: Hour]

    static mapping = {
        hours cascade: "all-delete-orphan"
        version false
    }

    Boolean hasDetails() {
        if (audio || screen || internet || pointer || cpu || description) {
            true
        }
    }

    String toString() { dateOfApplication }
}
