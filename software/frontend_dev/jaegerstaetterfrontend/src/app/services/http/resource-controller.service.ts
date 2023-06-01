import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  CorrespInfo,
  MarkRsRequest,
  NoteResourceDTO,
  ResourceDTO,
  SearchRequest,
} from 'src/app/models/dto';
import { ResourceListMsg, StringListMsg } from 'src/app/models/msg';
import { HttpConfigService } from '../http-config.service';

@Injectable({
  providedIn: 'root',
})
export class ResourceControllerService {
  readonly baseUrl: string;

  constructor(httpConfig: HttpConfigService, private http: HttpClient) {
    this.baseUrl = httpConfig.baseDir() + 'resource/';
  }

  toc(): Observable<ResourceListMsg> {
    const url = this.baseUrl;
    return this.http
      .get<ResourceListMsg>(url)
      .pipe(map((dto) => ResourceListMsg.fromDTO(dto)));
  }

  metadata(id: string): Observable<ResourceDTO> {
    const url = this.baseUrl + 'metadata/' + id;
    return this.http
      .get<ResourceDTO>(url)
      .pipe(map((dto) => ResourceDTO.fromDTO(dto)));
  }

  metadataWithDate(id: string, date: string): Observable<ResourceDTO> {
    const url = this.baseUrl + 'metadata/' + id + '?date=' + date;
    return this.http
      .get<ResourceDTO>(url)
      .pipe(map((dto) => ResourceDTO.fromDTO(dto)));
  }

  getAuthorKeys(): Observable<StringListMsg> {
    return this.getKeys('author');
  }
  getRecipientKeys(): Observable<StringListMsg> {
    return this.getKeys('recipient');
  }
  getPlaceKeys(): Observable<StringListMsg> {
    return this.getKeys('place');
  }
  getObjectTypeKeys(): Observable<StringListMsg> {
    return this.getKeys('objecttype');
  }

  getKeys(type: string): Observable<StringListMsg> {
    const url = this.baseUrl + 'keys/' + type;
    return this.http
      .get<StringListMsg>(url)
      .pipe(map((dto) => StringListMsg.fromDTO(dto)));
  }

  getXml(dto: ResourceDTO, includeHeader: boolean): Observable<string> {
    const url =
      this.baseUrl + 'xml/' + dto.id + '?includeHeader=' + includeHeader;
    return this.http.get(url, { responseType: 'text' });
  }

  getXmlWithDate(
    dto: ResourceDTO,
    includeHeader: boolean,
    date: string
  ): Observable<string> {
    if (!date) {
      return this.getXml(dto, includeHeader);
    }
    const url =
      this.baseUrl +
      'xml/' +
      dto.id +
      '?includeHeader=' +
      includeHeader +
      '&date=' +
      date;
    return this.http.get(url, { responseType: 'text' });
  }

  getNormWithSearch(
    dto: ResourceDTO,
    searchRequest: SearchRequest
  ): Observable<string> {
    const url = this.baseUrl + 'norm_highlight/' + dto.id;
    return this.http.post(url, searchRequest, { responseType: 'text' });
  }

  getNormMarkRs(
    dto: ResourceDTO,
    markRequest: MarkRsRequest
  ): Observable<string> {
    const url = this.baseUrl + 'norm_markrs/' + dto.id;
    return this.http.post(url, markRequest, { responseType: 'text' });
  }

  getNote(dto: ResourceDTO, noteId: string): Observable<NoteResourceDTO> {
    const url = this.baseUrl + 'notes/' + dto.id + '/' + noteId;
    return this.http
      .get<NoteResourceDTO>(url)
      .pipe(map((dto) => NoteResourceDTO.fromDTO(dto)));
  }

  getNoteWithDate(
    dto: ResourceDTO,
    noteId: string,
    date: string
  ): Observable<NoteResourceDTO> {
    const url =
      this.baseUrl + 'notes/' + dto.id + '/' + noteId + '?date=' + date;
    return this.http
      .get<NoteResourceDTO>(url)
      .pipe(map((dto) => NoteResourceDTO.fromDTO(dto)));
  }

  getCorrespPlaces(
    filename: string,
    anchorName: string
  ): Observable<CorrespInfo> {
    const url = this.baseUrl + 'corresp/' + filename + '/' + anchorName;
    return this.http
      .get<CorrespInfo>(url)
      .pipe(map((dto) => CorrespInfo.fromDTO(dto)));
  }
}
