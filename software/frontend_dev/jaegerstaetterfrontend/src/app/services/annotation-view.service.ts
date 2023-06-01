import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AnnotationViewService {
  currentAnnotationView: AnnotationViewEnum;

  annotationView$: ReplaySubject<AnnotationViewEnum>;

  constructor() {
    this.annotationView$ = new ReplaySubject(1);
    this.setAnnotationView(AnnotationViewEnum.COMMENTS);
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    const newView = !view ? AnnotationViewEnum.COMMENTS : view;
    if (this.currentAnnotationView !== newView) {
      this.currentAnnotationView = newView;
      this.annotationView$.next(this.currentAnnotationView);
    }
  }

  toggleAnnotationView(): void {
    let nextAnnotationView: AnnotationViewEnum;
    switch (this.currentAnnotationView) {
      case AnnotationViewEnum.ALL:
        nextAnnotationView = AnnotationViewEnum.COMMENTS;
        break;
      case AnnotationViewEnum.COMMENTS:
        nextAnnotationView = AnnotationViewEnum.NONE;
        break;
      default:
      case AnnotationViewEnum.NONE:
        nextAnnotationView = AnnotationViewEnum.ALL;
    }
    this.setAnnotationView(nextAnnotationView);
  }
}

export enum AnnotationViewEnum {
  ALL = 'ALL',
  COMMENTS = 'COMMENTS',
  NONE = 'NONE',
}
