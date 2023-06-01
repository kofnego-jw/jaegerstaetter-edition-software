import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { RegistryEntryCorporation } from 'src/app/models/dto';

@Component({
  selector: 'app-corporation-info-modal',
  templateUrl: './corporation-info-modal.component.html',
  styleUrls: ['./corporation-info-modal.component.scss'],
})
export class CorporationInfoModalComponent implements OnInit {
  @Input()
  entry: RegistryEntryCorporation;

  constructor(private modalRef: BsModalRef, private router: Router) {}

  ngOnInit(): void {}

  gotoRegistry(): void {
    this.modalRef.hide();
    this.router.navigate(['/register/organisationen'], {
      queryParams: { key: this.entry.key },
    });
  }
}
