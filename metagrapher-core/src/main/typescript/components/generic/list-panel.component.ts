import {Component, ElementRef, ViewChild, Input} from "@angular/core";
import * as $ from "jquery";

@Component({
    selector: 'list-panel',
    template: `
<div class="panel panel-default">
     <div  (click)="toggle()" class="panel-heading">
        {{title}}
    </div>
   
     <ul class="list-group" #bodyElement>
       <ng-content></ng-content>
    </ul>
</div>
`
})
export class ListPanelComponent {

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
