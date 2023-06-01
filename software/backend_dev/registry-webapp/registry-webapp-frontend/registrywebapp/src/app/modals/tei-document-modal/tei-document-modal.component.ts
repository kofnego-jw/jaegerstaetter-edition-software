import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { TeiDocumentDTO } from 'src/app/model';

@Component({
  selector: 'app-tei-document-modal',
  templateUrl: './tei-document-modal.component.html',
  styleUrls: ['./tei-document-modal.component.scss'],
})
export class TeiDocumentModalComponent implements OnInit {
  @Input()
  teiDocument: TeiDocumentDTO;

  @Input()
  isPreview: boolean;

  doPublish: Subject<boolean> = new Subject();

  constructor(private modalRef: BsModalRef) {}

  ngOnInit(): void {}

  close() {
    this.doPublish.next(false);
    this.modalRef.hide();
  }

  publish() {
    this.doPublish.next(true);
    this.modalRef.hide();
  }
}
