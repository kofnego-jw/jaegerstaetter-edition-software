import {
  Component,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { FilterAndSortSetting, SortableField } from 'src/app/frontend-model';
import { ControlledVocabulary, PlaceInfo, VocabularyType } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';
import { faPencilAlt, faMinus } from '@fortawesome/free-solid-svg-icons';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditPlaceInfoModalComponent } from 'src/app/modals/edit-place-info-modal/edit-place-info-modal.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { GeoLocationModalComponent } from 'src/app/modals/geo-location-modal/geo-location-modal.component';

@Component({
  selector: 'app-place-list',
  templateUrl: './place-list.component.html',
  styleUrls: ['./place-list.component.scss'],
})
export class PlaceListComponent implements OnInit, OnDestroy, OnChanges {
  faPencilAlt = faPencilAlt;
  faMinus = faMinus;

  @Input()
  placeList: PlaceInfo[];

  type: VocabularyType = VocabularyType.PLACE;

  destroyed$: Subject<any>;

  placeListToShow: PlaceInfo[] = [];

  filterAndSortSetting: FilterAndSortSetting;

  constructor(private app: AppService, private modalService: BsModalService) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.filterPlaceInfoList(this.filterAndSortSetting);
  }

  ngOnInit(): void {
    this.app.filterAndSort$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((fs) => this.filterPlaceInfoList(fs));
  }

  openPlaceInfoModal(pi: PlaceInfo): void {
    this.modalService
      .show(EditPlaceInfoModalComponent, {
        initialState: {
          placeInfo: pi,
        },
      })
      .content.onSubmit.subscribe({
        next: (newPlaceInfo) => {
          this.app.placeUpdate(newPlaceInfo);
        },
      });
  }

  filterPlaceInfoList(fs: FilterAndSortSetting) {
    this.filterAndSortSetting = fs;
    if (fs) {
      this.placeListToShow = this.placeList
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
        .sort(fs.sortPlace());
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

  onControlledVocabularyListChange(
    placeInfo: PlaceInfo,
    list: ControlledVocabulary[]
  ): void {
    placeInfo.controlledVocabularies = list;
    this.app.placeUpdate(placeInfo);
  }

  onPreferredNameChange(placeInfo: PlaceInfo, name: string): void {
    placeInfo.preferredName = name;
    this.app.placeUpdate(placeInfo);
  }

  deletePlaceInfo(pi: PlaceInfo): void {
    this.modalService
      .show(ConfirmationModalComponent, {
        initialState: {
          title: 'Ort löschen',
          message: `Wollen Sie wirklich diesen Ort '${pi.key}'vom Register löschen?`,
        },
      })
      .content.onSubmit.subscribe({
        next: (bool) => {
          if (bool) {
            this.app.placeDelete(pi);
          }
        },
      });
  }

  addNewPlaceInfo(): void {
    this.openPlaceInfoModal(
      new PlaceInfo('', '', '', '', '', '', [], null, '')
    );
  }
}
