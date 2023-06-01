import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { SaintInfo } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-saint',
  templateUrl: './saint.component.html',
  styleUrls: ['./saint.component.scss'],
})
export class SaintComponent implements OnInit, OnDestroy {
  destroyed$: Subject<any>;
  saintList: SaintInfo[];

  constructor(private app: AppService) {
    this.destroyed$ = new Subject();
    this.saintList = [];
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.app.saintList$.pipe(takeUntil(this.destroyed$)).subscribe({
      next: (dtos) => (this.saintList = dtos),
    });
    this.app.saintFindAll();
  }
}
