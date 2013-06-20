import org.cloudfoundry.runtime.env.CloudEnvironment
import org.cloudfoundry.runtime.env.RdbmsServiceInfo

def cloudEnv = new CloudEnvironment()

dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:file:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            pooled = true
            //url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            if (cloudEnv.isCloudFoundry()) {
                def dbSvcInfo = cloudEnv.getServiceInfos(RdbmsServiceInfo.class)
                if (dbSvcInfo.size() > 0) {
                    url = dbSvcInfo[0].url
                    username = dbSvcInfo[0].userName
                    password = dbSvcInfo[0].password

                    if (url.startsWith("jdbc:mysql"))
                        driverClassName = "com.mysql.jdbc.Driver"
                    else if (url.startsWith("jdbc:postgres"))
                        driverClassName = "org.postgresql.Driver"
                }
            } else {
                url = "jdbc:postgresql://localhost:5432/petclinic"
            }

            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
