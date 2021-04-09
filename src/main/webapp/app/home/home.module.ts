import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SeKotlinSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [SeKotlinSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class SeKotlinHomeModule {}
