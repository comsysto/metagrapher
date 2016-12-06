import {Component} from "@angular/core";

@Component({
    selector: 'list-panel-item',
    template: `
<li class="list-group-item">
    <ng-content></ng-content>
</li>
`
})
export class ListPanelItemComponent {
}
