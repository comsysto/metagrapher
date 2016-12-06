import {Component} from '@angular/core';

@Component({
    selector: 'instance-group',
    template: `
<list-panel title="Instances">
    <div *ngFor="let instance of [{hostName: 'google', port:80, homePage: 'http://www.google.de'}, {hostName: 'golem', port:8080, homePage: 'http://www.golem.de'}]">
        <list-panel-item >
            <span class="instance-label">{{instance.hostName}}:{{instance.port}}</span>
            <a class="homepage-link" ng-hre="instance.homePage">
                <span class="glyphicon glyphicon-home"></span>
            </a>            
        </list-panel-item>
    </div>
</list-panel>
`
})
export class InstanceListGroupComponent {
    instances: Instance[] = [];
}

export class Instance {
    hostName: string;
    homePage:string;
    port: number;
}
