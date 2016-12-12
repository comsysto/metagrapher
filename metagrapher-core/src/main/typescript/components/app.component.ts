import {
    Component, ElementRef, ViewChild,
    AfterViewInit, AfterContentChecked, OnInit, OnChanges, SimpleChanges, AfterViewChecked
} from "@angular/core";
import * as $ from "jquery";
import {GraphDataService} from "../services/GraphDataService";
import {INode} from "../data/INode";
import {Observable, Subject} from "rxjs";
import {IGraphConfig} from "../data/IGraphConfig";
import {GraphComponent} from "./domain/graph.component";

@Component({
    selector: 'app',
    template: `
    <control-box></control-box>
    <graph [nodes]="nodes" [styles]="styles" [config]="config" class="graph-container"></graph>
`
})
export class AppComponent implements OnInit, AfterViewInit{

    constructor(private graphDataService: GraphDataService, private element: ElementRef) {
    }

    @ViewChild(GraphComponent)
    private graphComponent: GraphComponent;

    ngOnInit(): void{
        let $element = $(this.element.nativeElement);
        this.graphDataService.nodesUrl = $element.attr('graph-nodes-url');
        this.graphDataService.styleUrl = $element.attr('graph-style-url');
        this.graphDataService.configUrl = $element.attr('graph-config-url');
    }

    get nodes(): Observable<INode[]>{
        return this.graphDataService.nodes;
    }

    get styles(): Observable<string>{
        return this.graphDataService.styles;
    }

    get config(): Observable<IGraphConfig>{
        return this.graphDataService.config;
    }


    ngAfterViewInit(): void {
        this.graphDataService.configProvider = () => this.graphComponent.currentConfig
    }

}
