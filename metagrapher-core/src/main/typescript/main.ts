"use strict";

import * as $ from "jquery";
(<any>global)['jQuery'] = $;
import "bootstrap";


import "es6-shim";
import "zone.js";
import "reflect-metadata";
import "rxjs/Rx";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./components/app.component";
import {ControlBoxComponent} from "./components/domain/control-box.component";
import {InstanceListGroupComponent} from "./components/domain/instance-group.component";
import {PanelComponent} from "./components/generic/panel.component";
import {ListPanelComponent} from "./components/generic/list-panel.component";
import {ListPanelItemComponent} from "./components/generic/list-panel-item.component";

const cytoscape = require("cytoscape");

console.log(cytoscape);

@NgModule({
    imports: [BrowserModule],
    declarations: [
        AppComponent,
        ControlBoxComponent,
        InstanceListGroupComponent,
        PanelComponent,
        ListPanelComponent,
        ListPanelItemComponent
    ],
    bootstrap: [AppComponent]
})
class AppModule {
}



document.addEventListener('DOMContentLoaded', function () {
    platformBrowserDynamic()
        .bootstrapModule(AppModule);
});
