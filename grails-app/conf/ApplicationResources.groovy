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
    resource url:"js/requestList.js"
  }
}
