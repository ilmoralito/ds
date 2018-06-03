grails.config.locations = [AppConfig, DatashowConfig, DatashowAssignationConfig, SchoolConfig, ClassroomConfig]

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
        grails.serverURL = System.env.DEVELOPMENT_SERVER_URL
    }

    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = System.env.PRODUCTION_SERVER_URL
    }
}

log4j = {
    trace 'org.hibernate.type.descriptor.sql.BasicBinder'

    // debug 'org.hibernate.SQL', 'grails.app.controllers'

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
