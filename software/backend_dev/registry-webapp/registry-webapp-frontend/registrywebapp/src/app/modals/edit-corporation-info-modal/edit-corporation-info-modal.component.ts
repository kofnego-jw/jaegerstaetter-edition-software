import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { CorporationInfo } from 'src/app/model';

@Component({
  selector: 'app-edit-corporation-info-modal',
  templateUrl: './edit-corporation-info-modal.component.html',
  styleUrls: ['./edit-corporation-info-modal.component.scss'],
})
export class EditCorporationInfoModalComponent implements OnInit {
  @Input()
  corporationInfo: CorporationInfo;

  corporationInfoForm: FormGroup;

  onSubmit: Subject<CorporationInfo> = new Subject();

  constructor(private modalRef: BsModalRef, private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.setUpForm();
  }

  setUpForm(): void {
    this.corporationInfoForm = this.formBuilder.group({
      key: [],
      organisation: [],
      preferredName: [],
      note: [],
      todo: [],
    });
    if (this.corporationInfo?.key) {
      this.corporationInfoForm.get('key').setValue(this.corporationInfo.key);
    }
    if (this.corporationInfo?.organisation) {
      this.corporationInfoForm
        .get('organisation')
        .setValue(this.corporationInfo.organisation);
    }
    if (this.corporationInfo?.preferredName) {
      this.corporationInfoForm
        .get('preferredName')
        .setValue(this.corporationInfo.preferredName);
    }
    if (this.corporationInfo?.note) {
      this.corporationInfoForm.get('note').setValue(this.corporationInfo.note);
    }
    if (this.corporationInfo?.todo) {
      this.corporationInfoForm.get('todo').setValue(this.corporationInfo.todo);
    }
  }

  submitForm(): void {
    if (this.corporationInfoForm) {
      const key = this.corporationInfoForm.get('key').value;
      const organisation = this.corporationInfoForm.get('organisation').value;
      const preferredName = this.corporationInfoForm.get('preferredName').value;
      const note = this.corporationInfoForm.get('note').value;
      const todo = this.corporationInfoForm.get('todo').value;
      const pi = new CorporationInfo(
        organisation,
        key,
        preferredName,
        note,
        todo,
        this.corporationInfo.controlledVocabularies,
        ''
      );
      this.onSubmit.next(pi);
    }
    this.onSubmit.complete();
    this.modalRef.hide();
  }

  cancel(): void {
    this.onSubmit.complete();
    this.modalRef.hide();
  }
}
