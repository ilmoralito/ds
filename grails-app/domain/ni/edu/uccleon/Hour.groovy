package ni.edu.uccleon

import static java.util.Calendar.*

class Hour implements Serializable {
    def requestService

    Integer block

    Date dateCreated
    Date lastUpdated

    Hour(Integer block) {
        this()
        this.block = block
    }

    static constraints = {
        block min: 0, validator: { block, obj ->
            String school = obj.request.school
            Integer dayOfWeek = obj.request.dateOfApplication[DAY_OF_WEEK]
            Integer blocks = obj.requestService.getDayOfWeekBlocks(dayOfWeek, school)

            block in 0..blocks
        }
    }

    static belongsTo = [request: Request]

    static mapping = {
        version false
    }

    String toString() { block }
}
