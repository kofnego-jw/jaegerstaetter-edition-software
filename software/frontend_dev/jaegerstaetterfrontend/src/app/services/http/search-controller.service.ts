import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SearchRequest } from 'src/app/models/dto';
import { SearchResultMsg } from 'src/app/models/msg';
import { HttpConfigService } from '../http-config.service';

@Injectable({
  providedIn: 'root',
})
export class SearchControllerService {
  readonly baseUrl: string;

  constructor(httpConfig: HttpConfigService, private http: HttpClient) {
    this.baseUrl = httpConfig.baseDir() + 'search/';
  }

  search(request: SearchRequest): Observable<SearchResultMsg> {
    const url = this.baseUrl;
    return this.http
      .post<SearchResultMsg>(url, request)
      .pipe(map((dto) => SearchResultMsg.fromDTO(dto)));
  }
}
