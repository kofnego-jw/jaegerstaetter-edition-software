import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, UntypedFormGroup } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-admin-password-modal',
  templateUrl: './admin-password-modal.component.html',
  styleUrls: ['./admin-password-modal.component.scss'],
})
export class AdminPasswordModalComponent {
  passwordForm: FormGroup;

  onClose$: Subject<string> = new Subject();

  constructor(private modalRef: BsModalRef, fb: FormBuilder) {
    this.passwordForm = fb.group({
      password: [''],
    });
  }

  onSubmit(): void {
    const passwd = this.passwordForm.get('password').value;
    this.onClose$.next(passwd);
    this.onClose$.complete();
    this.modalRef.hide();
  }

  onCancel(): void {
    this.onClose$.complete();
    this.modalRef.hide();
  }
}
