module.exports = function(grunt) {
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),

		jshint: {
			options: {
				ignores: ['web-app/js/**/*.min.js']
			},
			all: ['Gruntfile.js', 'web-app/js/**/*.js']
		}
	});

	grunt.loadNpmTasks('grunt-contrib-jshint');

	grunt.registerTask('default', ['jshint']);
};