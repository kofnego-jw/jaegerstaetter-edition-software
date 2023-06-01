import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  faAngleDoubleLeft,
  faAngleLeft,
  faAngleRight,
  faAngleDoubleRight,
  faSort,
} from '@fortawesome/free-solid-svg-icons';
import { SearchSortField } from 'src/app/models/dto';

@Component({
  selector: 'app-document-pagination',
  templateUrl: './document-pagination.component.html',
  styleUrls: ['./document-pagination.component.scss'],
})
export class DocumentPaginationComponent implements OnInit {
  faAngleDoubleLeft = faAngleDoubleLeft;
  faAngleLeft = faAngleLeft;
  faAngleRight = faAngleRight;
  faAngleDoubleRight = faAngleDoubleRight;
  faSort = faSort;

  @Input()
  totalCount: number;

  @Input()
  fromNumber: number;

  @Input()
  toNumber: number;

  @Input()
  pageNumber: number;

  @Input()
  pageSize: number;

  @Input()
  maxPageCount: number;

  @Input()
  sortableFields: SearchSortField[];

  @Input()
  sortByField: SearchSortField;

  @Input()
  sortAsc: boolean;

  @Output()
  selectPage: EventEmitter<number> = new EventEmitter();

  @Output()
  selectSort: EventEmitter<SearchSortField> = new EventEmitter();

  @Output()
  changeSortDir: EventEmitter<boolean> = new EventEmitter();

  constructor() {}

  ngOnInit(): void {}

  setPage(pn: number): void {
    if (this.pageNumber !== pn) {
      this.selectPage.emit(pn);
    }
  }

  fastPrev(): void {
    this.setPage(0);
  }

  prev(): void {
    const prevPage = this.pageNumber <= 0 ? 0 : this.pageNumber - 1;
    this.setPage(prevPage);
  }

  next(): void {
    const nextPage =
      this.maxPageCount - this.pageNumber <= 2
        ? this.maxPageCount - 1
        : this.pageNumber + 1;
    this.setPage(nextPage);
  }

  fastNext(): void {
    this.setPage(this.maxPageCount - 1);
  }

  setSortField(field: SearchSortField): void {
    this.selectSort.emit(field);
  }

  onPageChange(event: Event): void {
    const pn = Number.parseInt((event.target as HTMLInputElement).value);
    this.setPage(pn - 1);
  }

  toggleSort(): void {
    const sortDir = !this.sortAsc;
    this.changeSortDir.emit(sortDir);
  }
}
