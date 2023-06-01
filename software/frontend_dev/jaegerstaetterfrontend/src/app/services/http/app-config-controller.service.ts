import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppConfigMsg } from 'src/app/models/msg';
import { HttpConfigService } from '../http-config.service';

@Injectable({
  providedIn: 'root',
})
export class AppConfigControllerService {
  readonly baseUrl: string;

  constructor(httpConfig: HttpConfigService, private http: HttpClient) {
    this.baseUrl = httpConfig.baseDir() + 'app_config/';
  }

  getAppConfig(): Observable<AppConfigMsg> {
    return this.http
      .get<AppConfigMsg>(this.baseUrl)
      .pipe(map((dto) => AppConfigMsg.fromDTO(dto)));
  }
}
