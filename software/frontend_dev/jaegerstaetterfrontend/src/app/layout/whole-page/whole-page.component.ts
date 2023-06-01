import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  FontSizeEnum,
  FontsizeService,
} from 'src/app/services/fontsize.service';

@Component({
  selector: 'app-whole-page',
  templateUrl: './whole-page.component.html',
  styleUrls: ['./whole-page.component.scss'],
})
export class WholePageComponent implements OnInit, OnDestroy {
  id: string;

  destroyed$: Subject<boolean>;

  floating: boolean = false;

  constructor(
    private fontsizeService: FontsizeService,
    private activatedRoute: ActivatedRoute
  ) {
    this.destroyed$ = new Subject();
  }

  ngOnInit(): void {
    this.id =
      this.fontsizeService.currentFontSize === FontSizeEnum.SMALL
        ? 'small'
        : 'regular';
    this.activatedRoute.queryParamMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((param) => {
        if (param?.get('floating') === 'true') {
          this.floating = true;
        }
      });
    this.fontsizeService.fontSize$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((size) => {
        if (size === FontSizeEnum.SMALL) {
          this.id = 'small';
        } else {
          this.id = 'regular';
        }
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }
}
