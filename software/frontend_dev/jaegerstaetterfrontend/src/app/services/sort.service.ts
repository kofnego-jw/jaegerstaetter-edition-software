import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { LetterSorter, LetterSorterField } from '../models/frontend';

@Injectable({
  providedIn: 'root',
})
export class SortService {
  sortField: LetterSorterField;
  ascending: boolean;

  letterSorter$: ReplaySubject<LetterSorter>;

  constructor() {
    this.letterSorter$ = new ReplaySubject(1);
    this.ascending = true;
    this.setSortField(LetterSorterField.IDNO);
  }

  createSorter(): void {
    const sorter: LetterSorter = new LetterSorter(
      this.sortField,
      this.ascending
    );
    this.letterSorter$.next(sorter);
  }

  setSortField(field: LetterSorterField): void {
    this.ascending = true;
    if (!field) {
      this.sortField = LetterSorterField.NONE;
    } else {
      this.sortField = field;
    }
    this.createSorter();
  }

  setAscending(asc: boolean): void {
    this.ascending = asc;
    this.createSorter();
  }

  toggleAscending(): void {
    this.setAscending(!this.ascending);
  }
}
