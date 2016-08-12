package ni.edu.uccleon

import org.hibernate.transform.AliasToEntityMapResultTransformer
import grails.util.Holders

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

  String status = "pending"

  Date dateCreated
  Date lastUpdated

  static constraints = {
    dateOfApplication nullable: false, validator: { dateOfApplication ->
      Date date = new Date()

      dateOfApplication >= date.clearTime()
    }
    classroom blank: false, maxSize: 255, validator: { classroom, requestInstance ->
      classroom in requestInstance.user.classrooms
    }
    school blank: false, validator: { school, requestInstance ->
      school in requestInstance.user.schools
    }
    description nullable: true, maxSize: 10000
    datashow range: 1..Holders.config.ni.edu.uccleon.datashows.size(), validator: { datashow, requestInstance ->
      // validate when 
    }
    type inList: ["common", "express"], maxSize:255
    audio nullable: true
    screen nullable: true
    internet nullable: true
    status inList: ["pending", "attended", "absent", "canceled"]
    hours nullable: true
  }

  static namedQueries = {
    filter { users, schools, departments, classrooms, types, status = null, requestFromDate = null, requestToDate = null ->
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

    //reports
    requestsBy { property ->
      projections {
        groupProperty property, "property"
        count property, "count"
      }

      order("count", "desc")
      resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
    }

    requestsByBlocks {
      projections {
        hours {
          groupProperty "block", "property"
          count "block", "count"
        }
      }

      order("count", "desc")
      resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
    }
  }

  static belongsTo = [user:User]
  static hasMany = [hours:Hour]

  static mapping = {
    version false
    hours cascade: "all-delete-orphan"
  }

  Boolean hasDetails() {
    if (audio || screen || internet || description) {
      true
    }
  }

  String toString() { dateOfApplication }
}
