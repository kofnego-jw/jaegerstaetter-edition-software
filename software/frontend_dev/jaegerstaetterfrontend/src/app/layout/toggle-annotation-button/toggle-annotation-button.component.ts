import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { faPen } from '@fortawesome/free-solid-svg-icons';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { SelectionModalComponent } from 'src/app/modals/selection-modal/selection-modal.component';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';

@Component({
  selector: 'app-toggle-annotation-button',
  templateUrl: './toggle-annotation-button.component.html',
  styleUrls: ['./toggle-annotation-button.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ToggleAnnotationButtonComponent implements OnInit, OnDestroy {
  static readonly TOOLTIP_ALL = 'Alle Annotationen werden angezeigt';
  static readonly TOOLTIP_COMMENTS =
    'Nur Kommentare und Anmerkungen werden angezeigt';
  static readonly TOOLTIP_NONE = 'Keine Annotationen werden angezeigt';

  AnnotationViewEnum = AnnotationViewEnum;
  annotationLevel: AnnotationViewEnum;
  faPen = faPen;
  destroyed$: Subject<boolean> = new Subject();
  annotationTooltip: string;

  constructor(
    private annotationView: AnnotationViewService,
    private modalService: BsModalService,
    private changeDetector: ChangeDetectorRef
  ) {
    this.annotationLevel = this.annotationView.currentAnnotationView
      ? this.annotationView.currentAnnotationView
      : AnnotationViewEnum.ALL;
    this.annotationTooltip = ToggleAnnotationButtonComponent.TOOLTIP_COMMENTS;
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationLevel(view));
  }

  setAnnotationLevel(view: AnnotationViewEnum): void {
    this.annotationLevel = view;
    switch (this.annotationLevel) {
      case AnnotationViewEnum.ALL:
        this.annotationTooltip = ToggleAnnotationButtonComponent.TOOLTIP_ALL;
        break;
      case AnnotationViewEnum.COMMENTS:
        this.annotationTooltip =
          ToggleAnnotationButtonComponent.TOOLTIP_COMMENTS;
        break;
      default:
      case AnnotationViewEnum.NONE:
        this.annotationTooltip = ToggleAnnotationButtonComponent.TOOLTIP_NONE;
    }
    this.changeDetector.markForCheck();
  }

  toggleAnnotationView(): void {
    const modalContent = this.modalService.show(
      SelectionModalComponent
    ).content;
    modalContent.onAnnotationSelect$.pipe(take(1)).subscribe((result) => {
      this.annotationView.setAnnotationView(result);
    });
  }
}
