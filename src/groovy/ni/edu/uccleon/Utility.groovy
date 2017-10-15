package ni.edu.uccleon

import static java.util.Calendar.*

class Utility {

    public static final List<String> MONTHLIST = [
        'Enero',
        'Febrero',
        'Marzo',
        'Abril',
        'Mayo',
        'Junio',
        'Julio',
        'Agosto',
        'Septiembre',
        'Octubre',
        'Noviembre',
        'Diciembre'
    ]

    public static final Map<String, String> STATUS = [
        pending: 'Pendiente',
        attended: 'Atendido',
        absent: 'Sin retirar',
        canceled: 'Cancelado'
    ]

    public static final Integer getDayOfWeek(final Date date) {
        date[DAY_OF_WEEK]
    }
}