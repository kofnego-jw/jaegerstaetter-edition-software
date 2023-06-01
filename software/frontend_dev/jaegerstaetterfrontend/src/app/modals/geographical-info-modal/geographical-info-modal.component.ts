import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { RegistryEntryPlace } from 'src/app/models/dto';

@Component({
  selector: 'app-geographical-info-modal',
  templateUrl: './geographical-info-modal.component.html',
  styleUrls: ['./geographical-info-modal.component.scss'],
})
export class GeographicalInfoModalComponent implements OnInit {
  @Input()
  entry: RegistryEntryPlace;

  constructor(public modalRef: BsModalRef, private router: Router) {}

  ngOnInit(): void {}

  gotoRegistry(): void {
    this.modalRef.hide();
    this.router.navigate(['/register/orte'], {
      queryParams: { key: this.entry.key },
    });
  }
}
