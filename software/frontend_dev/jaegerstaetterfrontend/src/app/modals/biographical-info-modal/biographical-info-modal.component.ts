import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { take } from 'rxjs/operators';
import { RegistryEntryPerson } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-biographical-info-modal',
  templateUrl: './biographical-info-modal.component.html',
  styleUrls: ['./biographical-info-modal.component.scss'],
})
export class BiographicalInfoModalComponent implements OnInit {
  @Input()
  entries: RegistryEntryPerson[] = [];

  constructor(public modalRef: BsModalRef, private router: Router) {}

  ngOnInit(): void {}

  gotoRegistry(entry: RegistryEntryPerson): void {
    this.modalRef.hide();
    this.router.navigate(['/register/personen'], {
      queryParams: { key: entry.key },
    });
  }
}
