grails.project.groupId = 'ni.edu.uccleon'
grails.mime.file.extensions = true
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*', '/components/*', '/libs/*']
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/*', '/plugins/**', '/components/**', '/libs/**']

grails.views.default.codec = 'html'
grails.views.gsp.encoding = 'UTF-8'
grails.converters.encoding = 'UTF-8'
grails.views.gsp.sitemesh.preprocess = true
grails.scaffolding.templates.domainSuffix = 'Instance'

grails.json.legacy.builder = false
grails.enable.native2ascii = true
grails.spring.bean.packages = []
grails.web.disable.multipart=false

grails.exceptionresolver.params.exclude = ['password']

grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = System.env.SERVER_URL
    }
}

log4j = {
    // trace 'org.hibernate.type.descriptor.sql.BasicBinder'

    // debug 'org.hibernate.SQL',
    //      'grails.app.controllers'

    error  'org.codehaus.groovy.grails.web.servlet',
           'org.codehaus.groovy.grails.web.pages',
           'org.codehaus.groovy.grails.web.sitemesh',
           'org.codehaus.groovy.grails.web.mapping.filter',
           'org.codehaus.groovy.grails.web.mapping',
           'org.codehaus.groovy.grails.commons',
           'org.codehaus.groovy.grails.plugins',
           'org.codehaus.groovy.grails.orm.hibernate',
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

ni {
    edu {
        uccleon {
            firstUserInformation = [
                fullName: 'FirstName LastName',
                schools: ['school1', 'school2'],
                classrooms: ['classroom1', 'classroom2']
            ]

            roles = ['admin', 'user', 'coordinador', 'asistente', 'administrativo', 'supervisor']

            data = [
                [
                    coordination: 'Industrial',
                    datashow: [[1, 4], 1, 1, 1, 1, 1, 1]
                ],[
                    coordination: 'CCEE',
                    datashow: [[2, 3], [2, 3], [2, 3], [2, 3], [2, 3], [2, 3], [2, 3]]
                ],[
                    coordination: 'Arquitectura y civil',
                    datashow: [null, 4, 4, 4, 4, 4, 4]
                ],[
                    coordination: 'Sistemas y diseno grafico',
                    datashow: [null, 5, 5, 5, 5, 5, 5]
                ],[
                    coordination: 'Ingles',
                    datashow: [null, 6, 6, 6, 6, 6, 6]
                ],[
                    coordination: 'Agronomia',
                    datashow: [null, 7, 7, 7, 7, 7, 7]
                ],[
                    coordination: 'Derecho',
                    datashow: [1, null, null, null, null, null, 9]
                ],[
                    coordination: 'Especializacion',
                    datashow: [
                        [5, 6, 7, 8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [4, 5, 6, 7, 8, 9, 10, 11, 12],
                    ]
                ],[
                    coordination: 'Educacion continua',
                    datashow: [
                        [5, 6, 7, 8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [4, 5, 6, 7, 8, 9, 10, 11, 12],
                    ]
                ],[
                    coordination: 'Administracion',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Protocolo',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Promotoria',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Direccion Academica',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Vice Rectoria General',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Registro',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Recursos humanos',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Vida Estudiantil',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Desarrollo Proyeccion',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Transporte',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Escuela de manejo',
                    datashow: [null, 6, 6, 6, 6, 6, 6]
                ],[
                    coordination: 'Direccion Financiera',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Delegacion de la sede',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ],[
                    coordination: 'Desarrollo Curricular',
                    datashow: [
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12],
                        [8, 9, 10, 11, 12]
                    ]
                ]
            ]

            schoolsAndDepartments = [
                schools: [
                    'Industrial',
                    'CCEE',
                    'Arquitectura y civil',
                    'Sistemas y diseno grafico',
                    'Ingles',
                    'Agronomia',
                    'FESE',
                    'Derecho'
                ],
                departments: [
                    'Especializacion',
                    'Educacion continua',
                    'Administracion',
                    'Protocolo',
                    'Promotoria',
                    'Direccion academica',
                    'Vice rectoria general',
                    'Registro',
                    'Recursos humanos',
                    'Vida estudiantil',
                    'Desarrollo y proyeccion',
                    'Transporte',
                    'Escuela de manejo',
                    'Direccion financiera',
                    'Soporte tecnico',
                    'Delegacion de la sede',
                    'Desarrollo Curricular'
                ]
            ]

            cls = [
                B: [
                    [code: 'B101', name: 'Auditorio mayor'],
                    [code: 'B201', name: 'Mesanini 1', wifi: true],
                    [code: 'B202', name: 'Mesanini 2', wifi: true]
                ],
                C: [
                    [code: 'C101', name: 'Auditorio menor', wifi: true],
                    [code: 'C102', name: 'Especializacion'],
                    [code: 'C103'],
                    [code: 'C104'],
                    [code: 'C105'],
                    [code: 'C106'],
                    [code: 'C109A', wifi: true],
                    [code: 'C109B'],
                    [code: 'C201', name: 'Biblioteca', wifi: true],
                    [code: 'C202', wifi: true],
                    [code: 'C203'],
                    [code: 'C204'],
                    [code: 'C205', wifi: true],
                    [code: 'C206', name: 'Lab 4'],
                    [code: 'C207', name: 'Lab 3'],
                    [code: 'C208', name: 'Lab 2']
                ],
                D: [
                    [code: 'D101'],
                    [code: 'D102'],
                    [code: 'D103'],
                    [code: 'D104'],
                    [code: 'D105'],
                    [code: 'D109', name: 'Audiovisuales', wifi: true],
                    [code: 'D201'],
                    [code: 'D202'],
                    [code: 'D203'],
                    [code: 'D204'],
                    [code: 'D205'],
                    [code: 'D206'],
                    [code: 'D207'],
                    [code: 'D208', name: 'Lab 1']
                ],
                E: [
                    [code: 'E108'],
                    [code: 'E112', wifi: true],
                    [code: 'E113', name: 'CCEE'],
                    [code: 'E114', name: 'CCEE'],
                    [code: 'E115'],
                    [code: 'E116'],
                    [code: 'E117'],
                    [code: 'E118'],
                    [code: 'E119', name: 'Sala de maestros', wifi: true],
                    [code: 'E204', name: 'Sala de reuniones', wifi: true],
                    [code: 'E208'],
                    [code: 'E209'],
                    [code: 'E210'],
                    [code: 'E211'],
                    [code: 'E212'],
                    [code: 'E213', name: 'Proyecto', wifi: true],
                    [code: 'E214'],
                    [code: 'E215'],
                    [code: 'E216'],
                    [code: 'E217'],
                    [code: 'E218'],
                    [code: 'E219'],
                    [code: 'E220', name: 'Escuela de manejo']
                ],
                K: [
                    [code: 'K103'],
                    [code: 'K104'],
                    [code: 'K105'],
                    [code: 'K201'],
                    [code: 'K202'],
                    [code: 'K203']
                ],
                undefined: [
                    [code: 'Afuera de UCC'],
                    [code: 'Corredor registro'],
                    [code: 'Piscina']
                ]
            ]

            datashows = [
                [
                    trademark: 'EPSON',
                    model: 'H692A',
                    serialNumber: 'VU3K6402339',
                    code: 1,
                    hdmi: true,
                    wifi: true,
                    ethernet: true,
                    enabled: true,
                ],[
                    trademark: 'EPSON',
                    model: 'H573A',
                    serialNumber: 'U3SK4400328',
                    code: 2,
                    hdmi: true,
                    wifi: true,
                    enabled: true,
                ],[
                    trademark: 'EPSON',
                    model: 'H430A',
                    serialNumber: 'PSPK2302445',
                    code: 3,
                    enabled: true
                ],[
                    trademark: 'EPSON',
                    model: 'H430A',
                    serialNumber: 'PSPK2302494',
                    code: 4,
                    enabled: true
                ],[
                    trademark: 'BENQ',
                    model: 'MS521P',
                    serialNumber: 'PD6CD01995000',
                    code: 5,
                    hdmi: true,
                    enabled: true
                ],[
                    trademark: 'BENQ',
                    model: 'MS504',
                    serialNumber: 'PDCCD01514000',
                    code: 6,
                    enabled: true
                ],[
                    trademark: 'EPSON',
                    model: 'H430A',
                    serialNumber: 'PSPK2302433',
                    code: 7,
                    enabled: true
                ],[
                    trademark: 'EPSON',
                    model: 'H692A',
                    serialNumber: 'VU3K6401282',
                    code: 8,
                    hdmi: true,
                    wifi: true,
                    ethernet: true,
                    enabled: true
                ],[
                    trademark: 'BENQ',
                    model: 'MS521P',
                    serialNumber: 'PD6CD02163000',
                    code: 9,
                    hdmi: true,
                    enabled: true,
                    observation: [
                        'Este equipo presenta suciedad en la imagen'
                    ]
                ],[
                    trademark: 'BENQ',
                    model: 'MX662',
                    serialNumber: 'PDH8D02670000',
                    code: 10,
                    hdmi: true,
                    enabled: true
                ],[
                    trademark: 'EPSON',
                    model: 'H573A',
                    serialNumber: 'U3SK4X07310',
                    code: 11,
                    hdmi: true,
                    wifi: true,
                    enabled: true
                ],[
                    trademark: 'EPSON',
                    model: 'H573A',
                    serialNumber: 'U3SK4X07317',
                    code: 12,
                    hdmi: true,
                    wifi: true,
                    enabled: true
                ]
            ]

            requestStatus = [
                [english: 'pending', spanish: 'Pendiente'],
                [english: 'attended', spanish: 'Atendido'],
                [english: 'absent', spanish: 'Sin retirar'],
                [english: 'canceled', spanish: 'Cancelado']
            ]

            specialization.saturday.blocks = 6
            specialization.sunday.blocks = 6

            saturday.blocks = 3
            sunday.blocks = 3
            blocks = 6

            computers = 1
            speakers = 2
            pointers = 2
            screens = 2
        }
    }
}

grails.mail.default.from=System.env.GMAIL_USERNAME

grails {
    mail {
        host = 'smtp.gmail.com'
        port = 465
        username = System.env.GMAIL_USERNAME
        password = System.env.GMAIL_PASSWORD
        props = [
            'mail.smtp.auth': 'true',
            'mail.smtp.socketFactory.port': '465',
            'mail.smtp.socketFactory.class': 'javax.net.ssl.SSLSocketFactory',
            'mail.smtp.socketFactory.fallback': 'false'
        ]
    }
}