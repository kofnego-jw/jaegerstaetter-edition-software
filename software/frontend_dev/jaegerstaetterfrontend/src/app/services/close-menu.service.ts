import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CloseMenuService {
  closeMenu$: Subject<string> = new Subject();

  constructor() {}

  closeMenu(): void {
    this.closeMenu$.next('close');
  }
}
