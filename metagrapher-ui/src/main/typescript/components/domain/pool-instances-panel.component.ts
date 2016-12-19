import {Component} from "@angular/core";
import {GraphDataService} from "../../services/GraphDataService";
import {IPool} from "../../data/IPool";
import {IInstance} from "../../data/IInstance";

@Component({
    selector: 'pool-instance-panel',
    template: `
<panel title="Pool Instances ({{instances?.length}})" [hidden]="hidden">
 <table class="table">
    <tbody>
        <tr *ngFor="let instance of instances" [class.info]="isSelected(instance)" (click)="selectInstance(instance)">
            <td><input type="radio" [name]="instance.hostName + instance.port" value="" [checked]="isSelected(instance)"></td>
            <td>{{instance.hostName}}:{{instance.port}}</td>
            <td>{{instance.state}}</td>
            <td>
                <a class="homepage-link" [href]="instance.homePage" target="_blank">
                    <span class="glyphicon glyphicon-home"></span>
                </a>
            </td>
        </tr>
    </tbody>
  </table>
</panel>
`
})
export class PoolInstancesPanel {

    constructor(private graphDataService: GraphDataService) {
        graphDataService.selectedPool.subscribe(pool => {
            this.hidden = !pool;
            this.instances = pool && pool.instances || null;
        });
        graphDataService.selectedInstance
            .subscribe(instance => this.selectedInstance = instance);
    }

    private hidden: boolean = true;
    private instances: IInstance[] = null;
    private selectedInstance: IInstance = null;

    isSelected(instance: IInstance){
        if(!instance){
            return false;
        }
        if(!this.selectedInstance){
            return false;
        }
        return this.selectedInstance.id === instance.id;
    }

    selectInstance(instance: IInstance){
        this.graphDataService.selectInstance(instance);
    }

}

