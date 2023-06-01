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
import { EditPersonInfoModalComponent } from 'src/app/modals/edit-person-info-modal/edit-person-info-modal.component';
import {
  ControlledVocabulary,
  PersonInfo,
  VocabularyType,
} from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-person-info-list',
  templateUrl: './person-info-list.component.html',
  styleUrls: ['./person-info-list.component.scss'],
})
export class PersonInfoListComponent implements OnInit, OnDestroy, OnChanges {
  faPencilAlt = faPencilAlt;
  faMinus = faMinus;

  type: VocabularyType = VocabularyType.PERSON;

  @Input()
  personList: PersonInfo[];

  destroyed$: Subject<any>;

  personListToShow: PersonInfo[] = [];

  filterAndSortSetting: FilterAndSortSetting;

  constructor(private app: AppService, private modalService: BsModalService) {
    this.destroyed$ = new Subject();
  }
  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.filterPersonInfoList(this.filterAndSortSetting);
  }

  ngOnInit(): void {
    this.app.filterAndSort$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((fs) => this.filterPersonInfoList(fs));
  }

  filterPersonInfoList(fs: FilterAndSortSetting) {
    this.filterAndSortSetting = fs;
    if (fs) {
      this.personListToShow = this.personList
        .filter((pi) =>
          fs.keyFilter
            ? pi.key.toLowerCase().indexOf(fs.keyFilter.toLowerCase()) >= 0
            : true
        )
        .filter((pi) =>
          fs.stringFilter
            ? pi.stringRepresentation
                .toLowerCase()
                .indexOf(fs.stringFilter.toLowerCase()) >= 0
            : true
        )
        .sort(fs.sortPerson());
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

  editPersonInfo(pi: PersonInfo): void {
    this.modalService
      .show(EditPersonInfoModalComponent, {
        class: 'modal-xl',
        initialState: {
          personInfo: pi,
        },
      })
      .content.onSubmit.subscribe({
        next: (newPersonInfo) => {
          this.app.personUpdate(newPersonInfo);
        },
      });
  }

  onControlledVocabularyListChange(
    personInfo: PersonInfo,
    list: ControlledVocabulary[]
  ): void {
    personInfo.controlledVocabularies = list;
    this.app.personUpdate(personInfo);
  }

  onPreferredNameChange(pi: PersonInfo, name: string): void {
    pi.preferredName = name;
    this.app.personUpdate(pi);
  }

  deletePersonInfo(pi: PersonInfo): void {
    this.modalService
      .show(ConfirmationModalComponent, {
        initialState: {
          title: 'Eintrag löschen',
          message:
            'Wollen Sie wirklich diesen Eintrag vom Personenverzeichnis löschen?',
        },
      })
      .content.onSubmit.subscribe({
        next: (bool) => {
          if (bool) {
            this.app.personDelete(pi);
          }
        },
      });
  }

  addNewPersonInfo(): void {
    this.editPersonInfo(
      new PersonInfo(
        '',
        '',
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
        '',
        [],
        '',
        ''
      )
    );
  }
}
