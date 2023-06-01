import { Component, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.scss'],
})
export class ConfirmationModalComponent {
  @Input()
  title: string;

  @Input()
  message: string;

  onSubmit: Subject<boolean>;

  constructor(private modalRef: BsModalRef) {
    this.onSubmit = new Subject();
  }

  confirm(): void {
    this.onSubmit.next(true);
    this.onSubmit.complete();
    this.modalRef.hide();
  }

  decline(): void {
    this.onSubmit.next(false);
    this.onSubmit.complete();
    this.modalRef.hide();
  }
}
