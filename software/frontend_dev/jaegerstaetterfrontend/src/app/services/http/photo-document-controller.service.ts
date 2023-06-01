import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PhotoDocumentGroup } from 'src/app/models/dto';
import { HttpConfigService } from '../http-config.service';

@Injectable({
  providedIn: 'root',
})
export class PhotoDocumentControllerService {
  readonly baseUrl: string;

  constructor(httpConfig: HttpConfigService, private http: HttpClient) {
    this.baseUrl = httpConfig.baseDir() + 'photodocument/';
  }

  listAllGroupsAndItems(): Observable<PhotoDocumentGroup[]> {
    return this.http
      .get<PhotoDocumentGroup[]>(this.baseUrl)
      .pipe(map((dtos) => PhotoDocumentGroup.fromDTOs(dtos)));
  }

  listPhotoGroups(): Observable<PhotoDocumentGroup[]> {
    const url = this.baseUrl + 'photo';
    return this.http
      .get<PhotoDocumentGroup[]>(url)
      .pipe(map((dtos) => PhotoDocumentGroup.fromDTOs(dtos)));
  }

  listDocumentGroups(): Observable<PhotoDocumentGroup[]> {
    const url = this.baseUrl + 'document';
    return this.http
      .get<PhotoDocumentGroup[]>(url)
      .pipe(map((dtos) => PhotoDocumentGroup.fromDTOs(dtos)));
  }
}
