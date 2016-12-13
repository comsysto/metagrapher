import {Component} from "@angular/core";
import {GraphDataService} from "../../services/GraphDataService";
import {IPool} from "../../data/IPool";

@Component({
    selector: 'pool-links-panel',
    template: `
<panel title="Pool Links ({{links?.length}})" [hidden]="hidden">
    <table class="table">
        <tbody>
            <tr *ngFor="let link of links">
                <td class="instance-label">{{link.name}}</td>
                <td>
                    <a class="homepage-link" [href]="link.href" target="_blank">
                        {{link.href}}
                    </a>
                </td>
            </tr>
        </tbody>
    </table>
</panel>
`
})
export class PoolLinksPanel {

    constructor(private graphDataService: GraphDataService) {
        graphDataService.selectedPool.subscribe((pool: IPool) => {
                if (pool) {
                    this.hidden = false;
                    this.links = Object.keys(pool.links).map(name => new Link(name, pool.links[name]));
                }else{
                    this.hidden = true;
                    this.links = [];
                }
            }
        );
    }

    private hidden: boolean = true;
    private links: Link[] = [];

}

class Link {
    constructor(public name: string,
                public href: string){
    }
}

