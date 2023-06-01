import { Injectable } from '@angular/core';
import { BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { catchError, Observable, ReplaySubject } from 'rxjs';
import { FilterAndSortSetting, SortableField } from '../frontend-model';
import { ErrorModalComponent } from '../modals/error-modal/error-modal.component';
import {
  ControlledVocabulary,
  CorporationInfo,
  GeonamesResult,
  GndRecord,
  PersonInfo,
  PlaceInfo,
  SaintInfo,
  TeiDocumentDTO,
  VocabularyType,
} from '../model';
import { RestApiService } from './rest-api.service';

@Injectable({
  providedIn: 'root',
})
export class AppService {
  personList: PersonInfo[];
  personList$: ReplaySubject<PersonInfo[]>;

  placeList: PlaceInfo[];
  placeList$: ReplaySubject<PlaceInfo[]>;

  saintList: SaintInfo[];
  saintList$: ReplaySubject<SaintInfo[]>;

  corporationList: CorporationInfo[];
  corporationList$: ReplaySubject<CorporationInfo[]>;

  filterAndSort: FilterAndSortSetting;
  filterAndSort$: ReplaySubject<FilterAndSortSetting>;

  constructor(
    private restApi: RestApiService,
    private bsModalService: BsModalService
  ) {
    this.personList$ = new ReplaySubject(1);
    this.placeList$ = new ReplaySubject(1);
    this.saintList$ = new ReplaySubject(1);
    this.corporationList$ = new ReplaySubject(1);
    this.filterAndSort$ = new ReplaySubject(1);
    this.setPersonList([]);
    this.setPlaceList([]);
    this.setSaintList([]);
    this.setCorporationList([]);
    this.setFilterAndSort(FilterAndSortSetting.EMPTY);
  }

  personFindAll(): void {
    this.restApi.findAllPersons().subscribe({
      next: (dtos) => this.setPersonList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  personUpdate(pi: PersonInfo): void {
    this.restApi.updatePerson(pi).subscribe({
      next: (dtos) => this.setPersonList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  personDelete(pi: PersonInfo): void {
    this.restApi.deletePerson(pi).subscribe({
      next: (dtos) => this.setPersonList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  placeFindAll(): void {
    this.restApi.findAllPlaces().subscribe({
      next: (dtos) => this.setPlaceList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  placeAutoAddGeoLocation(pi: PlaceInfo): void {
    this.restApi.autoAddGeoLocation(pi).subscribe({
      next: (dtos) => this.setPlaceList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  placeUpdate(pi: PlaceInfo): void {
    this.restApi.updatePlace(pi).subscribe({
      next: (dtos) => this.setPlaceList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  placeDelete(pi: PlaceInfo): void {
    this.restApi.deletePlace(pi).subscribe({
      next: (dtos) => this.setPlaceList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  saintFindAll(): void {
    this.restApi.findAllSaints().subscribe({
      next: (dtos) => this.setSaintList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  saintUpdate(si: SaintInfo): void {
    this.restApi.updateSaint(si).subscribe({
      next: (dtos) => this.setSaintList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  saintDelete(si: SaintInfo): void {
    this.restApi.deleteSaint(si).subscribe({
      next: (dtos) => this.setSaintList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  corporationFindAll(): void {
    this.restApi.findAllCorporations().subscribe({
      next: (dtos) => this.setCorporationList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  corporationUpdate(ci: CorporationInfo): void {
    this.restApi.updateCorporation(ci).subscribe({
      next: (dtos) => this.setCorporationList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  corporationDelete(ci: CorporationInfo): void {
    this.restApi.deleteCorporation(ci).subscribe({
      next: (dtos) => this.setCorporationList(dtos),
      error: (err) => this.viewError(err),
      complete: () => {},
    });
  }

  fsSetKeyFilter(keyFilter: string): void {
    this.setFilterAndSort(this.filterAndSort.changeKeyFilter(keyFilter));
  }

  fsSetStringFilter(sFilter: string): void {
    this.setFilterAndSort(this.filterAndSort.changeStringFilter(sFilter));
  }

  fsSetSortField(field: SortableField, asc?: boolean): void {
    this.setFilterAndSort(this.filterAndSort.changeSortField(field, asc));
  }

  fsToggleSortField(): void {
    this.setFilterAndSort(this.filterAndSort.toggleAscending());
  }

  searchGnd(
    queryString: string,
    type: VocabularyType,
    pn: number
  ): Observable<GndRecord[]> {
    return this.restApi.searchGnd(queryString, type, pn).pipe(
      catchError((err) => {
        this.viewError(err);
        throw err;
      })
    );
  }

  searchGeonames(
    queryString: string,
    pn: number
  ): Observable<GeonamesResult[]> {
    return this.restApi.searchGeonames(queryString, pn).pipe(
      catchError((err) => {
        this.viewError(err);
        throw err;
      })
    );
  }

  searchWikidata(
    queryString: string,
    type: VocabularyType,
    pn: number
  ): Observable<ControlledVocabulary[]> {
    return this.restApi.searchWikidata(queryString, type, pn).pipe(
      catchError((err) => {
        this.viewError(err);
        throw err;
      })
    );
  }

  publishFindRegistryDocs(): Observable<TeiDocumentDTO[]> {
    return this.restApi.getAllRegistryDocumentVersions().pipe(
      catchError((err) => {
        this.viewError(err);
        throw err;
      })
    );
  }

  publishPublishRegistry(oldVersion: number): Observable<TeiDocumentDTO> {
    return this.restApi.publishRegistry(oldVersion).pipe(
      catchError((err) => {
        this.viewError(err);
        throw err;
      })
    );
  }

  publishPeekTeiDoc(): Observable<TeiDocumentDTO> {
    return this.restApi.peekTeiDoc().pipe(
      catchError((err) => {
        this.viewError(err);
        throw err;
      })
    );
  }

  private viewError(err: Error): void {
    const initState: ModalOptions = {
      initialState: {
        message: err.message,
      },
    };
    this.bsModalService.show(ErrorModalComponent, initState);
  }

  private setPersonList(list: PersonInfo[]): void {
    this.personList = list ? list : [];
    this.personList$.next(this.personList);
  }
  private setPlaceList(list: PlaceInfo[]): void {
    this.placeList = list ? list : [];
    this.placeList$.next(this.placeList);
  }
  private setSaintList(list: SaintInfo[]): void {
    this.saintList = list ? list : [];
    this.saintList$.next(this.saintList);
  }
  private setCorporationList(list: CorporationInfo[]): void {
    this.corporationList = list ? list : [];
    this.corporationList$.next(this.corporationList);
  }
  private setFilterAndSort(filterAndSort: FilterAndSortSetting): void {
    this.filterAndSort = filterAndSort;
    this.filterAndSort$.next(this.filterAndSort);
  }
}
