import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ViewOptionService } from 'src/app/services/view-option.service';

@Component({
  selector: 'app-sic-corr',
  templateUrl: './sic-corr.component.html',
  styleUrls: ['./sic-corr.component.scss'],
})
export class SicCorrComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean>;
  showCorr: boolean;

  constructor(private viewOption: ViewOptionService) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.viewOption.choiceOption$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((bool) => {
        this.showCorr = bool;
      });
  }

  toggleShowCorr(): void {
    this.showCorr = !this.showCorr;
  }
}
