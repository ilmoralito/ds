package ni.edu.uccleon

class Hour implements Serializable {

    Integer block

	Date dateCreated
	Date lastUpdated

    static constraints = {
        block blank:false
    }

    static belongsTo = [request:Request]

    static mapping = {
    	version false
    }

    String toString() {
        "block:$block"
    }

}