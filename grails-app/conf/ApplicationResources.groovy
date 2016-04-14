modules = {
  application {
    resource url:"js/application.js"
  }

  app {
    resource url:"css/app.css"
  }

  overrides {
    "jquery-theme" {
      resource id:"theme", url:"/css/custom-theme/jquery-ui-1.10.3.custom.min.css"
    }
  }

  datepicker {
    dependsOn "jquery"
    resource url:"js/app.js"
  }

  requestList {
    dependsOn "jquery"
    resource url:"js/users.js"
    resource url:"js/requestList.js"
  }

  activity {
    resource url:"js/users.js"
    resource url:"js/requestDetails.js"
  }

  createUser {
    dependsOn "app"
    dependsOn "jquery"
    resource url:"js/jquery.details.min.js"
    resource url:"js/details.js"
  }

  users {
    dependsOn "jquery"
    dependsOn "app"
    resource url: "js/users.js"
    resource url: "js/FilterBox.js"
  }

  getUserClassroomsAndSchools {
    dependsOn "app"
    resource url: "js/getUserClassroomsAndSchools.js"
  }

  roster {
    dependsOn "app"
    resource url: "css/sweetalert.css"
    resource url: "js/sweetalert.min.js"
    resource url: "js/roster.js"
    resource url: "js/FilterBox.js"
  }

  classrooms {
    dependsOn "app"
    resource url: "js/classrooms.js"
  }

  userShow {
    dependsOn "app"
    resource url: "components/clipboard/dist/clipboard.min.js"
    resource url: "js/copy.js"
    resource url: "js/updateUserRole.js"
    resource url: "js/updateUserSchools.js"
    resource url: "js/updateUserEnabledProperty.js"
  }
}
