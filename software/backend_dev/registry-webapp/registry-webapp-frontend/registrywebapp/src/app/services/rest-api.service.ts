import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import {
  ControlledVocabulary,
  CorporationInfo,
  GeonamesResult,
  GndRecord,
  PersonInfo,
  PlaceInfo,
  RegistryPreviewDTO,
  SaintInfo,
  TeiDocumentDTO,
  VocabularyType,
} from '../model';

@Injectable({
  providedIn: 'root',
})
export class RestApiService {
  constructor(private http: HttpClient) {}

  findAllPersons(): Observable<PersonInfo[]> {
    return this.http
      .get<PersonInfo[]>('api/person/')
      .pipe(map((dtos) => PersonInfo.fromDTOs(dtos)));
  }

  addNewPerson(person: PersonInfo): Observable<PersonInfo[]> {
    return this.http
      .put<PersonInfo[]>('api/person/', person)
      .pipe(map((dtos) => PersonInfo.fromDTOs(dtos)));
  }

  updatePerson(person: PersonInfo): Observable<PersonInfo[]> {
    return this.http
      .post<PersonInfo[]>('api/person/', person)
      .pipe(map((dtos) => PersonInfo.fromDTOs(dtos)));
  }

  deletePerson(person: PersonInfo): Observable<PersonInfo[]> {
    return this.http
      .delete<PersonInfo[]>('api/person/' + person.key)
      .pipe(map((dtos) => PersonInfo.fromDTOs(dtos)));
  }

  findAllPlaces(): Observable<PlaceInfo[]> {
    return this.http
      .get<PlaceInfo[]>('api/place/')
      .pipe(map((dtos) => PlaceInfo.fromDTOs(dtos)));
  }

  addNewPlace(place: PlaceInfo): Observable<PlaceInfo[]> {
    return this.http
      .put<PlaceInfo[]>('api/place/', place)
      .pipe(map((dtos) => PlaceInfo.fromDTOs(dtos)));
  }

  updatePlace(place: PlaceInfo): Observable<PlaceInfo[]> {
    return this.http
      .post<PlaceInfo[]>('api/place/', place)
      .pipe(map((dtos) => PlaceInfo.fromDTOs(dtos)));
  }

  deletePlace(place: PlaceInfo): Observable<PlaceInfo[]> {
    return this.http
      .delete<PlaceInfo[]>('api/place/' + place.key)
      .pipe(map((dtos) => PlaceInfo.fromDTOs(dtos)));
  }

  findAllSaints(): Observable<SaintInfo[]> {
    return this.http
      .get<SaintInfo[]>('api/saint/')
      .pipe(map((dtos) => SaintInfo.fromDTOs(dtos)));
  }

  addNewSaint(saint: SaintInfo): Observable<SaintInfo[]> {
    return this.http
      .put<SaintInfo[]>('api/saint/', saint)
      .pipe(map((dtos) => SaintInfo.fromDTOs(dtos)));
  }

  updateSaint(saint: SaintInfo): Observable<SaintInfo[]> {
    return this.http
      .post<SaintInfo[]>('api/saint/', saint)
      .pipe(map((dtos) => SaintInfo.fromDTOs(dtos)));
  }

  deleteSaint(saint: SaintInfo): Observable<SaintInfo[]> {
    return this.http
      .delete<SaintInfo[]>('api/saint/' + saint.key)
      .pipe(map((dtos) => SaintInfo.fromDTOs(dtos)));
  }

  findAllCorporations(): Observable<CorporationInfo[]> {
    return this.http
      .get<CorporationInfo[]>('api/corporation/')
      .pipe(map((dtos) => CorporationInfo.fromDTOs(dtos)));
  }

  addNewCorporation(
    corporation: CorporationInfo
  ): Observable<CorporationInfo[]> {
    return this.http
      .put<CorporationInfo[]>('api/corporation/', corporation)
      .pipe(map((dtos) => CorporationInfo.fromDTOs(dtos)));
  }

  updateCorporation(
    corporation: CorporationInfo
  ): Observable<CorporationInfo[]> {
    return this.http
      .post<CorporationInfo[]>('api/corporation/', corporation)
      .pipe(map((dtos) => CorporationInfo.fromDTOs(dtos)));
  }

  deleteCorporation(
    corporation: CorporationInfo
  ): Observable<CorporationInfo[]> {
    return this.http
      .delete<CorporationInfo[]>('api/corporation/' + corporation.key)
      .pipe(map((dtos) => CorporationInfo.fromDTOs(dtos)));
  }

  searchGnd(
    query: string,
    type: VocabularyType,
    pageNumber: number
  ): Observable<GndRecord[]> {
    const params = new FormData();
    params.append('queryString', query);
    return this.http
      .post<GndRecord[]>(`api/search/gnd/${type}/${pageNumber}`, params)
      .pipe(map((recs) => GndRecord.fromDTOs(recs)));
  }

  searchGeonames(
    query: string,
    pageNumber: number
  ): Observable<GeonamesResult[]> {
    const params = new FormData();
    params.append('queryString', query);
    return this.http
      .post<GeonamesResult[]>(`api/search/geonames/${pageNumber}`, params)
      .pipe(map((recs) => GeonamesResult.fromDTOs(recs)));
  }

  searchWikidata(
    query: string,
    type: VocabularyType,
    pageNumber: number
  ): Observable<ControlledVocabulary[]> {
    const params = new FormData();
    params.append('queryString', query);
    return this.http
      .post<ControlledVocabulary[]>(
        `api/search/wikidata/${type}/${pageNumber}`,
        params
      )
      .pipe(map((recs) => ControlledVocabulary.fromDTOs(recs)));
  }

  autoAddGeoLocation(place: PlaceInfo): Observable<PlaceInfo[]> {
    return this.http
      .post<PlaceInfo[]>('api/geoLocation', place)
      .pipe(map((dtos) => PlaceInfo.fromDTOs(dtos)));
  }

  previewRegistry(
    type: VocabularyType,
    key: string
  ): Observable<RegistryPreviewDTO> {
    const param = new FormData();
    param.append('key', key);
    param.append('type', type);
    return this.http
      .post<RegistryPreviewDTO>('api/registrydoc/preview/', param)
      .pipe(map((dto) => RegistryPreviewDTO.fromDTO(dto)));
  }

  publishRegistry(oldVersion: number): Observable<TeiDocumentDTO> {
    const param = new FormData();
    param.append('oldversion', oldVersion.toString());
    console.log(param);
    return this.http
      .post<TeiDocumentDTO>('api/registrydoc/publish', param)
      .pipe(map((dto) => TeiDocumentDTO.fromDTO(dto)));
  }

  getCurrentRegistryDocument(): Observable<TeiDocumentDTO> {
    return this.http
      .get<TeiDocumentDTO>('api/registrydoc/')
      .pipe(map((dto) => TeiDocumentDTO.fromDTO(dto)));
  }

  getAllRegistryDocumentVersions(): Observable<TeiDocumentDTO[]> {
    return this.http
      .get<TeiDocumentDTO[]>('api/registrydoc/allversions/')
      .pipe(map((dtos) => TeiDocumentDTO.fromDTOs(dtos)));
  }

  peekTeiDoc(): Observable<TeiDocumentDTO> {
    return this.http
      .get<TeiDocumentDTO>('api/registrydoc/peekTeiDoc')
      .pipe(map((dtos) => TeiDocumentDTO.fromDTO(dtos)));
  }
}
