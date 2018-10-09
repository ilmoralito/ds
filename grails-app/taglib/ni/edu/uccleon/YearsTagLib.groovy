package ni.edu.uccleon

import groovy.xml.*

class YearsTagLib {

    static namespace = 'years'

    def widget = { attrs ->
        MarkupBuilder builder = new MarkupBuilder(out)
        final String active = isGlobalActive()

        builder.div {
            ul(class: 'nav nav-pills nav-stacked') {
                li(class: "$active") {
                    a(href: createLink(action: 'record', id: attrs.id)) {
                        mkp.yield 'Global'
                    }
                }

                attrs.years.each { Integer year ->
                    final Boolean isActive = isActive(actionName: actionName, currentYear: attrs?.currentYear, year: year.toString())
                    final String status = isActive ? 'active' : ''

                    li(class: "$status") {
                        a(href: createLink(action: 'recordsByYear', params: [id: attrs.id, year: year])) {
                            mkp.yield year
                        }
                    }
                }
            }
        }
    }

    private String isGlobalActive() {
        actionName in ['record', 'recordsDetail'] ? 'active' : ''
    }

    private Boolean isActive(Map args) {
        args.actionName in ['recordsByYear', 'recordsDetailByYear'] && args.currentYear == args.year
    }
}
