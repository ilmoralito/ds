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
    dependsOn "jquery"
    resource url:"js/users.js"
    //resource url:"js/activity.js"
  }

  createUser {
    dependsOn "app"
    dependsOn "jquery"
    resource url:"js/jquery.details.min.js"
    resource url:"js/details.js"
  }

  reports {
    dependsOn "app"
    resource url:"js/reports.js"
  }

  users {
    dependsOn "jquery"
    dependsOn "app"
    resource url:"js/users.js"
  }

  getUserClassroomsAndSchools {
    dependsOn "app"
    resource url: "js/getUserClassroomsAndSchools.js"
  }

  roster {
    dependsOn "app"
    resource url: "js/roster.js"
  }
}
