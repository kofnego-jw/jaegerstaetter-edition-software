import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CorporationComponent } from './pages/corporation/corporation.component';
import { PersonComponent } from './pages/person/person.component';
import { PlaceComponent } from './pages/place/place.component';
import { WorldMapComponent } from './pages/place/world-map/world-map.component';
import { PublishComponent } from './pages/publish/publish.component';
import { SaintComponent } from './pages/saint/saint.component';
import { StartComponent } from './pages/start/start.component';

const routes: Routes = [
  { path: 'start', component: StartComponent },
  { path: 'person', component: PersonComponent },
  { path: 'saint', component: SaintComponent },
  { path: 'place', component: PlaceComponent },
  { path: 'corporation', component: CorporationComponent },
  { path: 'map', component: WorldMapComponent },
  { path: 'publish', component: PublishComponent },
  { path: '', pathMatch: 'full', redirectTo: '/start' },
  { path: '**', redirectTo: '/start' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      useHash: true,
      scrollPositionRestoration: 'top',
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
