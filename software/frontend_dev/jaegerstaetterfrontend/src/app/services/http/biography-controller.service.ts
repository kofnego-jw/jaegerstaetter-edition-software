import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Biography, BiographyFW } from 'src/app/models/dto';
import { HttpConfigService } from '../http-config.service';

@Injectable({
  providedIn: 'root',
})
export class BiographyControllerService {
  readonly baseUrl: string;

  constructor(httpConfig: HttpConfigService, private http: HttpClient) {
    this.baseUrl = httpConfig.baseDir() + 'biography/';
  }

  listBiographies(): Observable<BiographyFW[]> {
    return this.http
      .get<BiographyFW[]>(this.baseUrl)
      .pipe(map((dtos) => BiographyFW.fromDTOs(dtos)));
  }

  getBiography(key: string): Observable<Biography> {
    const url = this.baseUrl + key;
    return this.http
      .get<Biography>(url)
      .pipe(map((dto) => Biography.fromDTO(dto)));
  }
}
