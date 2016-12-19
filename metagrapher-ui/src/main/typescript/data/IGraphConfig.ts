export interface IGraphConfig {
    nodeConfigs: {[nodeId: string]: INodeConfig}
}

export type INodeConfig = {position: {x: number, y: number}};