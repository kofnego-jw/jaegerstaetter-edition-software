import { Injectable } from '@angular/core';
import { ReplaySubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ScrollIntoViewService {
  scrollIntoViewId: string;

  scrollIntoViewId$: ReplaySubject<string> = new ReplaySubject(1);

  highlightId$: Subject<string> = new Subject();

  constructor() {}

  view(id: string): void {
    this.scrollIntoViewId = id;
    this.scrollIntoViewId$.next(this.scrollIntoViewId);
  }

  highlight(id: string): void {
    if (id?.startsWith('head')) {
      this.highlightId$.next(id);
    }
  }
}
