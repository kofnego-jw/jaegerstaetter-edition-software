import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './pages/admin/admin.component';
import { BiografienComponent } from './pages/biografien/biografien.component';
import { BiographyViewComponent } from './pages/biografien/biography-view/biography-view.component';
import { DatenschutzComponent } from './pages/datenschutz/datenschutz.component';
import { CommentDocViewComponent } from './pages/digitale-edition/comment-doc-view/comment-doc-view.component';
import { DigitaleEditionComponent } from './pages/digitale-edition/digitale-edition.component';
import { EditionAnzeigeComponent } from './pages/edition/edition-anzeige/edition-anzeige.component';
import { EditionComponent } from './pages/edition/edition.component';
import { VerzeichnisComponent } from './pages/edition/verzeichnis/verzeichnis.component';
import { FotoDokVerzeichnisComponent } from './pages/fotografie-dokumente/foto-dok-verzeichnis/foto-dok-verzeichnis.component';
import { FotografieDokumenteComponent } from './pages/fotografie-dokumente/fotografie-dokumente.component';
import { PhotoDocItemViewComponent } from './pages/fotografie-dokumente/photo-doc-item-view/photo-doc-item-view.component';
import { PictureGalleryComponent } from './pages/fotografie-dokumente/picture-gallery/picture-gallery.component';
import { ImpressumComponent } from './pages/impressum/impressum.component';
import { KontaktComponent } from './pages/kontakt/kontakt.component';
import { MaterialienComponent } from './pages/materialien/materialien.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { BibelstellenRegisterComponent } from './pages/register/bibelstellen-register/bibelstellen-register.component';
import { HeiligeRegisterComponent } from './pages/register/heilige-register/heilige-register.component';
import { OrganisationenRegisterComponent } from './pages/register/organisationen-register/organisationen-register.component';
import { OrtsRegisterComponent } from './pages/register/orts-register/orts-register.component';
import { PersonenRegisterComponent } from './pages/register/personen-register/personen-register.component';
import { RegisterVerzeichnisComponent } from './pages/register/register-verzeichnis/register-verzeichnis.component';
import { RegisterComponent } from './pages/register/register.component';
import { StartseiteComponent } from './pages/startseite/startseite.component';
import { SucheComponent } from './pages/suche/suche.component';
import { AdminGuard } from './services/admin.guard';
import { BiographyIndexComponent } from './pages/biografien/biography-index/biography-index.component';
import { MaterialienViewComponent } from './pages/materialien/materialien-view/materialien-view.component';
import { SpezialBriefwechselComponent } from './pages/edition/spezial-briefwechsel/spezial-briefwechsel.component';
import { AdminStatReportComponent } from './pages/admin/admin-stat-report/admin-stat-report.component';
import { DanksagungComponent } from './pages/danksagung/danksagung.component';

const routes: Routes = [
  { path: 'startseite', component: StartseiteComponent },
  {
    path: 'edition',
    component: EditionComponent,
    children: [
      {
        path: 'ausgewaehlte-briefwechsel',
        component: SpezialBriefwechselComponent,
      },
      { path: 'verzeichnis', component: VerzeichnisComponent },
      { path: 'view/:id', component: EditionAnzeigeComponent },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: '/edition/verzeichnis',
      },
    ],
  },
  {
    path: 'digitale-edition',
    component: DigitaleEditionComponent,
    children: [{ path: ':id', component: CommentDocViewComponent }],
  },
  {
    path: 'biografien',
    component: BiografienComponent,
    children: [
      { path: 'ffaj', component: BiographyIndexComponent },
      { path: ':id', component: BiographyViewComponent },
      { path: '', pathMatch: 'full', redirectTo: '/startseite' },
    ],
  },
  {
    path: 'register',
    component: RegisterComponent,
    children: [
      { path: '', pathMatch: 'full', component: RegisterVerzeichnisComponent },
      { path: 'orte', component: OrtsRegisterComponent },
      { path: 'personen', component: PersonenRegisterComponent },
      { path: 'heilige', component: HeiligeRegisterComponent },
      { path: 'organisationen', component: OrganisationenRegisterComponent },
      { path: 'bibelstellen', component: BibelstellenRegisterComponent },
      { path: '**', redirectTo: '/notFound' },
    ],
  },
  {
    path: 'fotografien-dokumente',
    component: FotografieDokumenteComponent,
    children: [
      { path: 'verzeichnis', component: FotoDokVerzeichnisComponent },
      { path: ':groupType', component: PictureGalleryComponent },
      {
        path: ':groupType/:groupKey/:itemId',
        component: PhotoDocItemViewComponent,
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: '/fotografien-dokumente/verzeichnis',
      },
    ],
  },
  {
    path: 'materialien',
    component: MaterialienComponent,
    children: [{ path: ':id', component: MaterialienViewComponent }],
  },
  { path: 'suche', component: SucheComponent },
  { path: 'kontakt', component: KontaktComponent },
  { path: 'impressum', component: ImpressumComponent },
  { path: 'datenschutz', component: DatenschutzComponent },
  { path: 'danksagung', component: DanksagungComponent },
  { path: 'notFound', component: NotFoundComponent },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuard],
  },
  {
    path: 'admin/stat-report',
    component: AdminStatReportComponent,
    canActivate: [AdminGuard],
  },
  { path: '', pathMatch: 'full', redirectTo: '/startseite' },
  { path: '**', redirectTo: '/notFound' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      useHash: true,
      scrollPositionRestoration: 'top',
      initialNavigation: 'enabledBlocking',
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
