package ni.edu.uccleon

class User implements Serializable {

    String email
    String password
    String role = "user"
    String fullName
    Boolean enabled = false

	Date dateCreated
	Date lastUpdated

    static constraints = {
        email email:true, unique:true, blank:false
        password blank:false
        role inList:['admin', 'user'], maxSize:255
        fullName blank:false
        schools nullable:true
        classrooms nullable:true
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