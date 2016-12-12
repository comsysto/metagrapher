import {Component} from "@angular/core";

@Component({
    selector: 'control-box',
    template: `
<div class="control-box">
    <h1>metagrapher</h1>
    <hr/>
    <div class="panel-group">  
        <graph-config-panel></graph-config-panel>
    </div>

</div>
  `
})
export class ControlBoxComponent {
}
