modules = {
    application {
        resource url: 'css/application.css'
        resource url: 'js/Chart.min.js'
        resource url: 'js/application.js'
    }

    app {
        dependsOn 'jquery'
        resource url: 'css/app.css'
    }

    base {
        dependsOn 'jquery'
        resource url: 'css/base.css'
    }

    publicActivity {
        dependsOn 'app'
        // resource url: 'js/axios.min.js'
        // resource url: 'js/check.js'
        resource url: 'js/modal.js'
        resource url: 'css/vendors/font-awesome/css/font-awesome.min.css'
    }

    overrides {
        'jquery-theme' {
            resource id: 'theme', url:'/css/custom-theme/jquery-ui-1.10.3.custom.min.css'
        }
    }

    datepicker {
        dependsOn 'jquery'
        resource url: 'js/app.js'
    }

    requestList {
        dependsOn 'app'

        resource url: 'js/requestListFilter.js'
        resource url: 'js/requestList.js'
    }

    activity {
        dependsOn 'app'

        resource url: 'js/users.js'
        resource url: 'css/animate.css'
        resource url: 'js/requestDetails.js'
    }

    users {
        dependsOn 'app'

        resource url: 'js/users.js'
        resource url: 'js/FilterBox.js'
    }

    players {
        resource url: 'css/app.css'
        resource url: 'js/filter.js'
    }

    buildRequest {
        dependsOn 'app'
        resource url: 'js/popover.js'
        resource url: 'js/getUserClassroomsAndSchools.js'
    }

    roster {
        dependsOn 'app'
        resource url: 'css/sweetalert.css'
        resource url: 'js/sweetalert.min.js'
        resource url: 'js/roster.js'
        resource url: 'js/FilterBox.js'
    }

    // TODO: Try to use one solution for both cases
    classrooms {
        dependsOn 'app'
        resource url: 'js/classrooms.js'
    }

    adminUserClassrooms {
        dependsOn 'app'
        resource url: 'js/axios.min.js'
        resource url: 'js/adminUserClassrooms.js'
    }

    userShow {
        dependsOn 'app'
        resource url: 'components/clipboard/dist/clipboard.min.js'
        resource url: 'js/copy.js'
        resource url: 'js/updateUserEnabledProperty.js'
    }

    requestShow {
        dependsOn 'app'
        resource url: 'js/cloneRequest.js'
    }

    filter {
        dependsOn 'app'
        resource url: 'js/filterPendingRequest.js'
    }
}
