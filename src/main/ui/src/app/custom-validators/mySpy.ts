import { Directive, OnChanges, OnInit, DoCheck, AfterContentInit, AfterContentChecked, AfterViewInit, AfterViewChecked, OnDestroy, ElementRef } from '@angular/core';

@Directive({
    selector: '[mySpy]'
})
export class SpyDirective implements OnChanges, OnInit, DoCheck, AfterContentInit, AfterContentChecked, AfterViewInit, AfterViewChecked, OnDestroy {

    constructor(private el: ElementRef) { }


    ngOnChanges() {
        console.log('this.el.nativeElement' + JSON.stringify(this.el.nativeElement));
        console.log('ngOnChanges');
    }

    ngOnInit() {
        console.log('this.el.nativeElement' + JSON.stringify(this.el.nativeElement));
        console.log('onInit');
    }

    ngDoCheck() {
        console.log('this.el.nativeElement' + JSON.stringify(this.el.nativeElement));
        console.log('ngDoCheck');
    }

    ngAfterContentInit() {
        console.log('this.el.nativeElement' + JSON.stringify(this.el.nativeElement));
        console.log('ngAfterContentInit');
    }
    ngAfterContentChecked() {
        console.log('this.el.nativeElement' + JSON.stringify(this.el.nativeElement));
        console.log('ngAfterContentChecked');
    }
    ngAfterViewInit() {
        console.log('this.el.nativeElement' + JSON.stringify(this.el.nativeElement));
        console.log('ngAfterViewInit');
    }
    ngAfterViewChecked() {
        console.log('this.el.nativeElement' + JSON.stringify(this.el.nativeElement));
        console.log('ngAfterViewChecked');
    }

    ngOnDestroy() {
        console.log('this.el.nativeElement' + JSON.stringify(this.el.nativeElement));
        console.log('ngOnDestroy');
    }

}


