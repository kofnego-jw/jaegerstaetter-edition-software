import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-map-consent-modal',
  templateUrl: './map-consent-modal.component.html',
  styleUrls: ['./map-consent-modal.component.scss'],
})
export class MapConsentModalComponent implements OnInit {
  onMapConsentSelect$: Subject<string> = new Subject();

  constructor(private modalRef: BsModalRef) {}

  ngOnInit(): void {}

  close(): void {
    this.onMapConsentSelect$.complete();
    this.modalRef.hide();
  }

  select(selected: string): void {
    this.onMapConsentSelect$.next(selected);
    this.close();
  }

  selectYes(): void {
    this.select('true');
  }

  selectNo(): void {
    this.select('false');
  }
}
