import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ViewVersionService {
  date: string = '';

  constructor() {}

  setDate(date: string) {
    this.date = date;
  }

  reset(): void {
    this.date = '';
  }
}
