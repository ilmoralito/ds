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
    dateOfApplication nullable:false, validator: {val, obj ->
      def today = new Date()
      def minDate = today + 2

      if (obj.type == "common") {
        val >= minDate.clearTime() ? true : "buildRequestCommand.dateOfApplication.validator"
      } else {
        val >= today.clearTime() ? true : "buildRequestCommand.dateOfApplication.validator"
      }
    }
    classroom blank:false, inList:Holders.config.ni.edu.uccleon.classrooms, maxSize:255
    school blank:false
    description nullable:true, maxSize:10000
    datashow nullable:true//, range:0..grailsApplication.config.ni.edu.uccleon.datashows
    type inList:["common", "express"], maxSize:255
    audio nullable:true
    screen nullable:true
    internet nullable:true
    status inList:["pending", "attended", "absent", "canceled"]
    hours nullable:true
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
  }

  String toString() {
    "the $dateOfApplication in $classroom from $user to $school"
  }

}