import * as gulp from "gulp";
import * as browserify from "browserify";
import * as source from "vinyl-source-stream";
import * as watchify from "watchify";
import * as gulpUtil from "gulp-util";
var tsify: any = require("tsify");


const config = {
    mainPath: "src/main/typescript/main.ts",
    baseDir: ".",
    distFile: 'metagrapher.js',
    distDir: "target/classes/static/js"
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

function bundle(browserifyLike) {

    return browserifyLike
        .bundle()
        .on('error', function (err) {
            gulpUtil.log(gulpUtil.colors.bgRed("ERROR:"), " " ,  err.toString());
            // this.emit('end');
        })
        .pipe(source(config.distFile))
        .pipe(gulp.dest(config.distDir));
}

function configureTsify(browserifyLike) {
    return browserifyLike.plugin(tsify, {
        module: "commonjs",
        target: "es5",
        jsx: "react"
    })
}

gulp.task("bundle", () => {
    bundle(configureTsify(browerifyInit()));
});


gulp.task("watch", () => {
    var watchedBrowserify = configureTsify(watchify(browerifyInit()));
    bundle(watchedBrowserify);

    watchedBrowserify.on("update", () => bundle(watchedBrowserify));
    watchedBrowserify.on("log", gulpUtil.log);
});
