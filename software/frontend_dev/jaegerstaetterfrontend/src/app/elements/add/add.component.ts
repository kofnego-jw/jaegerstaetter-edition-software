import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ColoringService } from 'src/app/services/coloring.service';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss'],
})
export class AddComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  @Input()
  hand: string;

  textColor: string = 'inherit';

  constructor(private coloring: ColoringService) {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.coloring.coloringInfo$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((info) => {
        if (this.hand && info.coloringInfos.length) {
          for (let ci of info.coloringInfos) {
            if (ci.hand === this.hand) {
              this.textColor = ci.color;
              break;
            }
          }
        }
      });
  }
}
