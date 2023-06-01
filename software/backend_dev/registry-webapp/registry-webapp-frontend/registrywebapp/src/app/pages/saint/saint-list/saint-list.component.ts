import {
  Component,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { faMinus, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Subject, takeUntil } from 'rxjs';
import { FilterAndSortSetting, SortableField } from 'src/app/frontend-model';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { EditSaintInfoModalComponent } from 'src/app/modals/edit-saint-info-modal/edit-saint-info-modal.component';
import { ControlledVocabulary, SaintInfo, VocabularyType } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-saint-list',
  templateUrl: './saint-list.component.html',
  styleUrls: ['./saint-list.component.scss'],
})
export class SaintListComponent implements OnInit, OnDestroy, OnChanges {
  @Input()
  saintList: SaintInfo[];

  faPencilAlt = faPencilAlt;
  faMinus = faMinus;

  type: VocabularyType = VocabularyType.SAINT;

  destroyed$: Subject<any>;

  saintInfoListToShow: SaintInfo[] = [];

  filterAndSortSetting: FilterAndSortSetting;

  constructor(
    private app: AppService,
    private modalService: BsModalService,
    private sanitizer: DomSanitizer
  ) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.filterSaintInfoList(this.filterAndSortSetting);
  }

  ngOnInit(): void {
    this.app.filterAndSort$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((fs) => this.filterSaintInfoList(fs));
  }

  editSaintInfo(si: SaintInfo): void {
    this.modalService
      .show(EditSaintInfoModalComponent, {
        class: 'modal-xl',
        initialState: {
          saintInfo: si,
        },
      })
      .content.onSubmit.subscribe({
        next: (newSaintInfo) => {
          this.app.saintUpdate(newSaintInfo);
        },
      });
  }

  onControlledVocabularyListChange(
    saintInfo: SaintInfo,
    list: ControlledVocabulary[]
  ): void {
    saintInfo.controlledVocabularies = list;
    this.app.saintUpdate(saintInfo);
  }

  onPreferredNameChange(saintInfo: SaintInfo, name: string): void {
    saintInfo.preferredName = name;
    this.app.saintUpdate(saintInfo);
  }

  deleteSaintInfo(si: SaintInfo): void {
    this.modalService
      .show(ConfirmationModalComponent, {
        initialState: {
          title: 'Eintrag löschen',
          message:
            'Wollen Sie wirklich diesen Eintrag vom Heiligenverzeichnis löschen?',
        },
      })
      .content.onSubmit.subscribe({
        next: (bool) => {
          if (bool) {
            this.app.saintDelete(si);
          }
        },
      });
  }

  addNewSaintInfo(): void {
    this.editSaintInfo(
      new SaintInfo(
        '',
        '',
        '',
        '',
        [],
        '',
        '',
        [],
        [],
        '',
        '',
        [],
        '',
        '',
        '',
        '',
        '',
        ''
      )
    );
  }

  filterSaintInfoList(fs: FilterAndSortSetting) {
    this.filterAndSortSetting = fs;
    if (fs) {
      this.saintInfoListToShow = this.saintList
        .filter((ci) =>
          fs?.keyFilter
            ? ci.key.toLowerCase().indexOf(fs.keyFilter.toLowerCase()) >= 0
            : true
        )
        .filter((ci) =>
          fs?.stringFilter
            ? ci.stringRepresentation
                .toLowerCase()
                .indexOf(fs.stringFilter.toLowerCase()) >= 0
            : true
        )
        .sort(fs.sortSaint());
    }
  }

  setSortField(field: string): void {
    const sortField: SortableField = !field
      ? SortableField.NONE
      : field === 'name'
      ? SortableField.NAME
      : SortableField.KEY;
    this.app.fsSetSortField(sortField);
  }

  encyclopediaLink(si: SaintInfo): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(si.encyclopediaLink);
  }
}
