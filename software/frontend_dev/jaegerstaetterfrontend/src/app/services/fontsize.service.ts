import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FontsizeService {
  currentFontSize: FontSizeEnum;
  fontSize$: Subject<FontSizeEnum>;

  constructor() {
    this.fontSize$ = new Subject();
    this.changeFontSize(FontSizeEnum.REGULAR);
  }

  toggleFontSize(): void {
    if (this.currentFontSize === FontSizeEnum.REGULAR) {
      this.changeFontSize(FontSizeEnum.SMALL);
    } else {
      this.changeFontSize(FontSizeEnum.REGULAR);
    }
  }

  changeFontSize(size: FontSizeEnum): void {
    this.currentFontSize = size;
    this.fontSize$.next(this.currentFontSize);
  }
}

export enum FontSizeEnum {
  REGULAR = 'regular',
  SMALL = 'small',
}
