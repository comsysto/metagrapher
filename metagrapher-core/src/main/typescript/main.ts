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
import {GraphComponent} from "./components/domain/graph.component";
import {GraphDataService} from "./services/GraphDataService";
import {HttpModule} from "@angular/http";
import {ButtonComponent} from "./components/generic/link-button.component";
import {GraphConfigPanel} from "./components/domain/graph-config-panel.component";

const cytoscape = require("cytoscape");


@NgModule({
    imports: [ BrowserModule, HttpModule],
    declarations: [
        AppComponent,
        ButtonComponent,
        ControlBoxComponent,
        InstanceListGroupComponent,
        GraphConfigPanel,
        PanelComponent,
        ListPanelComponent,
        ListPanelItemComponent,
        GraphComponent,
    ],
    providers: [
        GraphDataService
    ],
    bootstrap: [AppComponent]
})
class AppModule {
}


document.addEventListener('DOMContentLoaded', function () {
    platformBrowserDynamic()
        .bootstrapModule(AppModule);
});
