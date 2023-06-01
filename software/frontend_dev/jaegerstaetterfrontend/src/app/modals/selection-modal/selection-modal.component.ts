import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { AnnotationViewEnum } from 'src/app/services/annotation-view.service';

@Component({
  selector: 'app-selection-modal',
  templateUrl: './selection-modal.component.html',
  styleUrls: ['./selection-modal.component.scss'],
})
export class SelectionModalComponent implements OnInit {
  onAnnotationSelect$: Subject<AnnotationViewEnum> = new Subject();

  constructor(private modalRef: BsModalRef) {}

  ngOnInit(): void {}

  close(): void {
    this.onAnnotationSelect$.complete();
    this.modalRef.hide();
  }

  select(selected: AnnotationViewEnum): void {
    this.onAnnotationSelect$.next(selected);
    this.close();
  }

  selectAll(): void {
    this.select(AnnotationViewEnum.ALL);
  }

  selectSome(): void {
    this.select(AnnotationViewEnum.COMMENTS);
  }

  selectNone(): void {
    this.select(AnnotationViewEnum.NONE);
  }
}
