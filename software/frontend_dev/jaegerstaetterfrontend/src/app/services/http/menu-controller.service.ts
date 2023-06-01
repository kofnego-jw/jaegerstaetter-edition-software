import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CommentDocMsg, MenuItemLisMsg } from 'src/app/models/msg';
import { HttpConfigService } from '../http-config.service';

@Injectable({
  providedIn: 'root',
})
export class MenuControllerService {
  readonly baseUrl: string;

  constructor(httpConfig: HttpConfigService, private http: HttpClient) {
    this.baseUrl = httpConfig.baseDir() + 'menu/';
  }

  startCommentDoc(): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'start';
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  digitalEditionMenu(): Observable<MenuItemLisMsg> {
    const url = this.baseUrl + 'digitale_edition';
    return this.http
      .get<MenuItemLisMsg>(url)
      .pipe(map((dto) => MenuItemLisMsg.fromDTO(dto)));
  }

  digitalEditionCommentDoc(docKey: string): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'digitale_edition/' + docKey;
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  registryDocument(registertype: string): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'register_dokument/' + registertype;
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  biographyIndex(): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'biography/index.xml';
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  materialMenu(): Observable<MenuItemLisMsg> {
    const url = this.baseUrl + 'materials';
    return this.http
      .get<MenuItemLisMsg>(url)
      .pipe(map((dto) => MenuItemLisMsg.fromDTO(dto)));
  }

  materialDocument(key: string): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'materials/' + key;
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  contactCommentDoc(): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'contact';
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  gdprCommentDoc(): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'gdpr';
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  imprintCommentDoc(): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'imprint';
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  acknowledgementsCommentDoc(): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'acknowledgements';
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }

  specialCorrespCommentDoc(): Observable<CommentDocMsg> {
    const url = this.baseUrl + 'special_correspondence';
    return this.http
      .get<CommentDocMsg>(url)
      .pipe(map((dto) => CommentDocMsg.fromDTO(dto)));
  }
}
