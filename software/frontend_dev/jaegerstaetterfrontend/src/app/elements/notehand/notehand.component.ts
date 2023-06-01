import {
  ChangeDetectorRef,
  Component,
  Input,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ColoringService } from 'src/app/services/coloring.service';

@Component({
  selector: 'app-notehand',
  templateUrl: './notehand.component.html',
  styleUrls: ['./notehand.component.scss'],
})
export class NotehandComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  initFinished: boolean = false;

  @Input()
  hand: string;

  textColor: string;

  constructor(
    private coloring: ColoringService,
    private changeDetector: ChangeDetectorRef
  ) {
    this.textColor = coloring.defaultColor.toString();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.coloring.coloringInfo$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((info) => {
        if (info.coloringInfos?.length) {
          for (let ci of info.coloringInfos) {
            if (ci.hand === this.hand) {
              this.textColor = ci.color;
              this.changeDetector.detectChanges();
              break;
            }
          }
        }
      });
    this.initFinished = true;
  }
}
