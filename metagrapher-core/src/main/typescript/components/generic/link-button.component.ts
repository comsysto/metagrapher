import {Component, Output, Input, EventEmitter} from "@angular/core";
@Component({
    selector: 'link-button',
    template: `
    <a class="btn-{{type}} btn-{{size}} btn" (click)="onClick()">
        <ng-content></ng-content>
    </a>
`
})
export class ButtonComponent{

    @Output()
    private click: EventEmitter<void> = new EventEmitter<void>();

    @Input()
    private size: string = "md";

    @Input()
    private type: string = "default";


    onClick(): void {
        this.click.emit();
    }

}




