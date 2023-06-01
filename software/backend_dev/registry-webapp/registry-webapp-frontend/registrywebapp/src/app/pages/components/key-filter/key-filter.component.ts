import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-key-filter',
  templateUrl: './key-filter.component.html',
  styleUrls: ['./key-filter.component.scss'],
})
export class KeyFilterComponent implements OnInit, OnDestroy {
  keyFilter: string;
  destroyed$: Subject<any>;

  constructor(private app: AppService) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.app.filterAndSort$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((fs) => (this.keyFilter = fs.keyFilter));
  }

  setKeyFilter(): void {
    this.app.fsSetKeyFilter(this.keyFilter);
  }
}
