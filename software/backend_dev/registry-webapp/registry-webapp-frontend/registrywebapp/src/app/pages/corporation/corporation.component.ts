import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { CorporationInfo } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-corporation',
  templateUrl: './corporation.component.html',
  styleUrls: ['./corporation.component.scss'],
})
export class CorporationComponent implements OnInit, OnDestroy {
  destroyed$: Subject<any>;
  corporationList: CorporationInfo[];

  constructor(private app: AppService) {
    this.destroyed$ = new Subject();
    this.corporationList = [];
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.app.corporationList$.pipe(takeUntil(this.destroyed$)).subscribe({
      next: (dtos) => (this.corporationList = dtos),
    });
    this.app.corporationFindAll();
  }
}
