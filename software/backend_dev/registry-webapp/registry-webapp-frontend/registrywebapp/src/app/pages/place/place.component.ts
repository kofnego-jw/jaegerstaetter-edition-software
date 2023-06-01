import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { PlaceInfo } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-place',
  templateUrl: './place.component.html',
  styleUrls: ['./place.component.scss'],
})
export class PlaceComponent implements OnInit, OnDestroy {
  destroyed$: Subject<any>;
  placeList: PlaceInfo[];

  constructor(private app: AppService) {
    this.destroyed$ = new Subject();
    this.placeList = [];
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.app.placeList$.pipe(takeUntil(this.destroyed$)).subscribe({
      next: (dtos) => (this.placeList = dtos),
    });
    this.app.placeFindAll();
  }
}
