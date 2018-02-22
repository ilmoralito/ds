//grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.work.dir = "target"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.war.file = "target/${appName}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        mavenLocal()
        mavenCentral()
        grailsCentral()
        grailsPlugins()
        grailsHome()
        //mavenRepo 'http://repo.grails.org/grails'
        mavenRepo 'https://repo.grails.org/grails/plugins'

        //mavenRepo "http://maven.springframework.org/milestone/"

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        compile "org.grails:grails-webflow:$grailsVersion"
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        runtime 'mysql:mysql-connector-java:5.1.38'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.8.3"
        //runtime ":resources:1.2"
        //runtime ":resources:1.1.6"
        //runtime ":resources:1.2.7" ok in local
        runtime ':resources:1.2.14'

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.5"

        build ":tomcat:$grailsVersion"

        runtime ":database-migration:1.3.2"

        compile ':cache:1.0.1'

        //app plugins
        runtime 'org.grails.plugins:external-config-reload:1.4.1'
        compile ':twitter-bootstrap:2.3.0'
        compile ':jquery-ui:1.8.24'
        compile ':webflow:2.0.8.1'
        runtime ':console:1.5.11'
        compile ':quartz:1.0.2'
        compile ':webxml:1.4.1'
        compile ':mail:1.0.6'
    }
}
