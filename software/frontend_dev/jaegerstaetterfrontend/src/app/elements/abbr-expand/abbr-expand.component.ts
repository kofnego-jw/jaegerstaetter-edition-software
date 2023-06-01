import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ViewOptionService } from 'src/app/services/view-option.service';

@Component({
  selector: 'app-abbr-expand',
  templateUrl: './abbr-expand.component.html',
  styleUrls: ['./abbr-expand.component.scss'],
})
export class AbbrExpandComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean>;
  showExpand: boolean;

  @Input()
  abbr: string;

  @Input()
  expand: string;

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
        this.showExpand = bool;
      });
  }

  toggleShowExpand(): void {
    this.showExpand = !this.showExpand;
  }
}
