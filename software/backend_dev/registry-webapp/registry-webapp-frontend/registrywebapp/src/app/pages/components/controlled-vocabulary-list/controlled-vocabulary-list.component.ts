import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { ControlledVocabulary, VocabularyType } from 'src/app/model';
import { faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { BsModalService } from 'ngx-bootstrap/modal';
import { VocabularySearchModalComponent } from 'src/app/modals/vocabulary-search-modal/vocabulary-search-modal.component';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-controlled-vocabulary-list',
  templateUrl: './controlled-vocabulary-list.component.html',
  styleUrls: ['./controlled-vocabulary-list.component.scss'],
})
export class ControlledVocabularyListComponent implements OnInit, OnDestroy {
  destroyed$: Subject<any> = new Subject();

  @Input()
  controlledVocabularyList: ControlledVocabulary[];

  @Input()
  queryString: string;

  @Input()
  key: string;

  @Input()
  type: VocabularyType;

  @Output()
  change: EventEmitter<ControlledVocabulary[]> = new EventEmitter();

  @Output()
  preferredNameChange: EventEmitter<string> = new EventEmitter();

  faPencilAlt = faPencilAlt;

  constructor(private modalService: BsModalService) {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }
  ngOnInit(): void {}

  emitChange(): void {
    this.change.emit(this.controlledVocabularyList);
  }

  deleteControlledVocabulary(voc: ControlledVocabulary): void {
    const index = this.controlledVocabularyList.indexOf(voc);
    this.controlledVocabularyList.splice(index, 1);
    this.emitChange();
  }

  editControlledVocabularies(): void {
    const modalRef = this.modalService.show(VocabularySearchModalComponent, {
      class: 'modal-xl',
      initialState: {
        queryString: this.queryString,
        type: this.type,
        selectedVocabularies: this.controlledVocabularyList,
        key: this.key,
      },
    });
    modalRef.content.changePreferredName
      .pipe(takeUntil(this.destroyed$))
      .subscribe({
        next: (name) => this.preferredNameChange.emit(name),
      });
    modalRef.content.selected.pipe(takeUntil(this.destroyed$)).subscribe({
      next: (result) => {
        this.controlledVocabularyList = result;
        this.emitChange();
      },
    });
  }
}
