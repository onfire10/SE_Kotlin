import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SeKotlinSharedModule } from 'app/shared/shared.module';
import { SeKotlinCoreModule } from 'app/core/core.module';
import { SeKotlinAppRoutingModule } from './app-routing.module';
import { SeKotlinHomeModule } from './home/home.module';
import { SeKotlinEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    SeKotlinSharedModule,
    SeKotlinCoreModule,
    SeKotlinHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SeKotlinEntityModule,
    SeKotlinAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class SeKotlinAppModule {}
