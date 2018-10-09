package ni.edu.uccleon

class UserResumeUtility {

    Map getResume(List<Map> dataset) {
        Map classroomResume = reduce(dataset.classroom)
        Map statusResume = reduce(dataset.status)
        Integer hours = getHoursInMonth(dataset)

        [classrooms: classroomResume, status: statusResume, hours: hours]
    }

    private Map reduce(List<String> collection) {
        Map result = collection.inject([:]) { result, value ->
            if (result[value]) {
                result[value] = result[value] + 1
            } else {
                result[value] = 1
            }

            result
        }

        result
    }

    private Integer getHoursInMonth(List<Map> collection) {
        List<String> blocks = getBlocksByStatus(collection, 'Atendido')
        Integer result = blocks.inject(0) { result, value ->
            result = result + value.tokenize(',').size()

            result
        }

        result
    }

    private List<String> getBlocksByStatus(List<Map> collection, final String status) {
        collection.findAll { it.status == status }.blocks
    }
}