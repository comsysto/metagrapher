import {INode} from "../../data/INode";
import {IGraphConfig, INodeConfig} from "../../data/IGraphConfig";
const cytoscape = require("cytoscape");

export class CytoscapeGraph {

    cy: any;


    constructor(nodes: INode[], styles: string, containerElement: any, private onSelectedNodeChange: (node: any) => void) {
        this.cy = cytoscape({
            container: containerElement,

            ready: function () {
            },

            style: styles,

            layout: {
                name: 'null',
            },

            elements: nodes
        });

        this.fixPoolPositionToElement();
        this.fixStatePositionToPool();
        this.registerSelectionHandler();
    }

    private fixPoolPositionToElement(): void {
        this.cy.nodes('.service').on('position', function (e: any) {
            var serviceElementHeight = 100;
            var poolElementHeight = 32;
            var serviceElement = e.cyTarget;
            var p = serviceElement.position();
            var poolElements = serviceElement.neighborhood('node.pool').sort(function (e1: any, e2: any) {
                return e1.data('name').localeCompare(e2.data('name'));
            });

            poolElements.forEach(function (element: any, i: number) {
                element.position({
                    x: p.x,
                    y: p.y + serviceElementHeight / 2 + (poolElementHeight * i) + poolElementHeight / 2
                })
            })
        });
    }

    private fixStatePositionToPool(): void {
        this.cy.nodes('.pool').on('position', function (e: any) {
            var poolElement = e.cyTarget;
            var p = poolElement.position();
            var stateElements = poolElement.neighborhood('node.state').sort(function (e1: any, e2: any) {
                return e1.data('order') - e2.data('order');
            });

            stateElements.forEach(function (element: any, i: number) {
                element.position({
                    x: p.x + (12 * i) - (stateElements.length / 2 * 12) + 6,
                    y: p.y + 6
                })
            })
        });
    }

    private registerSelectionHandler(): void {
        this.cy.nodes().on('select', function (e: any) {
            this.onSelectedNodeChange(e.cyTarget);
        });

        this.cy.nodes().on('unselect', function (e: any) {
            this.onSelectedNodeChange(e.cyTarget);
        });
    }

    public  destroy(): void {
        // handle clean up
    }

    public selectNode(node: INode) {
        var selectedNodeId = this.cy.$(':selected').id();
        if (!node || node.id !== selectedNodeId) {
            this.cy.$(':selected').unselect();
        }
        if (node && node.id !== selectedNodeId) {
            this.cy.$('#' + node.id).select();
        }

    }

    public autoConfig() {
        this.cy.makeLayout({name: 'grid'}).run();
    }

    public applyConfig(graphLayout: IGraphConfig): void {
        let nodeLayouts = graphLayout.nodeConfigs;
        this.cy.nodes().forEach(function (node: any) {
            let nodeLayout: INodeConfig = nodeLayouts[node.id()];
            if (nodeLayout) {
                node.position(nodeLayout.position);
            }
        });
    }

    public getLayout(): IGraphConfig {
        return {
            nodeConfigs: this.cy.nodes().toArray().reduce(function (map: {[nodeId:string]: INodeConfig}, node: any) {
                var p = node.position();
                map[node.id()] = {
                    position: {
                        x: <number>p.x,
                        y: <number>p.y,
                }};
                return map;
            }, {})
        };
    }
}


