import {Component} from "@angular/core";
import {GraphDataService} from "../../services/GraphDataService";
import {IInstance} from "../../data/IInstance";

@Component({
    selector: 'instance-links-panel',
    template: `
<panel title="Instance Links ({{links?.length}})" [hidden]="hidden">
    <table class="table">
        <tbody>
            <tr>
                <th>{{host}}:{{port}}</th>
                <th>
                    <a class="homepage-link" [href]="homePage" target="_blank">
                        <span class="glyphicon glyphicon-home"></span>
                    </a>
                </th>
            </tr>
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
export class InstanceLinksPanel {

    constructor(private graphDataService: GraphDataService) {
        graphDataService.selectedInstance.subscribe((instance: IInstance) => {
                if (instance) {
                    this.hidden = false;
                    this.links = Object.keys(instance.links).map(name => new Link(name, instance.links[name]));
                    this.host = instance.hostName;
                    this.port = instance.port;
                    this.homePage = instance.homePage;
                } else {
                    this.hidden = true;
                    this.links = [];
                    this.homePage = null;
                }
            }
        );
    }

    private hidden: boolean = true;
    private links: Link[] = [];
    private host: string = null;
    private port: number = null;
    private homePage: string = null;

}

class Link {
    constructor(public name: string,
                public href: string) {
    }
}

