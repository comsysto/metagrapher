import {INode} from "./INode";
import {IInstance} from "./IInstance";

export interface IPool extends INode{
    instances: IInstance[]
    links:{[name: string]: string}
}