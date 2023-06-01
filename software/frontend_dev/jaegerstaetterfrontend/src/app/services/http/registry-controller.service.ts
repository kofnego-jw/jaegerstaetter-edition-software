import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, ReplaySubject } from 'rxjs';
import { delay, map, share, tap } from 'rxjs/operators';
import {
  IndexEntryBiblePassages,
  IndexEntryCorporation,
  IndexEntryPerson,
  IndexEntryPlace,
  IndexEntrySaint,
  RegistryEntryBibleBook,
  RegistryEntryCorporation,
  RegistryEntryPerson,
  RegistryEntryPlace,
  RegistryEntrySaint,
  RegistryType,
} from 'src/app/models/dto';
import { HttpConfigService } from '../http-config.service';

@Injectable({
  providedIn: 'root',
})
export class RegistryControllerService {
  readonly baseUrl: string;

  private personEntries$: Observable<RegistryEntryPerson[]> | undefined =
    undefined;
  private placeEntries$: Observable<RegistryEntryPlace[]> | undefined =
    undefined;
  private corporationEntries$:
    | Observable<RegistryEntryCorporation[]>
    | undefined = undefined;
  private saintEntries$: Observable<RegistryEntrySaint[]> | undefined =
    undefined;

  constructor(httpConfig: HttpConfigService, private http: HttpClient) {
    this.baseUrl = httpConfig.baseDir() + 'registry/';
  }

  listCorporations(): Observable<RegistryEntryCorporation[]> {
    if (this.corporationEntries$) {
      return this.corporationEntries$;
    }
    const url = this.baseUrl + 'corporation/';
    this.corporationEntries$ = this.http
      .get<RegistryEntryCorporation[]>(url)
      .pipe(
        map((dtos) => RegistryEntryCorporation.fromDTOs(dtos)),
        share()
      );
    return this.corporationEntries$;
  }

  listCorporationResources(corpKey: string): Observable<IndexEntryCorporation> {
    const url = this.baseUrl + 'corporation/' + corpKey;
    return this.http
      .get<IndexEntryCorporation>(url)
      .pipe(map((dto) => IndexEntryCorporation.fromDTO(dto)));
  }

  listPersons(): Observable<RegistryEntryPerson[]> {
    if (this.personEntries$) {
      return this.personEntries$;
    }
    const url = this.baseUrl + 'person/';
    this.personEntries$ = this.http.get<RegistryEntryPerson[]>(url).pipe(
      map((dtos) => RegistryEntryPerson.fromDTOs(dtos)),
      share()
    );
    return this.personEntries$;
  }

  listPersonResources(personKey: string): Observable<IndexEntryPerson> {
    const url = this.baseUrl + 'person/' + personKey;
    return this.http
      .get<IndexEntryPerson>(url)
      .pipe(map((dto) => IndexEntryPerson.fromDTO(dto)));
  }

  listPlaces(): Observable<RegistryEntryPlace[]> {
    if (this.placeEntries$) {
      return this.placeEntries$;
    }
    const url = this.baseUrl + 'place/';
    this.placeEntries$ = this.http.get<RegistryEntryPlace[]>(url).pipe(
      map((dtos) => RegistryEntryPlace.fromDTOs(dtos)),
      share()
    );
    return this.placeEntries$;
  }

  listPlaceResources(placeKey: string): Observable<IndexEntryPlace> {
    const url = this.baseUrl + 'place/' + placeKey;
    return this.http
      .get<IndexEntryPlace>(url)
      .pipe(map((dto) => IndexEntryPlace.fromDTO(dto)));
  }

  listSaints(): Observable<RegistryEntrySaint[]> {
    if (this.saintEntries$) {
      return this.saintEntries$;
    }
    const url = this.baseUrl + 'saint/';
    this.saintEntries$ = this.http.get<RegistryEntrySaint[]>(url).pipe(
      map((dtos) => RegistryEntrySaint.fromDTOs(dtos)),
      share()
    );
    return this.saintEntries$;
  }

  listSaintResources(saintKey: string): Observable<IndexEntrySaint> {
    const url = this.baseUrl + 'saint/' + saintKey;
    return this.http
      .get<IndexEntrySaint>(url)
      .pipe(map((dto) => IndexEntrySaint.fromDTO(dto)));
  }

  listBibleBooks(): Observable<RegistryEntryBibleBook[]> {
    const url = this.baseUrl + 'bible/';
    return this.http
      .get<RegistryEntryBibleBook[]>(url)
      .pipe(map((dtos) => RegistryEntryBibleBook.fromDTOs(dtos)));
  }

  listBiblePassageResource(book: string): Observable<IndexEntryBiblePassages> {
    const url = this.baseUrl + 'bible/' + book;
    return this.http
      .get<IndexEntryBiblePassages>(url)
      .pipe(map((dto) => IndexEntryBiblePassages.fromDTO(dto)));
  }
}
