import {Component, ElementRef, Input, AfterViewInit, ViewChild} from "@angular/core";
import * as $ from "jquery";

@Component({
    selector: 'panel',
    template: `
<div class="panel panel-default">
    <div  (click)="toggle()" class="panel-heading">
        {{title}}
    </div>
   
   <div #bodyElement>
        <ng-content></ng-content>
   </div>
  
</div>
`
})
export class PanelComponent implements AfterViewInit{
    @Input() title: string;

    @ViewChild('bodyElement')
    bodyElement: ElementRef;

    toggle(): void {
        console.log("collapse toggle", {element: this.bodyElement});
        $(this.bodyElement.nativeElement).collapse('toggle');
    }

    ngAfterViewInit(): void {
        console.log("collapse hide", {element: this.bodyElement});
        $(this.bodyElement.nativeElement).collapse();
        $(this.bodyElement.nativeElement).collapse('hide');
    }

}
