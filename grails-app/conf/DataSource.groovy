dataSource {
    pooled                                       = true
    driverClassName                              = 'org.h2.Driver'
    username                                     = System.env.MYSQL_USERNAME
    password                                     = System.env.MYSQL_PASSWORD
}

hibernate {
    cache.use_second_level_cache                 = true
    cache.use_query_cache                        = false
    cache.region.factory_class                   = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}

environments {
    development {
        dataSource {
            url                                  = System.env.DS_DEVELOPMENT_DATABASE_URL
            dialect                              = org.hibernate.dialect.MySQL5InnoDBDialect
            driverClassName                      = 'com.mysql.jdbc.Driver'
            dbCreate                             = 'create-drop'
            formatSql                            = true
            logSql                               = true
        }
    }

    test {
        dataSource {
            dbCreate                             = 'update'
            url                                  = 'jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000'
        }
    }

    production {
        dataSource {
            dbCreate                             = 'none'
            driverClassName                      = 'com.mysql.jdbc.Driver'
            dialect                              = org.hibernate.dialect.MySQL5InnoDBDialect
            url                                  = System.env.DS_PRODUCTION_DATABASE_URL
            pooled                               = true
            properties {
                maxActive                        = -1
                minEvictableIdleTimeMillis       = 1800000
                timeBetweenEvictionRunsMillis    = 1800000
                numTestsPerEvictionRun           = 3
                testOnBorrow                     = true
                testWhileIdle                    = true
                testOnReturn                     = true
                validationQuery                  = 'SELECT 1'
            }
        }
    }
}
