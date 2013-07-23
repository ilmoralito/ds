package ni.edu.uccleon

class UserClassroom {

    String classroom

	Date dateCreated
	Date lastUpdated

    static constraints = {
        classroom blank:false
    }

    static namedQueries = {

    }

    static belongsTo = [user:User]

    static mapping = {
    	version false
    }

    String toString() {
        name
    }

}