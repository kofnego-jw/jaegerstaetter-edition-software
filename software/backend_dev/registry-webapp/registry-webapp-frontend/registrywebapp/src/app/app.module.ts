import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { AppRoutingModule } from './app-routing.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { LeafletMarkerClusterModule } from '@asymmetrik/ngx-leaflet-markercluster';
import { HighlightJsModule, HIGHLIGHTJS_CONFIG } from 'ngx-highlight-js';
import { StartComponent } from './pages/start/start.component';
import { PersonComponent } from './pages/person/person.component';
import { SaintComponent } from './pages/saint/saint.component';
import { CorporationComponent } from './pages/corporation/corporation.component';
import { PlaceComponent } from './pages/place/place.component';
import { ErrorModalComponent } from './modals/error-modal/error-modal.component';
import { PersonInfoListComponent } from './pages/person/person-info-list/person-info-list.component';
import { ControlledVocabularyComponent } from './pages/components/controlled-vocabulary/controlled-vocabulary.component';
import { PlaceListComponent } from './pages/place/place-list/place-list.component';
import { GeoLocationComponent } from './pages/components/geo-location/geo-location.component';
import { GeoLocationModalComponent } from './modals/geo-location-modal/geo-location-modal.component';
import { ControlledVocabularyListComponent } from './pages/components/controlled-vocabulary-list/controlled-vocabulary-list.component';
import { VocabularySearchModalComponent } from './modals/vocabulary-search-modal/vocabulary-search-modal.component';
import { EditPlaceInfoModalComponent } from './modals/edit-place-info-modal/edit-place-info-modal.component';
import { WorldMapComponent } from './pages/place/world-map/world-map.component';
import { CorporationListComponent } from './pages/corporation/corporation-list/corporation-list.component';
import { ConfirmationModalComponent } from './modals/confirmation-modal/confirmation-modal.component';
import { EditCorporationInfoModalComponent } from './modals/edit-corporation-info-modal/edit-corporation-info-modal.component';
import { EditSaintInfoModalComponent } from './modals/edit-saint-info-modal/edit-saint-info-modal.component';
import { SaintListComponent } from './pages/saint/saint-list/saint-list.component';
import { EditPersonInfoModalComponent } from './modals/edit-person-info-modal/edit-person-info-modal.component';
import { KeyFilterComponent } from './pages/components/key-filter/key-filter.component';
import { RegistryDocumentComponent } from './pages/publish/registry-document/registry-document.component';
import { PublishComponent } from './pages/publish/publish.component';
import { TeiDocumentModalComponent } from './modals/tei-document-modal/tei-document-modal.component';

@NgModule({
  declarations: [
    AppComponent,
    StartComponent,
    PersonComponent,
    SaintComponent,
    CorporationComponent,
    PlaceComponent,
    ErrorModalComponent,
    PersonInfoListComponent,
    ControlledVocabularyComponent,
    PlaceListComponent,
    GeoLocationComponent,
    GeoLocationModalComponent,
    ControlledVocabularyListComponent,
    VocabularySearchModalComponent,
    EditPlaceInfoModalComponent,
    WorldMapComponent,
    CorporationListComponent,
    ConfirmationModalComponent,
    EditCorporationInfoModalComponent,
    EditSaintInfoModalComponent,
    SaintListComponent,
    EditPersonInfoModalComponent,
    KeyFilterComponent,
    RegistryDocumentComponent,
    PublishComponent,
    TeiDocumentModalComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    ModalModule.forRoot(),
    TooltipModule.forRoot(),
    TabsModule.forRoot(),
    FontAwesomeModule,
    LeafletModule,
    LeafletMarkerClusterModule,
    HighlightJsModule,
  ],
  providers: [
    {
      provide: HIGHLIGHTJS_CONFIG,
      useValue: {
        coreLibraryLoader: () => import('highlight.js/lib/core'),
        languages: {
          xml: () => import('highlight.js/lib/languages/xml'),
        },
      },
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
