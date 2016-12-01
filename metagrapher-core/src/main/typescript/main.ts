"use strict";

import "es6-shim";
import "zone.js";
import "reflect-metadata";
import "rxjs/Rx";


import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {AppComponent} from './components/app';

import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

@NgModule({
        imports:      [ BrowserModule ],
        declarations: [ AppComponent ],
        bootstrap:    [ AppComponent ]
})
class AppModule { }

document.addEventListener('DOMContentLoaded', function() {
        platformBrowserDynamic()
        .bootstrapModule(AppModule);
});
