import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import {
  FontSizeEnum,
  FontsizeService,
} from 'src/app/services/fontsize.service';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-font-size-switcher',
  templateUrl: './font-size-switcher.component.html',
  styleUrls: ['./font-size-switcher.component.scss'],
})
export class FontSizeSwitcherComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean>;
  currentFontSize: FontSizeEnum;

  constructor(private fontsizeService: FontsizeService) {
    this.destroyed$ = new Subject();
  }

  ngOnInit(): void {
    this.currentFontSize = this.fontsizeService.currentFontSize;
    this.fontsizeService.fontSize$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((x) => (this.currentFontSize = x));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  toggleFontSize(): void {
    this.fontsizeService.toggleFontSize();
  }
}
