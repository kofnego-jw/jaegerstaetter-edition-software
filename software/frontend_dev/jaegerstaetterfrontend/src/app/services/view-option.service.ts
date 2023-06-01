import { Injectable } from '@angular/core';
import { ReplaySubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ViewOptionService {
  choiceOption: boolean;
  choiceOption$: ReplaySubject<boolean>;

  breakOption: boolean;
  breakOption$: ReplaySubject<boolean>;

  constructor() {
    this.choiceOption$ = new ReplaySubject(1);
    this.breakOption$ = new ReplaySubject(1);
    this.setChoiceOption(true);
    this.setBreakOption(true);
  }

  setChoiceOption(bool: boolean): void {
    this.choiceOption = bool;
    this.choiceOption$.next(this.choiceOption);
  }

  toggleChoiceOption(): void {
    this.setChoiceOption(!this.choiceOption);
  }

  setBreakOption(bool: boolean): void {
    this.breakOption = bool;
    this.breakOption$.next(this.breakOption);
  }

  toggleBreakOption(): void {
    this.setBreakOption(!this.breakOption);
  }
}
