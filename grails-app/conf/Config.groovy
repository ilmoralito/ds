// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

//grails.config.locations = ["file:${userHome}"/grails/${appName}-settings.groovy]

grails.project.groupId = "ni.edu.uccleon" // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
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

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "html" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "http://uccds.herokuapp.com"
    }
}

// log4j configuration
log4j = {
    debug 'grails.app.services.com.grailsrocks.emailconfirmation'
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

ni.edu.uccleon.schoolsAndDepartments = [
  schools:["Ciencias Economicas Empresariales", "Administracion Turistica y Hotelera", "Ciencias Juridicas Empresariales", "Ciencias Agragrias", "Ingenieria e Informatica", "Estudios por encuentro superior", "Arquitectura"],
  departments:["Especializacion", "Administracion", "Protocolo", "Promotoria", "Direccion Academica", "Vice Rectoria General", "Registro", "Recursos humanos", "Vida Estudiantil", "Desarrollo Proyeccion", "Transporte", "Escuela de manejo"]
]
ni.edu.uccleon.classrooms = ["Afuera de UCC", "Auditorio mayor", "Auditorio menor", "C103", "C104", "C105", "C106", "C109A", "C109B", "Biblioteca", "C202", "C203", "C204", "C205", "Laboratorio 4", "Laboratorio 3", "Laboratorio 2", "Corredor registro", "D101", "D102", "D103", "D104", "D105", "D109", "D201", "D202", "D203", "D204", "D205", "D206", "D207", "Laboratorio 1", "Desarrollo y proyeccion", "E108", "E112", "Administracion", "E115", "E116", "E117", "E118", "Sala de reuniones", "E208", "E209", "E210", "E211", "E212", "E213", "E214", "E215", "E216", "E217", "E218", "E219", "Escuela de manejo", "K103", "K104", "K105", "K201", "K202", "Mesanini B201", "Mesanini B202"]
ni.edu.uccleon.cls = [
  B:[[code:"B101", name:"Auditorio mayor"],[code:"B201", name:"Mesanini B201"], [code:"B202", name:"Mesanini B202"]],
  C:[
      [code:"C101", name:"Auditorio menor"], [code:"C102", name:"Desarrollo y proyeccion"],
      [code:"C103"], [code:"C104"], [code:"C105"], [code:"C106"], [code:"C109A"], [code:"C109B"],
      [code:"C201", name:"Biblioteca"], [code:"C202"], [code:"C203"], [code:"C204"], [code:"C205"],
      [code:"C206", name:"Lab 4"], [code:"C207", name:"Lab 3"], [code:"C208", name:"Lab 2"]
  ],
  D:[[code:"D101"], [code:"D102"], [code:"D103"], [code:"D104"], [code:"D105"], [code:"D109"], [code:"D201"], [code:"D202"], [code:"D203"], [code:"D204"], [code:"D205"], [code:"D206"], [code:"D207"], [code:"D208", name:"Lab 1"]],
  E:[[code:"E108"], [code:"E112"], [code:"E113", name:"Finanzas"], [code:"E114", name:"Administracion"], [code:"E115"], [code:"E116"], [code:"E117"], [code:"E118"], [code:"E119", name:"Sala de maestros"], [code:"E204", name:"Sala de reuniones"], [code:"E208"], [code:"E209"], [code:"E210"], [code:"E211"], [code:"E212"], [code:"E213"], [code:"E214"], [code:"E215"], [code:"E216"], [code:"E217"], [code:"E218"], [code:"E219"], [code:"E220", name:"Escuela de manejo"]],
  K:[[code:"K103"], [code:"K104"], [code:"K105"], [code:"K201"], [code:"K202"], [code:"K203"]],
  undefined:[[code:"Afuera de UCC"], [code:"Corredor registro"]]
]
ni.edu.uccleon.blocks = 6
ni.edu.uccleon.saturday.blocks = 3
ni.edu.uccleon.sunday.blocks = 2
ni.edu.uccleon.datashows = 5
ni.edu.uccleon.week.datashows = 5
ni.edu.uccleon.saturday.datashows = 8
ni.edu.uccleon.sunday.datashows = 8

ni.edu.uccleon.speakers = 2
ni.edu.uccleon.screens = 3

grails.plugins.twitterbootstrap.fixtaglib = true

grails.mail.default.from="mario.martinez@ucc.edu.ni"

grails {
  mail {
    host = "smtp.gmail.com"
    port = 465
    username = System.env.GMAIL_USERNAME
    password = System.env.GMAIL_PASSWORD
    props = [
      "mail.smtp.auth":"true",
      "mail.smtp.socketFactory.port":"465",
      "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
      "mail.smtp.socketFactory.fallback":"false"
    ]
  }
}