import {Component, ViewChild, ElementRef, AfterViewInit, Input, DoCheck, Output, EventEmitter} from "@angular/core";
import {CytoscapeGraph} from "../graph/graph";
import {Observable} from "rxjs";
import {INode} from "../../data/INode";
import {IGraphConfig} from "../../data/IGraphConfig";

@Component({
    selector: 'graph',
    template: `
    <div class="graph-container" #containerElement></div>
`
})
export class GraphComponent implements AfterViewInit {

    @ViewChild('containerElement')
    containerElement: ElementRef;

    @Input()
    nodes: Observable<INode[]>;

    @Input()
    styles: Observable<string>;

    @Input()
    config: Observable<IGraphConfig>;

    @Output()
    onSelectedNode: EventEmitter<INode> = new EventEmitter();

    @Input()
    selectedNode: Observable<INode>;

    cytoscapeGraph: CytoscapeGraph = null;



    ngAfterViewInit(): void {
        console.log("GraphComponent ngAfterViewInit subscribe");

        this.onSelectedNode.subscribe((node: INode) => console.log("GraphComponent onSelectedNode", {node}));

        Observable.combineLatest(
            this.nodes.do((values) => console.log("GraphComponent nodes", {values})),
            this.styles.do((values) => console.log("GraphComponent styles", {values})),
            this.config.do((values) => console.log("GraphComponent config", {values}))
        )
            .do((values) => console.log("GraphComponent ngAfterViewInit", values))
            .filter(([nodes, styles]) => !!nodes && !!styles)
            .do((values) => console.log("GraphComponent ngAfterViewInit filter", values))
            .subscribe(([nodes, styles, config]) =>
                this.initialiseGraph(nodes, styles, config)
            );


        if (this.selectedNode) {
            this.selectedNode.subscribe(
                node => {
                    if (this.cytoscapeGraph) {
                        this.cytoscapeGraph.selectNode(node);
                    }
                }
            )
        }
    }

    private initialiseGraph(nodes: INode[], styles: string, config: IGraphConfig) {
        console.log("GraphComponent initialiseGraph", {nodes, styles, config});
        if (this.cytoscapeGraph) {
            console.log("GraphComponent initialiseGraph destroy graph");
            this.cytoscapeGraph.destroy();
            this.cytoscapeGraph = null;
        }
        this.cytoscapeGraph = new CytoscapeGraph(
            nodes,
            styles,
            this.containerElement.nativeElement,
            (element: any) => {
                this.selectNode(element.data());
            }
        );
        console.log("GraphComponent created cytoscape");
        if(config){
            this.cytoscapeGraph.applyConfig(config);
        }else{
            this.cytoscapeGraph.autoConfig();
        }

    }

    private selectNode(node: any) {
        this.onSelectedNode.emit(node);
    }

    get currentConfig(): IGraphConfig{
        if(this.cytoscapeGraph){
            return this.cytoscapeGraph.getLayout();
        }
        return null;
    }
}
