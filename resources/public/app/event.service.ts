import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/share';

import { Sse } from './sse';

@Injectable()
export class EventDataService {
  eventData: Observable<any>;

  constructor(sse: Sse) {
    this.eventData = sse.observe('/events').share();
  }
}
