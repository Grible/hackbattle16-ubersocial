'use strict';

var gulp = require('gulp'),
	$ = require('gulp-load-plugins')();

gulp.task('styles', function () {
  var sassOptions = {
    style: 'expanded',
    indentedSyntax: true,
    includePaths: ['../public/components']
  };

  return gulp.src('sass/styles.sass')
    .pipe($.sass(sassOptions).on('error', $.sass.logError))
    .pipe($.autoprefixer())
    .pipe(gulp.dest('../public/css'));
});

gulp.task('watch', ['styles'], function() {
  gulp.watch(['sass/*.{sass,scss}'], ['styles']);
});

gulp.task('default', function () {
  gulp.start(['watch']);
});
