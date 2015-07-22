module.exports = function(grunt) {
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),

		jshint: {
			options: {
				ignores: ['web-app/js/**/*.min.js']
			},
			all: ['Gruntfile.js', 'web-app/js/**/*.js']
		},

		uglify: {
			options: {
        banner: '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
      },
      dist: {
				files: {
					'web-app/js/dest/login.min.js': ['web-app/js/login.js']
				}
      }
		}
	});

	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-uglify');

	grunt.registerTask('default', ['jshint', 'uglify']);
};