import {Component} from '@angular/core';

@Component({
    selector: 'control-box',
    template: `
<div>
    <h1>metagrapher</h1>
    <hr/>
    <div class="panel-group">  
          <!--<instance-group></instance-group>-->
          <panel title="test-panel">
            <span>Hello World Panel</span>
          </panel>
          <list-panel title="test-list-panel">
            <list-panel-item>first</list-panel-item>
            <list-panel-item>second</list-panel-item>
          </list-panel>
    </div>

</div>
  `
})
export class ControlBoxComponent {}
