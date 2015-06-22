package ni.edu.uccleon

import grails.util.Holders

class User implements Serializable {
  String email
  String password = "123"
  String role = "user"
  String fullName
  Boolean enabled = true

	Date dateCreated
	Date lastUpdated

  static constraints = {
    email email:true, unique:true, blank:false
    password blank:false
    role maxSize:255, inList:Holders.config.ni.edu.uccleon.roles as List
    fullName blank:false
    schools validator: { schools, user ->
      if (!schools?.size()) {
        "notValid"
      }
    }
    classrooms validator: { classrooms, user ->
      if (!classrooms?.size()) {
       "notValid"
      }
    }
  }

  static namedQueries = {
    login {email, password ->
      eq "email", email
      eq "password", password.encodeAsSHA1()
    }

    listByRole {role ->
      eq "role", role
    }

    isEnabled {enabled ->
      eq "enabled", true
    }

    search { criteria ->
      or {
        ilike "fullName", criteria
        ilike "email",  criteria
      }
    }
  }

  static hasMany = [schools:String, requests:Request, classrooms:String]

  static mapping = {
    sort "dateCreated"
    role column: "user_role"
    enabled column: "user_status"
    requests sort: 'dateOfApplication', order: 'desc'
    version false
    schools joinTable: [name: "user_schools"]
    classrooms joinTable: [name: "user_classrooms"]
  }

  def beforeInsert() {
    password = password.encodeAsSHA1()
  }

  def beforeUpdate() {
    if (isDirty("password")) {
      password = password.encodeAsSHA1()
    }
  }

  String toString() {
    "$fullName ($email)"
  }
}
