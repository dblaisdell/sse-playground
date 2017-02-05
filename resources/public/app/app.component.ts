import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import 'rxjs/Rx';

@Component({
  selector: 'my-app',
  templateUrl: 'app/app.component.html',
})
export class AppComponent implements OnInit {
    name = 'Angular!';

    someStrings:string[] = ['A', 'B', 'C'];

    private EVENT_URL = '/events';
    private message: string = '0';

    constructor (private changeDetector: ChangeDetectorRef) {

    }

    ngOnInit() {
	console.log('B');
	// creates event object
        this.ws = new EventSource(this.EVENT_URL, { withCredentials: true });

        // listing to server messages
        this.ws.onmessage = (evt) => {
	    console.log('C');

	    this.logServerMessage(evt.data);

            // update the model
            this.message = evt.data;

            // manually detect changes
            this.changeDetector.detectChanges();
        };
    }

    logServerMessage (data) {
        console.log('message from server : ${data}');
    }
}
