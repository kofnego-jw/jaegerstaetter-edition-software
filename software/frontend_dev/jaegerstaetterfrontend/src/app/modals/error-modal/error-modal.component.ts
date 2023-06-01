import { Component } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-error-modal',
  templateUrl: './error-modal.component.html',
  styleUrls: ['./error-modal.component.scss'],
})
export class ErrorModalComponent {
  message: string;

  constructor(private modalRef: BsModalRef) {}

  close(): void {
    this.modalRef.hide();
  }
}
