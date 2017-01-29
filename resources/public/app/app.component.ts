import { Component, OnInit } from '@angular/core';
import 'rxjs/Rx';

@Component({
  selector: 'my-app',
  templateUrl: 'app/app.component.html',
})
export class AppComponent implements OnInit {
    name = 'Angular!';

    someStrings:string[] = ["A", "B"];

    ngOnInit() {

    }
}
