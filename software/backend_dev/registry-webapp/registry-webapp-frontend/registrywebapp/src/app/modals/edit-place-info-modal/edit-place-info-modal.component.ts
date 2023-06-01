import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { GeoLocation, PlaceInfo } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-edit-place-info-modal',
  templateUrl: './edit-place-info-modal.component.html',
  styleUrls: ['./edit-place-info-modal.component.scss'],
})
export class EditPlaceInfoModalComponent implements OnInit {
  @Input()
  placeInfo: PlaceInfo;

  placeInfoForm: FormGroup;

  onSubmit: Subject<PlaceInfo> = new Subject();

  constructor(
    private app: AppService,
    private modalRef: BsModalRef,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.setUpForm();
  }

  setUpForm(): void {
    this.placeInfoForm = this.formBuilder.group({
      key: [],
      locationName: [],
      preferredName: [],
      region: [],
      longitude: [],
      latitude: [],
      note: [],
      todo: [],
    });
    if (this.placeInfo?.key) {
      this.placeInfoForm.get('key').setValue(this.placeInfo.key);
    }
    if (this.placeInfo?.preferredName) {
      this.placeInfoForm
        .get('preferredName')
        .setValue(this.placeInfo.preferredName);
    }
    if (this.placeInfo?.locationName) {
      this.placeInfoForm
        .get('locationName')
        .setValue(this.placeInfo.locationName);
    }
    if (this.placeInfo.geoLocation) {
      this.placeInfoForm
        .get('latitude')
        .setValue(this.placeInfo.geoLocation.latitude);
      this.placeInfoForm
        .get('longitude')
        .setValue(this.placeInfo.geoLocation.longitude);
    }
    if (this.placeInfo?.region) {
      this.placeInfoForm.get('region').setValue(this.placeInfo.region);
    }
    if (this.placeInfo?.note) {
      this.placeInfoForm.get('note').setValue(this.placeInfo.note);
    }
    if (this.placeInfo?.todo) {
      this.placeInfoForm.get('todo').setValue(this.placeInfo.todo);
    }
  }

  submitForm(): void {
    if (this.placeInfoForm) {
      const key = this.placeInfoForm.get('key').value;
      const locationName = this.placeInfoForm.get('locationName').value;
      const region = this.placeInfoForm.get('region').value;
      const note = this.placeInfoForm.get('note').value;
      const todo = this.placeInfoForm.get('todo').value;
      const preferredName = this.placeInfoForm.get('preferredName').value;
      const latitude = this.placeInfoForm.get('latitude').value
        ? this.placeInfoForm.get('latitude').value
        : this.placeInfo.geoLocation?.latitude;
      const longitude = this.placeInfoForm.get('longitude').value
        ? this.placeInfoForm.get('longitude').value
        : this.placeInfo.geoLocation?.longitude;
      const geoLocation = new GeoLocation(latitude, longitude);
      const pi = new PlaceInfo(
        locationName,
        region,
        key,
        preferredName,
        note,
        todo,
        this.placeInfo.controlledVocabularies,
        geoLocation,
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
