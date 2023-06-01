import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { PersonInfo, Sex } from 'src/app/model';

@Component({
  selector: 'app-edit-person-info-modal',
  templateUrl: './edit-person-info-modal.component.html',
  styleUrls: ['./edit-person-info-modal.component.scss'],
})
export class EditPersonInfoModalComponent implements OnInit {
  @Input()
  personInfo: PersonInfo;

  personInfoForm: FormGroup;

  onSubmit: Subject<PersonInfo> = new Subject();

  constructor(private modalRef: BsModalRef, private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.setUpForm();
  }

  setUpForm(): void {
    this.personInfoForm = this.formBuilder.group({
      key: [],
      preferredName: [],
      surname: [],
      forename: [],
      addNames: [],
      roleNames: [],
      birthDate: [],
      birthPlace: [],
      residences: [],
      deathDate: [],
      deathPlace: [],
      sex: [],
      nationality: [],
      note: [],
      internalNotes: [],
    });
    if (this.personInfo?.key) {
      this.personInfoForm.get('key').setValue(this.personInfo.key);
    }
    if (this.personInfo?.preferredName) {
      this.personInfoForm
        .get('preferredName')
        .setValue(this.personInfo.preferredName);
    }
    if (this.personInfo?.surname) {
      this.personInfoForm.get('surname').setValue(this.personInfo.surname);
    }
    if (this.personInfo?.forename) {
      this.personInfoForm.get('forename').setValue(this.personInfo.forename);
    }
    if (this.personInfo?.addNames?.length) {
      const combined = this.personInfo.addNames.join(', ');
      this.personInfoForm.get('addNames').setValue(combined);
    }
    if (this.personInfo?.roleNames?.length) {
      const combined = this.personInfo.roleNames.join(', ');
      this.personInfoForm.get('roleNames').setValue(combined);
    }
    if (this.personInfo?.birthDate) {
      this.personInfoForm.get('birthDate').setValue(this.personInfo.birthDate);
    }
    if (this.personInfo?.birthPlace) {
      this.personInfoForm
        .get('birthPlace')
        .setValue(this.personInfo.birthPlace);
    }
    if (this.personInfo?.residences?.length) {
      const combined = this.personInfo.residences.join(', ');
      this.personInfoForm.get('residences').setValue(combined);
    }
    if (this.personInfo?.deathDate) {
      this.personInfoForm.get('deathDate').setValue(this.personInfo.deathDate);
    }
    if (this.personInfo?.deathPlace) {
      this.personInfoForm
        .get('deathPlace')
        .setValue(this.personInfo.deathPlace);
    }
    if (this.personInfo?.nationality) {
      this.personInfoForm
        .get('nationality')
        .setValue(this.personInfo.nationality);
    }
    if (this.personInfo?.sex) {
      const sex = this.personInfo.sex == Sex.FEMALE ? 'f' : 'm';
      this.personInfoForm.get('sex').setValue(sex);
    }
    if (this.personInfo?.note) {
      this.personInfoForm.get('note').setValue(this.personInfo.note);
    }
    if (this.personInfo?.internalNotes) {
      this.personInfoForm
        .get('internalNotes')
        .setValue(this.personInfo.internalNotes);
    }
  }

  submitForm(): void {
    if (this.personInfoForm) {
      const key = this.personInfoForm.get('key').value;
      const preferredName = this.personInfoForm.get('preferredName').value;
      const surname = this.personInfoForm.get('surname').value;
      const forename = this.personInfoForm.get('forename').value;
      const sexValue = this.personInfoForm.get('sex').value as string;
      const sex = !sexValue?.length
        ? null
        : sexValue.toLowerCase().startsWith('f')
        ? Sex.FEMALE
        : Sex.MALE;
      const addnamesValue = this.personInfoForm.get('addNames').value as string;
      const addNames = !addnamesValue?.length
        ? []
        : addnamesValue.split('\\s*,\\s*');
      const rolenamesValue = this.personInfoForm.get('roleNames')
        .value as string;
      const roleNames = !rolenamesValue?.length
        ? []
        : rolenamesValue.split('\\s*,\\s*');
      const birthDate = this.personInfoForm.get('birthDate').value;
      const birthPlace = this.personInfoForm.get('birthPlace').value;
      const residencesValue = this.personInfoForm.get('residences')
        .value as string;
      const residences = !residencesValue?.length
        ? []
        : residencesValue.split('\\s*,\\s*');
      const deathDate = this.personInfoForm.get('deathDate').value;
      const deathPlace = this.personInfoForm.get('deathPlace').value;
      const nationality = this.personInfoForm.get('nationality').value;

      const note = this.personInfoForm.get('note').value;
      const internalNotes = this.personInfoForm.get('internalNotes').value;
      const pi = new PersonInfo(
        key,
        preferredName,
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
        nationality,
        note,
        internalNotes,
        this.personInfo.controlledVocabularies,
        '',
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
