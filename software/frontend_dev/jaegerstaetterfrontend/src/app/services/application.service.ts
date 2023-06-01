import { Injectable } from '@angular/core';
import {
  DomSanitizer,
  SafeResourceUrl,
  SafeUrl,
} from '@angular/platform-browser';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EMPTY, Observable, of, ReplaySubject, Subject } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { ErrorModalComponent } from '../modals/error-modal/error-modal.component';
import {
  Biography,
  BiographyFW,
  CommentDoc,
  CorrespInfo,
  IndexEntryBiblePassages,
  IndexEntryCorporation,
  IndexEntryPerson,
  IndexEntryPlace,
  IndexEntrySaint,
  MarkRsRequest,
  MenuItem,
  NoteResourceDTO,
  PhotoDocumentGroup,
  PhotoDocumentItem,
  RegistryEntryBibleBook,
  RegistryEntryCorporation,
  RegistryEntryPerson,
  RegistryEntryPlace,
  RegistryEntrySaint,
  RegistryType,
  ResourceDTO,
  ResourceFWDTO,
  SearchField,
  SearchFieldParam,
  SearchOccur,
  SearchRequest,
  SearchResult,
} from '../models/dto';
import {
  CommentDocMsg,
  MenuItemLisMsg,
  SearchResultMsg,
  StringListMsg,
} from '../models/msg';
import { AppConfigControllerService } from './http/app-config-controller.service';
import { BiographyControllerService } from './http/biography-controller.service';
import { MenuControllerService } from './http/menu-controller.service';
import { PhotoDocumentControllerService } from './http/photo-document-controller.service';
import { RegistryControllerService } from './http/registry-controller.service';
import { ResourceControllerService } from './http/resource-controller.service';
import { SearchControllerService } from './http/search-controller.service';

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {
  photoDocumentGroups: PhotoDocumentGroup[] = [];
  photoDocumentGroups$: ReplaySubject<PhotoDocumentGroup[]> = new ReplaySubject(
    1
  );

  resourceList: ResourceFWDTO[] = [];
  resourceList$: ReplaySubject<ResourceFWDTO[]> = new ReplaySubject(1);

  currentResource: ResourceDTO = null;
  currentResource$: ReplaySubject<ResourceDTO> = new ReplaySubject(1);

  preview: boolean = false;
  isPreview: ReplaySubject<boolean> = new ReplaySubject(1);

  constructor(
    appConfigController: AppConfigControllerService,
    private sanitizer: DomSanitizer,
    private menuController: MenuControllerService,
    private photoDocumentController: PhotoDocumentControllerService,
    private biographyController: BiographyControllerService,
    private resourceController: ResourceControllerService,
    private registryController: RegistryControllerService,
    private searchController: SearchControllerService,
    private bsModalService: BsModalService
  ) {
    appConfigController
      .getAppConfig()
      .pipe(take(1))
      .subscribe((msg) => {
        this.preview = !msg.edition;
        this.isPreview.next(this.preview);
      });
    this.photoDocumentGroups$.next(this.photoDocumentGroups);
    this.pd_loadPhotoDocumentGroups();

    this.loadResourceList();
  }

  // Digital Edition Menu
  de_getMenuItems(): Observable<MenuItem[]> {
    return this.menuController.digitalEditionMenu().pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new MenuItemLisMsg('http error', true, []))
        )
      ),
      map((msg) => msg.menuItemList)
    );
  }

  de_loadCommentDoc(id: string): Observable<CommentDoc> {
    return this.menuController.digitalEditionCommentDoc(id).pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new CommentDocMsg('http error', true, null))
        )
      ),
      map((msg) => msg.commentDoc)
    );
  }

  // Materials routines
  ma_getMenuItems(): Observable<MenuItem[]> {
    return this.menuController.materialMenu().pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new MenuItemLisMsg('http error', true, []))
        )
      ),
      map((msg) => msg.menuItemList)
    );
  }

  ma_loadMaterialDoc(id: string): Observable<CommentDoc> {
    return this.menuController.materialDocument(id).pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new CommentDocMsg('http error', true, null))
        )
      ),
      map((msg) => msg.commentDoc)
    );
  }

  // PhotoDocumentation Routines
  pd_loadPhotoDocumentGroups(): void {
    this.photoDocumentController
      .listAllGroupsAndItems()
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of([]))))
      .subscribe((list) => {
        this.photoDocumentGroups = list;
        this.photoDocumentGroups$.next(this.photoDocumentGroups);
      });
  }

  pd_getPhotoDocumentGroup(key: string): PhotoDocumentGroup {
    const found = this.photoDocumentGroups.filter(
      (group) => group.groupKey === key
    );
    if (found.length) {
      return found[0];
    }
    return null;
  }

  pd_imageSourceAsString(
    group: PhotoDocumentGroup,
    item: PhotoDocumentItem
  ): string {
    const jpg = item.jpegs?.length ? item.jpegs[0] : '';
    return (
      this.photoDocumentController.baseUrl + group.groupKey + '/' + jpg + '.jpg'
    );
  }

  pd_imageSource(
    group: PhotoDocumentGroup,
    item: PhotoDocumentItem
  ): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      this.pd_imageSourceAsString(group, item)
    );
  }

  pd_imageSourceWithJpgAsString(
    group: PhotoDocumentGroup,
    jpg: string
  ): string {
    return (
      this.photoDocumentController.baseUrl + group.groupKey + '/' + jpg + '.jpg'
    );
  }

  pd_imageSourceWithJpg(
    group: PhotoDocumentGroup,
    jpg: string
  ): SafeResourceUrl {
    const url = this.pd_imageSourceWithJpgAsString(group, jpg);
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  pd_routerLinkGroup(group: PhotoDocumentGroup): string {
    return '/fotografien-dokumente/' + group.type.toString().toLowerCase();
  }

  pd_routerLink(group: PhotoDocumentGroup, item: PhotoDocumentItem): string {
    if (group?.type && group.groupKey && item?.signature) {
      return (
        '/fotografien-dokumente/' +
        group.type.toString().toLowerCase() +
        '/' +
        group.groupKey +
        '/' +
        item.id
      );
    }
    return '/fotografien-dokumente/';
  }

  // Biography Routines
  bg_loadIndex(): Observable<CommentDoc> {
    return this.menuController.biographyIndex().pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new CommentDocMsg('http error', true, null))
        )
      ),
      map((msg) => msg.commentDoc)
    );
  }

  bg_getBiographies(): Observable<BiographyFW[]> {
    return this.biographyController
      .listBiographies()
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of([]))));
  }

  bg_getSingleBiography(id: string): Observable<Biography> {
    return this.biographyController
      .getBiography(id)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(null))));
  }

  bg_getImageResource(src: string): string {
    return 'api/biography/images/' + src;
  }

  re_getPersonRegistry(): Observable<RegistryEntryPerson[]> {
    return this.registryController
      .listPersons()
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of([]))));
  }

  re_getPersonIndex(key: string): Observable<IndexEntryPerson> {
    return this.registryController
      .listPersonResources(key)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(null))));
  }

  re_getPlaceRegistry(): Observable<RegistryEntryPlace[]> {
    return this.registryController
      .listPlaces()
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of([]))));
  }
  re_getPlaceIndex(key: string): Observable<IndexEntryPlace> {
    return this.registryController
      .listPlaceResources(key)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(null))));
  }

  re_getSaintRegistry(): Observable<RegistryEntrySaint[]> {
    return this.registryController
      .listSaints()
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of([]))));
  }
  re_getSaintIndex(key: string): Observable<IndexEntrySaint> {
    return this.registryController
      .listSaintResources(key)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(null))));
  }

  re_getCorporationRegistry(): Observable<RegistryEntryCorporation[]> {
    return this.registryController
      .listCorporations()
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of([]))));
  }
  re_getCorporationIndex(key: string): Observable<IndexEntryCorporation> {
    return this.registryController
      .listCorporationResources(key)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(null))));
  }

  re_getBibleRegistry(): Observable<RegistryEntryBibleBook[]> {
    return this.registryController
      .listBibleBooks()
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of([]))));
  }
  re_getBibleBookEntry(book: string): Observable<IndexEntryBiblePassages> {
    return this.registryController
      .listBiblePassageResource(book)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(null))));
  }
  re_getRegistryDoc(type: RegistryType | string): Observable<CommentDoc> {
    let registerType: string;
    switch (type) {
      case RegistryType.PERSON:
        registerType = 'personen';
        break;
      case RegistryType.PLACE:
        registerType = 'orte';
        break;
      case RegistryType.CORPORATION:
        registerType = 'organisationen';
        break;
      case RegistryType.SAINT:
        registerType = 'heilige';
        break;
      default:
        registerType = 'bibelstellen';
    }
    return this.menuController.registryDocument(registerType).pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new CommentDocMsg('error', true, null))
        )
      ),
      map((msg) => (msg == null ? null : msg.commentDoc))
    );
  }

  // Search API

  se_simpleSearch(queryString: string): Observable<SearchResult> {
    const request = new SearchRequest(
      [new SearchFieldParam(SearchField.ALL, queryString, SearchOccur.SHOULD)],
      0,
      20,
      null,
      true
    );
    return this.se_search(request);
  }

  se_search(request: SearchRequest): Observable<SearchResult> {
    return this.searchController.search(request).pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(
            new SearchResultMsg(
              'Unknown HTTP error',
              true,
              new SearchResult(request, 0, 0, 0, [])
            )
          )
        )
      ),
      map((msg) => msg.searchResult)
    );
  }

  res_getFloatingHrefFromResource(fw: ResourceFWDTO | ResourceDTO): SafeUrl {
    return this.res_getFloatingHrefFromResourceId(fw.id);
  }

  res_getFloatingHrefFromResourceId(id: string): SafeUrl {
    if (id?.startsWith('biografien/')) {
      const filename = id.substring(id.lastIndexOf('/') + 1);
      const url = '/#/biografien/' + filename + '?floating=true';
      return this.sanitizer.bypassSecurityTrustUrl(url);
    }
    const url = `/#/edition/view/${id}?floating=true`;
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  res_getFloatingHrefFromResourceIdAndScroll(
    id: string,
    scrollTo: string
  ): SafeUrl {
    if (id?.startsWith('biografien/')) {
      const filename = id.substring(id.lastIndexOf('/') + 1);
      const url = '/#/biografien/' + filename + '?floating=true';
      return this.sanitizer.bypassSecurityTrustUrl(url);
    }
    const url = `/#/edition/view/${id}?floating=true&scrollTo=${scrollTo}`;
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  loadResourceList(): void {
    this.resourceController.toc().subscribe({
      next: (msg) => this.setResourceList(msg.resources),
      error: (err) => this.handleHttpError(err),
    });
  }

  setResourceList(list: ResourceFWDTO[]): void {
    this.resourceList = list;
    this.resourceList$.next(this.resourceList);
  }

  getAuthorKeys(): Observable<string[]> {
    return this.resourceController.getAuthorKeys().pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new StringListMsg('Unknown HTTP error', true, []))
        )
      ),
      map((msg) => msg.list)
    );
  }
  getRecipientKeys(): Observable<string[]> {
    return this.resourceController.getRecipientKeys().pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new StringListMsg('Unknown HTTP error', true, []))
        )
      ),
      map((msg) => msg.list)
    );
  }
  getPlaceKeys(): Observable<string[]> {
    return this.resourceController.getPlaceKeys().pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new StringListMsg('Unknown HTTP error', true, []))
        )
      ),
      map((msg) => msg.list)
    );
  }
  getObjectTypeKeys(): Observable<string[]> {
    return this.resourceController.getObjectTypeKeys().pipe(
      catchError((err) =>
        this.handleHttpErrorAndReturn(
          err,
          of(new StringListMsg('Unknown HTTP error', true, []))
        )
      ),
      map((msg) => msg.list)
    );
  }

  getXml(resource: ResourceDTO, includeHeader: boolean): Observable<string> {
    return this.resourceController
      .getXml(resource, includeHeader)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(''))));
  }

  getXmlWithDate(
    resource: ResourceDTO,
    date: string,
    includeHeader: boolean
  ): Observable<string> {
    return this.resourceController
      .getXmlWithDate(resource, includeHeader, date)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(''))));
  }

  getNormWithSearch(
    resource: ResourceDTO,
    searchRequest: SearchRequest
  ): Observable<string> {
    return this.resourceController
      .getNormWithSearch(resource, searchRequest)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(''))));
  }

  getNormWithMarkRs(
    resource: ResourceDTO,
    mark: MarkRsRequest
  ): Observable<string> {
    return this.resourceController
      .getNormMarkRs(resource, mark)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(''))));
  }

  getNote(noteId: string): Observable<NoteResourceDTO> {
    return this.resourceController
      .getNote(this.currentResource, noteId)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(null))));
  }

  getNoteWithDate(noteId: string, date: string): Observable<NoteResourceDTO> {
    return this.resourceController
      .getNoteWithDate(this.currentResource, noteId, date)
      .pipe(catchError((err) => this.handleHttpErrorAndReturn(err, of(null))));
  }

  getCorrespInfo(corresp: string): Observable<CorrespInfo> {
    const index = corresp.indexOf('#');
    if (index > 0) {
      const filename = corresp.substring(0, index);
      const anchor = corresp.substring(index + 1);
      return this.resourceController
        .getCorrespPlaces(filename, anchor)
        .pipe(
          catchError((err) =>
            this.handleHttpErrorAndReturn(err, of(new CorrespInfo('', [])))
          )
        );
    }
    const index2 = corresp.indexOf('_xml_');
    if (index2 > 0) {
    }
    return of(new CorrespInfo('', []));
  }

  getPdfUrl(res: ResourceDTO): SafeUrl {
    const url = 'api/resource/norm_pdf/' + res.id + '.pdf';
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  getXmlUrl(res: ResourceDTO): SafeUrl {
    const url = 'api/resource/xml/' + res.id;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  getTitleById(id: string): string {
    if (this.resourceList?.length) {
      const found = this.resourceList.filter((dto) => dto.id === id);
      if (found.length) {
        return found[0].title;
      }
    }
    return id;
  }

  handleHttpError(err: any): any {
    const modalRef = this.bsModalService.show(ErrorModalComponent);
    const msg = err?.message
      ? err.message
      : 'Unbekannter Fehler, bitte Konsole checken.';
    console.log('ERROR');
    console.log(err);
    console.log('-----');
    modalRef.content.message = msg;
    return EMPTY;
  }

  handleHttpErrorAndReturn<T>(err: any, result: T): T {
    this.handleHttpError(err);
    return result;
  }

  setResourceById(id: string): void {
    this.resourceController.metadata(id).subscribe({
      next: (res) => this.setCurrentResource(res),
      error: (err) => this.handleHttpError(err),
    });
  }

  setResourceByIdAndDate(id: string, date: string): void {
    this.resourceController.metadataWithDate(id, date).subscribe({
      next: (res) => this.setCurrentResource(res),
      error: (err) => this.handleHttpError(err),
    });
  }

  setCurrentResource(res: ResourceDTO): void {
    this.currentResource = res;
    this.currentResource$.next(this.currentResource);
  }

  gap_translateReason(reason: string): string {
    if (!reason) {
      return 'Unleserlich';
    }
    switch (reason) {
      case 'filler':
        return 'Linie';
      //case 'illegible': return 'Unleserlich'
      case 'scribble':
        return 'Gekritzel';
    }
    return 'Unleserlich';
  }

  unclear_translateReason(reason: string): string {
    if (!reason) {
      return 'Unleserlich';
    }
    if (reason.indexOf('faded')) {
      return 'Verblasst';
    }
    if (reason.indexOf('smeared')) {
      return 'Verschmiert';
    }
    if (reason.indexOf('stamp')) {
      return 'Überstempelt';
    }
    if (reason.indexOf('ink_plot') || reason.indexOf('blot')) {
      return 'Tintenfleck';
    }
    return 'Unleserlich';
  }

  note_translateNoteType(type: string): string {
    if (!type) {
      return '';
    }
    const myType = type.toLowerCase();
    switch (myType) {
      case 'slang':
        return 'sprachliche Erläuterung';
      case 'definition':
        return 'Kommentar';
      case 'literature':
        return 'Literaturverweis';
      case 'collection':
        return 'Sammlungshinweis';
      case 'bible':
        return 'Bibelstellenverweis';
      case 'bible_ketter':
        return 'Bibelstellenverweis';
      case 'biography':
        return 'Biografieverweis';
      case 'reference':
        return 'Onlineverweis';
    }
    return 'unbekannt';
  }

  cert_translation(cert: string): string {
    if (!cert) {
      return '';
    }
    switch (cert) {
      case 'high':
        return 'wahrscheinl. ';
      case 'medium':
        return 'vermutl. ';
      case 'low':
        return 'mögl. ';
    }
    return '';
  }

  removeLeadingZero(s: string): string {
    let myString = s;
    while (myString.startsWith('0')) {
      myString = myString.substring(1);
    }
    return myString;
  }

  dateStringToDate(s: string): string {
    const matches = /(\d{4})-(\d{2})-(\d{2}).*/;
    const groups = matches.exec(s);
    if (groups.length === 4) {
      return (
        this.removeLeadingZero(groups[3]) +
        '.' +
        this.removeLeadingZero(groups[2]) +
        '.' +
        groups[1]
      );
    }
    return s;
  }
}
