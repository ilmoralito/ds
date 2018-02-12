package ni.edu.uccleon

import grails.util.Holders

class User implements Serializable {
    String email
    String password = '123'
    String role = 'user'
    String fullName
    Boolean enabled = true

    Date dateCreated
    Date lastUpdated

    static constraints = {
        email email: true, unique: true, blank: false, validator: { email, user ->
            List<String> roles = Holders.config.ni.edu.uccleon.roles as List
            List<String> institutionalRoles = roles - 'user'

            if (user.role in institutionalRoles) {
                List<String> emailTokenized = email.tokenize("@")
                Boolean validUsername = emailTokenized[0].tokenize('.').size() == 2
                Boolean validDomainName = emailTokenized[1] == 'ucc.edu.ni'

                if (!validUsername || !validDomainName) {
                    'not.valid.email'
                }
            }
        }
        password blank: false
        role maxSize: 255, inList: Holders.config.ni.edu.uccleon.roles as List
        fullName blank: false
        schools nullable: false, minSize: 1
        classrooms nullable: false, minSize: 1
    }

    static namedQueries = {
        login { email, password ->
            eq 'enabled', true
            eq 'email', email
            eq 'password', password.encodeAsSHA1()
        }

        listByRole { role ->
            eq 'role', role
        }

        isEnabled { enabled ->
            eq 'enabled', true
        }

        hasAcademyAuthority {
            isEnabled()
            'in' 'role', ['asistente', 'coordinador']
        }

        search { criteria ->
            or {
                ilike 'fullName', criteria
                ilike 'email',  criteria
            }
        }
    }

    static hasMany = [schools: String, requests: Request, classrooms: String]

    static mapping = {
        version false
        sort 'dateCreated'
        role column: 'user_role'
        enabled column: 'user_status'
        schools joinTable: [name: 'user_schools']
        classrooms joinTable: [name: 'user_classrooms']
        requests sort: 'dateOfApplication', order: 'desc'
        schools cascade: 'all-delete-orphan'
        classrooms cascade: 'all-delete-orphan'
    }

    def beforeInsert() {
        password = password.encodeAsSHA1()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            password = password.encodeAsSHA1()
        }
    }

    String toString() { fullName }
}
