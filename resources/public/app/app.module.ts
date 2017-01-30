import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule }    from '@angular/http';
import { AppComponent }  from './app.component';


import { EventDataService } from './event.service';
import { Sse } from './sse';

@NgModule({
    imports: [ BrowserModule,
     	       HttpModule ],

    declarations: [ AppComponent ],

    providers: [Sse, EventDataService],

    bootstrap: [ AppComponent ]

})
export class AppModule { }
