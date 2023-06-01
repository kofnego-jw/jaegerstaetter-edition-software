import {
  Component,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { faMinus, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Subject, takeUntil } from 'rxjs';
import { FilterAndSortSetting, SortableField } from 'src/app/frontend-model';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { EditCorporationInfoModalComponent } from 'src/app/modals/edit-corporation-info-modal/edit-corporation-info-modal.component';
import {
  ControlledVocabulary,
  CorporationInfo,
  VocabularyType,
} from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-corporation-list',
  templateUrl: './corporation-list.component.html',
  styleUrls: ['./corporation-list.component.scss'],
})
export class CorporationListComponent implements OnInit, OnDestroy, OnChanges {
  @Input()
  corporationList: CorporationInfo[];

  faPencilAlt = faPencilAlt;
  faMinus = faMinus;

  type: VocabularyType = VocabularyType.CORPORATION;

  destroyed$: Subject<any>;

  corporationListToShow: CorporationInfo[] = [];

  filterAndSortSetting: FilterAndSortSetting;

  constructor(private app: AppService, private modalService: BsModalService) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.filterCorporationInfoList(this.filterAndSortSetting);
  }

  ngOnInit(): void {
    this.app.filterAndSort$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((fs) => this.filterCorporationInfoList(fs));
  }

  editCorporationInfo(ci: CorporationInfo): void {
    this.modalService
      .show(EditCorporationInfoModalComponent, {
        initialState: {
          corporationInfo: ci,
        },
      })
      .content.onSubmit.subscribe({
        next: (newCorpInfo) => {
          this.app.corporationUpdate(newCorpInfo);
        },
      });
  }

  onControlledVocabularyListChange(
    corporationInfo: CorporationInfo,
    list: ControlledVocabulary[]
  ): void {
    corporationInfo.controlledVocabularies = list;
    this.app.corporationUpdate(corporationInfo);
  }

  onPreferredNameChange(ci: CorporationInfo, name: string): void {
    ci.preferredName = name;
    this.app.corporationUpdate(ci);
  }

  deleteCorporationInfo(ci: CorporationInfo): void {
    this.modalService
      .show(ConfirmationModalComponent, {
        initialState: {
          title: 'Organisation löschen',
          message:
            'Wollen Sie wirklich diese Organisation vom Register löschen?',
        },
      })
      .content.onSubmit.subscribe({
        next: (bool) => {
          if (bool) {
            this.app.corporationDelete(ci);
          }
        },
      });
  }

  addNewCorporationInfo(): void {
    this.editCorporationInfo(new CorporationInfo('', '', '', '', '', [], ''));
  }

  filterCorporationInfoList(fs: FilterAndSortSetting) {
    this.filterAndSortSetting = fs;
    if (fs) {
      this.corporationListToShow = this.corporationList
        .filter((ci) =>
          fs.keyFilter
            ? ci.key.toLowerCase().indexOf(fs.keyFilter.toLowerCase()) >= 0
            : true
        )
        .filter((ci) =>
          fs.stringFilter
            ? ci.organisation
                .toLowerCase()
                .indexOf(fs.stringFilter.toLowerCase()) >= 0
            : true
        )
        .sort(fs.sortCorporation());
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
}
