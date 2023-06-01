import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  PasswordDTO,
  StatElementFullDesc,
  StatReport,
} from 'src/app/models/dto';
import { BasicMsg } from 'src/app/models/msg';
import { HttpConfigService } from '../http-config.service';

@Injectable({
  providedIn: 'root',
})
export class AdminControllerService {
  readonly baseUrl: string;

  constructor(httpConfig: HttpConfigService, private http: HttpClient) {
    this.baseUrl = httpConfig.baseDir() + 'admin/';
  }

  cloneEditionToPreview(password: PasswordDTO): Observable<BasicMsg> {
    const url = this.baseUrl + 'clone_edition_to_preview';
    return this.http
      .post<BasicMsg>(url, password)
      .pipe(map((dto) => BasicMsg.fromDTO(dto)));
  }

  cloneEditionAndIngestToPreview(password: PasswordDTO): Observable<BasicMsg> {
    const url = this.baseUrl + 'clone_edition_to_preview_and_ingest';
    return this.http
      .post<BasicMsg>(url, password)
      .pipe(map((dto) => BasicMsg.fromDTO(dto)));
  }

  ingestToEdition(password: PasswordDTO): Observable<BasicMsg> {
    const url = this.baseUrl + 'ingest_to_edition';
    return this.http
      .post<BasicMsg>(url, password)
      .pipe(map((dto) => BasicMsg.fromDTO(dto)));
  }

  getStatistics(): Observable<StatReport> {
    const url = this.baseUrl + 'statistics';
    return this.http
      .get<StatReport>(url)
      .pipe(map((dto) => StatReport.fromDTO(dto)));
  }

  getElementNames(type: string): Observable<string[]> {
    const url = this.baseUrl + 'statistics/' + type;
    return this.http.get<string[]>(url);
  }

  getElementDesc(
    type: string,
    elementName: string
  ): Observable<StatElementFullDesc> {
    const url = this.baseUrl + 'statistics/' + type + '/' + elementName;
    return this.http
      .get<StatElementFullDesc>(url)
      .pipe(map((dto) => StatElementFullDesc.fromDTO(dto)));
  }
}
