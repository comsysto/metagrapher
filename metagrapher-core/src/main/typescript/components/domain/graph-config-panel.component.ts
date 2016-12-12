import {Component, Inject} from '@angular/core';
import {GraphDataService} from "../../services/GraphDataService";

@Component({
    selector: 'graph-config-panel',
    template: `
<list-panel title="Graph Configuration">
        <list-panel-item>
            <link-button (click)="onLoadConfig()">load configuration</link-button>                        
        </list-panel-item>
        <list-panel-item>
            <link-button type="danger" (click)="onStoreConfig()">store configuration</link-button>
        </list-panel-item>
        <list-panel-item>
            <link-button type="danger" (click)="onAutoConfig()">auto configure</link-button>
        </list-panel-item>
</list-panel>
`
})
export class GraphConfigPanel {

    constructor(private graphDataService: GraphDataService){
    }

    onLoadConfig(): void {
        this.graphDataService.loadConfig()
    }

    onStoreConfig(): void {
        this.graphDataService.storeConfig()
    }


    onAutoConfig(): void {
        this.graphDataService.autoConfig()
    }
}

