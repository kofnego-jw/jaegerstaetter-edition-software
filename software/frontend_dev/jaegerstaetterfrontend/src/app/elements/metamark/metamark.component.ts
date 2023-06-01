import {
  Component,
  Input,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';

@Component({
  selector: 'app-metamark',
  templateUrl: './metamark.component.html',
  styleUrls: ['./metamark.component.scss'],
})
export class MetamarkComponent implements OnInit, OnDestroy {
  @Input()
  place: string;

  @Input()
  rend: string;

  @Input()
  resp: string;

  @Input()
  corresp: string;

  @Input()
  empty: string;

  @ViewChild('modal')
  modal: TemplateRef<any>;

  @Input()
  mode: string;

  destroyed$: Subject<any> = new Subject();

  show: boolean = true;

  constructor(
    private bsModalService: BsModalService,
    private annotation: AnnotationViewService
  ) {}

  ngOnInit(): void {
    this.annotation.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    this.show =
      this.mode !== 'norm' || !view || view === AnnotationViewEnum.ALL;
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  showInfo(): void {
    this.bsModalService.show(this.modal);
  }
}
