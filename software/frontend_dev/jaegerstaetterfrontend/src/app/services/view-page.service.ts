import { Injectable } from '@angular/core';
import { merge, Observable, Subject } from 'rxjs';
import { debounceTime, tap } from 'rxjs/operators';
import { ViewPageBreakEvent } from '../models/frontend';

@Injectable({
  providedIn: 'root',
})
export class ViewPageService {
  private viewPageOnPageChange: Subject<ViewPageBreakEvent> = new Subject();
  private viewPageOnClick: Subject<ViewPageBreakEvent> = new Subject();
  viewPage$: Observable<ViewPageBreakEvent> = merge(
    this.viewPageOnPageChange.pipe(debounceTime(1000)),
    this.viewPageOnClick
  );
  counter: number = 0;

  constructor() {}

  getNextId(): string {
    this.counter += 1;
    if (this.counter > 1000000) {
      this.counter = 1;
    }
    return 'id_' + this.counter;
  }

  viewPage(
    pageNumber: number,
    byClick: boolean,
    facs: string,
    source: string
  ): void {
    const event = { pageNumber, byClick, facs, source };
    this.viewPageOnPageChange.next(event);
    if (byClick) {
      this.viewPageOnClick.next(event);
    }
  }
}
