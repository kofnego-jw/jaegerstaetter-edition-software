import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { CollapseModule } from 'ngx-bootstrap/collapse';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { PopoverModule } from 'ngx-bootstrap/popover';

import { GalleryModule } from 'ng-gallery';
import { LightboxModule } from 'ng-gallery/lightbox';

import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { LeafletMarkerClusterModule } from '@asymmetrik/ngx-leaflet-markercluster';

import { NgHttpLoaderModule } from 'ng-http-loader';

import { HighlightModule, HIGHLIGHT_OPTIONS } from 'ngx-highlightjs';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WholePageComponent } from './layout/whole-page/whole-page.component';
import { FooterComponent } from './layout/footer/footer.component';
import { HeaderComponent } from './layout/header/header.component';
import { TopNavComponent } from './layout/top-nav/top-nav.component';
import { FontSizeSwitcherComponent } from './layout/top-nav/font-size-switcher/font-size-switcher.component';
import { ContrastSwitcherComponent } from './layout/top-nav/contrast-switcher/contrast-switcher.component';
import { LanguageSwitcherComponent } from './layout/top-nav/language-switcher/language-switcher.component';
import { StartseiteComponent } from './pages/startseite/startseite.component';
import { KontaktComponent } from './pages/kontakt/kontakt.component';
import { ImpressumComponent } from './pages/impressum/impressum.component';
import { DatenschutzComponent } from './pages/datenschutz/datenschutz.component';
import { RegisterComponent } from './pages/register/register.component';
import { SucheComponent } from './pages/suche/suche.component';
import { DigitaleEditionComponent } from './pages/digitale-edition/digitale-edition.component';
import { BiografienComponent } from './pages/biografien/biografien.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { BriefPanelComponent } from './pages/digitale-edition/brief-panel/brief-panel.component';
import { ViewSwitchComponent } from './pages/digitale-edition/brief-panel/view-switch/view-switch.component';
import { FacsimileComponent } from './pages/digitale-edition/brief-panel/facsimile/facsimile.component';
import { DiploComponent } from './pages/digitale-edition/brief-panel/diplo/diplo.component';
import { NormComponent } from './pages/digitale-edition/brief-panel/norm/norm.component';
import { XmlViewComponent } from './pages/digitale-edition/brief-panel/xml-view/xml-view.component';
import { PersNameComponent } from './elements/pers-name/pers-name.component';
import { PlaceNameComponent } from './elements/place-name/place-name.component';
import { PersNameWrapperComponent } from './layout/pers-name-wrapper/pers-name-wrapper.component';
import { PlaceNameWrapperComponent } from './layout/place-name-wrapper/place-name-wrapper.component';
import { SortFieldModalComponent } from './modals/sort-field-modal/sort-field-modal.component';
import { NoteComponent } from './elements/note/note.component';
import { SicCorrComponent } from './elements/sic-corr/sic-corr.component';
import { AbbrExpandComponent } from './elements/abbr-expand/abbr-expand.component';
import { MetamarkComponent } from './elements/metamark/metamark.component';
import { UnclearComponent } from './elements/unclear/unclear.component';
import { GapComponent } from './elements/gap/gap.component';
import { CommentComponent } from './elements/comment/comment.component';
import { DamageComponent } from './elements/damage/damage.component';
import { BiographicalInfoModalComponent } from './modals/biographical-info-modal/biographical-info-modal.component';
import { GeographicalInfoModalComponent } from './modals/geographical-info-modal/geographical-info-modal.component';
import { ErrorModalComponent } from './modals/error-modal/error-modal.component';
import { ToggleAnnotationButtonComponent } from './layout/toggle-annotation-button/toggle-annotation-button.component';
import { StartpageSliderComponent } from './pages/startseite/startpage-slider/startpage-slider.component';
import { EditionComponent } from './pages/edition/edition.component';
import { VerzeichnisComponent } from './pages/edition/verzeichnis/verzeichnis.component';
import { EditionAnzeigeComponent } from './pages/edition/edition-anzeige/edition-anzeige.component';
import { ResourcePreviewComponent } from './layout/resource-preview/resource-preview.component';
import { CommentDocViewComponent } from './pages/digitale-edition/comment-doc-view/comment-doc-view.component';
import { ViewAnchorComponent } from './elements/view-anchor/view-anchor.component';
import { AnchorComponent } from './elements/anchor/anchor.component';
import { FotografieDokumenteComponent } from './pages/fotografie-dokumente/fotografie-dokumente.component';
import { FotoDokButtonComponent } from './pages/fotografie-dokumente/foto-dok-button/foto-dok-button.component';
import { PictureGalleryComponent } from './pages/fotografie-dokumente/picture-gallery/picture-gallery.component';
import { FotoDokVerzeichnisComponent } from './pages/fotografie-dokumente/foto-dok-verzeichnis/foto-dok-verzeichnis.component';
import { BiographyViewComponent } from './pages/biografien/biography-view/biography-view.component';
import { AdminComponent } from './pages/admin/admin.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { AdminPasswordModalComponent } from './modals/admin-password-modal/admin-password-modal.component';
import { InfoModalComponent } from './modals/info-modal/info-modal.component';
import { RegisterAnzeigeComponent } from './pages/register/register-anzeige/register-anzeige.component';
import { RegisterVerzeichnisComponent } from './pages/register/register-verzeichnis/register-verzeichnis.component';
import { OrtsRegisterComponent } from './pages/register/orts-register/orts-register.component';
import { PersonenRegisterComponent } from './pages/register/personen-register/personen-register.component';
import { HeiligeRegisterComponent } from './pages/register/heilige-register/heilige-register.component';
import { OrganisationenRegisterComponent } from './pages/register/organisationen-register/organisationen-register.component';
import { BibelstellenRegisterComponent } from './pages/register/bibelstellen-register/bibelstellen-register.component';
import { PersonenInfoComponent } from './pages/register/personen-info/personen-info.component';
import { HeiligeInfoComponent } from './pages/register/heilige-info/heilige-info.component';
import { OrganisationenInfoComponent } from './pages/register/organisationen-info/organisationen-info.component';
import { OrtsInfoComponent } from './pages/register/orts-info/orts-info.component';
import { SingleHitComponent } from './pages/suche/single-hit/single-hit.component';
import { DocumentPaginationComponent } from './layout/document-pagination/document-pagination.component';
import { FilterControllerComponent } from './pages/edition/filter-controller/filter-controller.component';
import { ViewPlaceRegistryComponent } from './elements/view-place-registry/view-place-registry.component';
import { BibelstellenInfoComponent } from './pages/register/bibelstellen-info/bibelstellen-info.component';
import { ResourceListCurrentStateViewComponent } from './pages/edition/resource-list-current-state-view/resource-list-current-state-view.component';
import { TocSelectorComponent } from './pages/edition/toc-selector/toc-selector.component';
import { PageSelectorComponent } from './pages/edition/page-selector/page-selector.component';
import { MetadataViewComponent } from './pages/digitale-edition/metadata-view/metadata-view.component';
import { RelatedResourcesComponent } from './pages/edition/related-resources/related-resources.component';
import { AbComponent } from './elements/ab/ab.component';
import { AddComponent } from './elements/add/add.component';
import { CbComponent } from './elements/cb/cb.component';
import { DelComponent } from './elements/del/del.component';
import { FwComponent } from './elements/fw/fw.component';
import { HiComponent } from './elements/hi/hi.component';
import { LbComponent } from './elements/lb/lb.component';
import { PbComponent } from './elements/pb/pb.component';
import { SubstComponent } from './elements/subst/subst.component';
import { SuppliedComponent } from './elements/supplied/supplied.component';
import { StampComponent } from './elements/stamp/stamp.component';
import { ChoiceComponent } from './elements/choice/choice.component';
import { AbbrComponent } from './elements/abbr/abbr.component';
import { CorrComponent } from './elements/corr/corr.component';
import { ExpanComponent } from './elements/expan/expan.component';
import { SicComponent } from './elements/sic/sic.component';
import { RefComponent } from './elements/ref/ref.component';
import { NotehandComponent } from './elements/notehand/notehand.component';
import { SurplusComponent } from './elements/surplus/surplus.component';
import { QuoteComponent } from './elements/quote/quote.component';
import { PhotoDocItemViewComponent } from './pages/fotografie-dokumente/photo-doc-item-view/photo-doc-item-view.component';
import { OrgNameComponent } from './elements/org-name/org-name.component';
import { CorporationInfoModalComponent } from './modals/corporation-info-modal/corporation-info-modal.component';
import { SaintInfoModalComponent } from './modals/saint-info-modal/saint-info-modal.component';
import { TocEntryComponent } from './pages/edition/toc-entry/toc-entry.component';
import { HandsInfoComponent } from './elements/hands-info/hands-info.component';
import { DivComponent } from './elements/div/div.component';
import { NoteRefComponent } from './elements/note-ref/note-ref.component';
import { SelectionModalComponent } from './modals/selection-modal/selection-modal.component';
import { MaterialienComponent } from './pages/materialien/materialien.component';
import { BreadCrumbComponent } from './layout/bread-crumb/bread-crumb.component';
import { BioNoteComponent } from './elements/bio-note/bio-note.component';
import { BioFigureComponent } from './elements/bio-figure/bio-figure.component';
import { BioRefComponent } from './elements/bio-ref/bio-ref.component';
import { CitationModalComponent } from './modals/citation-modal/citation-modal.component';
import { CorrespPlacesModalComponent } from './modals/corresp-places-modal/corresp-places-modal.component';
import { BiographyIndexComponent } from './pages/biografien/biography-index/biography-index.component';
import { CommentdocRefComponent } from './elements/commentdoc-ref/commentdoc-ref.component';
import { MaterialienViewComponent } from './pages/materialien/materialien-view/materialien-view.component';
import { SignatureSynopsisComponent } from './pages/suche/signature-synopsis/signature-synopsis.component';
import { SpezialBriefwechselComponent } from './pages/edition/spezial-briefwechsel/spezial-briefwechsel.component';
import { SpecialCorrespComponent } from './elements/special-corresp/special-corresp.component';
import { AdminStatReportComponent } from './pages/admin/admin-stat-report/admin-stat-report.component';
import { StatReportComponent } from './pages/admin/stat-report/stat-report.component';
import { StatElementFullDescComponent } from './pages/admin/stat-element-full-desc/stat-element-full-desc.component';
import { StatAttrFullDescComponent } from './pages/admin/stat-attr-full-desc/stat-attr-full-desc.component';
import { StatAttrValueOccComponent } from './pages/admin/stat-attr-value-occ/stat-attr-value-occ.component';
import { AdminStatElementDescComponent } from './pages/admin/admin-stat-element-desc/admin-stat-element-desc.component';
import { DanksagungComponent } from './pages/danksagung/danksagung.component';
import { MapConsentModalComponent } from './modals/map-consent-modal/map-consent-modal.component';
import { SearchHitFilterControlComponent } from './pages/suche/search-hit-filter-control/search-hit-filter-control.component';
import { SearchFilterControllerComponent } from './pages/suche/search-filter-controller/search-filter-controller.component';
import { BioindexFigureComponent } from './elements/bioindex-figure/bioindex-figure.component';

@NgModule({
  declarations: [
    AppComponent,
    WholePageComponent,
    FooterComponent,
    HeaderComponent,
    TopNavComponent,
    FontSizeSwitcherComponent,
    ContrastSwitcherComponent,
    LanguageSwitcherComponent,
    StartseiteComponent,
    KontaktComponent,
    ImpressumComponent,
    DatenschutzComponent,
    RegisterComponent,
    SucheComponent,
    DigitaleEditionComponent,
    BiografienComponent,
    BriefPanelComponent,
    ViewSwitchComponent,
    FacsimileComponent,
    DiploComponent,
    NormComponent,
    XmlViewComponent,
    PersNameComponent,
    PlaceNameComponent,
    PersNameWrapperComponent,
    PlaceNameWrapperComponent,
    SortFieldModalComponent,
    NoteComponent,
    SicCorrComponent,
    AbbrExpandComponent,
    MetamarkComponent,
    UnclearComponent,
    GapComponent,
    CommentComponent,
    DamageComponent,
    BiographicalInfoModalComponent,
    GeographicalInfoModalComponent,
    ErrorModalComponent,
    ToggleAnnotationButtonComponent,
    StartpageSliderComponent,
    EditionComponent,
    VerzeichnisComponent,
    EditionAnzeigeComponent,
    ResourcePreviewComponent,
    CommentDocViewComponent,
    ViewAnchorComponent,
    AnchorComponent,
    FotografieDokumenteComponent,
    FotoDokButtonComponent,
    PictureGalleryComponent,
    FotoDokVerzeichnisComponent,
    BiographyViewComponent,
    AdminComponent,
    NotFoundComponent,
    AdminPasswordModalComponent,
    InfoModalComponent,
    RegisterAnzeigeComponent,
    RegisterVerzeichnisComponent,
    MetadataViewComponent,
    OrtsRegisterComponent,
    PersonenRegisterComponent,
    HeiligeRegisterComponent,
    OrganisationenRegisterComponent,
    BibelstellenRegisterComponent,
    PersonenInfoComponent,
    HeiligeInfoComponent,
    OrganisationenInfoComponent,
    OrtsInfoComponent,
    SingleHitComponent,
    DocumentPaginationComponent,
    FilterControllerComponent,
    ViewPlaceRegistryComponent,
    BibelstellenInfoComponent,
    ResourceListCurrentStateViewComponent,
    TocSelectorComponent,
    PageSelectorComponent,
    RelatedResourcesComponent,
    AbComponent,
    AddComponent,
    CbComponent,
    DelComponent,
    FwComponent,
    HiComponent,
    LbComponent,
    PbComponent,
    SubstComponent,
    SuppliedComponent,
    StampComponent,
    ChoiceComponent,
    AbbrComponent,
    CorrComponent,
    ExpanComponent,
    SicComponent,
    RefComponent,
    NotehandComponent,
    SurplusComponent,
    QuoteComponent,
    PhotoDocItemViewComponent,
    OrgNameComponent,
    CorporationInfoModalComponent,
    SaintInfoModalComponent,
    TocEntryComponent,
    HandsInfoComponent,
    DivComponent,
    NoteRefComponent,
    SelectionModalComponent,
    MaterialienComponent,
    BreadCrumbComponent,
    BioNoteComponent,
    BioFigureComponent,
    BioRefComponent,
    CitationModalComponent,
    CorrespPlacesModalComponent,
    BiographyIndexComponent,
    CommentdocRefComponent,
    MaterialienViewComponent,
    SignatureSynopsisComponent,
    SpezialBriefwechselComponent,
    SpecialCorrespComponent,
    AdminStatReportComponent,
    StatReportComponent,
    StatElementFullDescComponent,
    StatAttrFullDescComponent,
    StatAttrValueOccComponent,
    AdminStatElementDescComponent,
    DanksagungComponent,
    MapConsentModalComponent,
    SearchHitFilterControlComponent,
    SearchFilterControllerComponent,
    BioindexFigureComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    GalleryModule,
    LightboxModule,
    NgHttpLoaderModule.forRoot(),
    CollapseModule.forRoot(),
    AccordionModule.forRoot(),
    CarouselModule.forRoot(),
    TooltipModule.forRoot(),
    ModalModule.forRoot(),
    BsDropdownModule.forRoot(),
    PopoverModule.forRoot(),
    FontAwesomeModule,
    HighlightModule,
    LeafletModule,
    LeafletMarkerClusterModule,
  ],
  providers: [
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: {
        coreLibraryLoader: () => import('highlight.js/lib/core'),
        lineNumbersLoader: () => import('highlightjs-line-numbers.js'),
        languages: {
          xml: () => import('highlight.js/lib/languages/xml'),
        },
      },
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
