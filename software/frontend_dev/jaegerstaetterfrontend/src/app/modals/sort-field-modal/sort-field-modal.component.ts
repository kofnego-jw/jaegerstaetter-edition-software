import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { LetterSorterField } from 'src/app/models/frontend';

@Component({
  selector: 'app-sort-field-modal',
  templateUrl: './sort-field-modal.component.html',
  styleUrls: ['./sort-field-modal.component.scss'],
})
export class SortFieldModalComponent implements OnInit {
  sortFieldList: LetterSorterField[] = [
    LetterSorterField.NONE,
    LetterSorterField.SENT_DATE,
    LetterSorterField.SENDER,
    LetterSorterField.RECEIVER,
    LetterSorterField.IDNO,
  ];

  @Input()
  currentSortField: LetterSorterField;

  selectedSortField$: Subject<LetterSorterField> = new Subject();

  constructor(public modalRef: BsModalRef) {}

  ngOnInit(): void {}

  onClose(sortField: LetterSorterField): void {
    this.modalRef.hide();
    this.selectedSortField$.next(sortField);
  }
}
