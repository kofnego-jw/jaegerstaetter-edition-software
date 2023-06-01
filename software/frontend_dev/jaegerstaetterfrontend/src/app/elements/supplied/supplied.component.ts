import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';

@Component({
  selector: 'app-supplied',
  templateUrl: './supplied.component.html',
  styleUrls: ['./supplied.component.scss'],
})
export class SuppliedComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();
  show: boolean = true;

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
    this.show = !view || view === AnnotationViewEnum.ALL;
  }
}