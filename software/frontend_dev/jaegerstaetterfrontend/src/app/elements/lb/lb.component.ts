import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { faArrowTurnDown } from '@fortawesome/free-solid-svg-icons';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';
import { ViewOptionService } from 'src/app/services/view-option.service';

@Component({
  selector: 'app-lb',
  templateUrl: './lb.component.html',
  styleUrls: ['./lb.component.scss'],
})
export class LbComponent implements OnInit, OnDestroy {
  showBreak: boolean;
  destroyed$: Subject<boolean>;
  faArrowTurnDown = faArrowTurnDown;

  @Input()
  mode: string;

  constructor(
    private viewOptionService: ViewOptionService,
    private annotationView: AnnotationViewService
  ) {
    this.destroyed$ = new Subject();
    this.showBreak = false;
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe(
        (view) =>
          (this.showBreak =
            view === AnnotationViewEnum.ALL && this.mode !== 'norm')
      );
  }

  toggleViewBreak(): void {
    this.viewOptionService.toggleBreakOption();
  }
}
