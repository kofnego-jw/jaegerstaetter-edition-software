import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';

@Component({
  selector: 'app-sic',
  templateUrl: './sic.component.html',
  styleUrls: ['./sic.component.scss'],
})
export class SicComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();
  show: boolean = true;
  @Input()
  inchoice: string;

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
      this.show = true;
    } else {
      switch (view) {
        case AnnotationViewEnum.ALL:
          this.show = true;
          break;
        default:
          this.show = false;
      }
    }
  }
}
