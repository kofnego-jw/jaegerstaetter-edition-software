import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';

@Component({
  selector: 'app-corr',
  templateUrl: './corr.component.html',
  styleUrls: ['./corr.component.scss'],
})
export class CorrComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();
  @Input()
  inchoice: string;
  red: boolean = true;

  constructor(private annotationView: AnnotationViewService) {
    this.setAnnotationView(annotationView.currentAnnotationView);
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    if (!view) {
      this.red = true;
    } else {
      switch (view) {
        case AnnotationViewEnum.ALL:
          this.red = true;
          break;
        case AnnotationViewEnum.COMMENTS:
        case AnnotationViewEnum.NONE:
        default:
          this.red = false;
      }
    }
  }
}
