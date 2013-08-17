package ni.edu.uccleon

class School implements Serializable {

    String name

	Date dateCreated
	Date lastUpdated

    static constraints = {
        name blank:false
    }

    static namedQueries = {

    }

    static belongsTo = [user:User]

    static mapping = {
        sort "name"
    	version false
    }

    String toString() {
        name
    }

}