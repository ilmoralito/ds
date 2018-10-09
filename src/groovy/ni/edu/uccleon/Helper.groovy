package ni.edu.uccleon

class Helper {
    public List<Map> getStatusList() {
        final List<Map> statusList = [
            [
                english: 'pending',
                spanish: 'Pendiente'
            ],[
                english: 'attended',
                spanish: 'Atendido'
            ],[
                english: 'absent',
                spanish: 'Sin retirar'
            ],[
                english: 'canceled',
                spanish: 'Cancelado'
            ],
        ]

        statusList
    }

    public List<Map> getStatusListExcept(final String status) {
        final List<Map> statusList = getStatusList();

        statusList.findAll { Map state -> state.english != status }
    }

    public List<String> getEnglishStatusList() {
        final List<Map> statusList = getStatusListExcept('pending')

        statusList.english
    }

    public String getStatusInSpanish(final String status) {
        final List<Map> statusList = getStatusList()

        statusList.find { it.english == status }.spanish
    }

    public List<String> months() {
        ['December', 'November', 'October', 'September', 'August', 'July', 'June', 'May', 'April', 'March', 'Febrary', 'January']
    }
}