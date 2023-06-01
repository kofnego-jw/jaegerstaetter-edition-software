import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { SaintInfo, Sex } from 'src/app/model';

@Component({
  selector: 'app-edit-saint-info-modal',
  templateUrl: './edit-saint-info-modal.component.html',
  styleUrls: ['./edit-saint-info-modal.component.scss'],
})
export class EditSaintInfoModalComponent implements OnInit {
  @Input()
  saintInfo: SaintInfo;

  saintInfoForm: FormGroup;

  onSubmit: Subject<SaintInfo> = new Subject();

  constructor(private modalRef: BsModalRef, private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.setUpForm();
  }

  setUpForm(): void {
    this.saintInfoForm = this.formBuilder.group({
      key: [],
      title: [],
      preferredName: [],
      encyclopediaLink: [],
      surname: [],
      forename: [],
      addNames: [],
      rolenames: [],
      sex: [],
      birthDate: [],
      birthPlace: [],
      residences: [],
      deathDate: [],
      deathPlace: [],
      note: [],
      todo: [],
    });
    if (this.saintInfo?.key) {
      this.saintInfoForm.get('key').setValue(this.saintInfo.key);
    }
    if (this.saintInfo?.title) {
      this.saintInfoForm.get('title').setValue(this.saintInfo.title);
    }
    if (this.saintInfo.preferredName) {
      this.saintInfoForm
        .get('preferredName')
        .setValue(this.saintInfo.preferredName);
    }
    if (this.saintInfo?.encyclopediaLink) {
      this.saintInfoForm
        .get('encyclopediaLink')
        .setValue(this.saintInfo.encyclopediaLink);
    }
    if (this.saintInfo?.surname) {
      this.saintInfoForm.get('surname').setValue(this.saintInfo.surname);
    }
    if (this.saintInfo?.forename) {
      this.saintInfoForm.get('forename').setValue(this.saintInfo.forename);
    }
    if (this.saintInfo?.addNames?.length) {
      const combined = this.saintInfo.addNames.join(', ');
      this.saintInfoForm.get('addNames').setValue(combined);
    }
    if (this.saintInfo?.rolenames?.length) {
      const combined = this.saintInfo.rolenames.join(', ');
      this.saintInfoForm.get('rolenames').setValue(combined);
    }
    if (this.saintInfo?.sex) {
      const sex = this.saintInfo.sex == Sex.FEMALE ? 'f' : 'm';
      this.saintInfoForm.get('sex').setValue(sex);
    }
    if (this.saintInfo?.birthDate) {
      this.saintInfoForm.get('birthDate').setValue(this.saintInfo.birthDate);
    }
    if (this.saintInfo?.birthPlace) {
      this.saintInfoForm.get('birthPlace').setValue(this.saintInfo.birthPlace);
    }
    if (this.saintInfo?.residences?.length) {
      const combined = this.saintInfo.residences.join(', ');
      this.saintInfoForm.get('residences').setValue(combined);
    }
    if (this.saintInfo?.deathDate) {
      this.saintInfoForm.get('deathDate').setValue(this.saintInfo.deathDate);
    }
    if (this.saintInfo?.deathPlace) {
      this.saintInfoForm.get('deathPlace').setValue(this.saintInfo.deathPlace);
    }
    if (this.saintInfo?.note) {
      this.saintInfoForm.get('note').setValue(this.saintInfo.note);
    }
    if (this.saintInfo?.todo) {
      this.saintInfoForm.get('todo').setValue(this.saintInfo.todo);
    }
  }

  submitForm(): void {
    if (this.saintInfoForm) {
      const key = this.saintInfoForm.get('key').value;
      const title = this.saintInfoForm.get('title').value;
      const preferredName = this.saintInfoForm.get('preferredName').value;
      const encyclopediaLink = this.saintInfoForm.get('encyclopediaLink').value;
      const surname = this.saintInfoForm.get('surname').value;
      const forename = this.saintInfoForm.get('forename').value;
      const sexValue = this.saintInfoForm.get('sex').value as string;
      const sex = !sexValue?.length
        ? null
        : sexValue.toLowerCase().startsWith('f')
        ? Sex.FEMALE
        : Sex.MALE;
      const birthDate = this.saintInfoForm.get('birthDate').value;
      const birthPlace = this.saintInfoForm.get('birthPlace').value;
      const deathDate = this.saintInfoForm.get('deathDate').value;
      const deathPlace = this.saintInfoForm.get('deathPlace').value;
      const note = this.saintInfoForm.get('note').value;
      const todo = this.saintInfoForm.get('todo').value;
      const addnamesValue = this.saintInfoForm.get('addNames').value as string;
      const addNames = !addnamesValue?.length
        ? []
        : addnamesValue.split('\\s*,\\s*');
      const rolenamesValue = this.saintInfoForm.get('rolenames')
        .value as string;
      const roleNames = !rolenamesValue?.length
        ? []
        : rolenamesValue.split('\\s*,\\s*');
      const residencesValue = this.saintInfoForm.get('residences')
        .value as string;
      const residences = !residencesValue?.length
        ? []
        : residencesValue.split('\\s*,\\s*');
      const pi = new SaintInfo(
        key,
        title,
        preferredName,
        encyclopediaLink,
        this.saintInfo.controlledVocabularies,
        surname,
        forename,
        addNames,
        roleNames,
        birthDate,
        birthPlace,
        residences,
        deathDate,
        deathPlace,
        sex,
        note,
        todo,
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
