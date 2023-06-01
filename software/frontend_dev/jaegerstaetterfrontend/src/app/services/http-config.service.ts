import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class HttpConfigService {
  static readonly BASE_DIR = 'api/';
  constructor() {}

  baseDir(): string {
    return HttpConfigService.BASE_DIR;
  }
}
