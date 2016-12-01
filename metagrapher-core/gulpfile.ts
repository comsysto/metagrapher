import * as gulp from "gulp";
import * as browserify from "browserify";
import * as source from "vinyl-source-stream";
import * as watchify from "watchify";
import * as gulpUtil from "gulp-util";
import * as uglify from "gulp-uglify";
import * as  sourcemaps from "gulp-sourcemaps";
import * as  buffer from "vinyl-buffer";

var tsify: any = require("tsify");


const config = {
    mainPath: "src/main/typescript/main.ts",
    baseDir: ".",
    distFile: 'application.js',
    distDir: "target/classes/static/js",
    libDir: "target/classes/static/js"
};

function browerifyInit() {
    return browserify({
        basedir: config.baseDir,
        debug: true,
        entries: [config.mainPath],
        cache: {},
        packageCache: {}
    })

}

function bundle(browserifyLike: any) {

    return browserifyLike
        .bundle()
        .on('error', function (err: any) {
            gulpUtil.log(gulpUtil.colors.bgRed("ERROR:"), " ", err.toString());
            // this.emit('end');
        })
        .pipe(source(config.distFile))
        .pipe(buffer())
        .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(uglify())
        .pipe(sourcemaps.write('./'))
        .pipe(gulp.dest(config.distDir));
}

function configureTsify(browserifyLike: any) {
    return browserifyLike.plugin(tsify, {
        module: "commonjs",
        target: "es5",
        path: "tsconfig.json"
    })
}

gulp.task("build:bundle", () => {
    bundle(configureTsify(browerifyInit()));
});

gulp.task("build:watch", () => {
    var watchedBrowserify = configureTsify(watchify(browerifyInit()));
    bundle(watchedBrowserify);

    watchedBrowserify.on("update", () => bundle(watchedBrowserify));
    watchedBrowserify.on("log", gulpUtil.log);
});

gulp.task("watch", ["build:watch"]);

gulp.task("default", ["build:bundle"]);

