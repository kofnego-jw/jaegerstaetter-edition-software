import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { CorrespInfo, ResourceDTO } from 'src/app/models/dto';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';
import { ApplicationService } from 'src/app/services/application.service';
import { ModalService } from 'src/app/services/modal.service';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-anchor',
  templateUrl: './anchor.component.html',
  styleUrls: ['./anchor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AnchorComponent implements OnInit, OnDestroy {
  @Input()
  id: string;

  @Input()
  corresp: string;

  @ViewChild('myAnchor')
  myAnchor: ElementRef;

  destroyed$: Subject<boolean> = new Subject();

  correspInfo: CorrespInfo = new CorrespInfo('', []);

  show: boolean = true;

  highlight: boolean = false;

  constructor(
    private scrollIntoView: ScrollIntoViewService,
    private app: ApplicationService,
    private modal: ModalService,
    private annotationView: AnnotationViewService,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.scrollIntoView.scrollIntoViewId$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((id) => {
        if (id && id === this.id) {
          this.viewMe();
        } else {
          this.highlight = false;
          this.changeDetector.detectChanges();
        }
      });
    if (this.id && !this.id.startsWith('ID') && !this.corresp) {
      this.app.currentResource$
        .pipe(takeUntil(this.destroyed$))
        .subscribe((res) => this.setCurrentResource(res));
    } else if (this.corresp) {
      this.loadCorrespInfo(this.corresp);
    }
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    this.show =
      view === AnnotationViewEnum.ALL || view === AnnotationViewEnum.COMMENTS;
  }

  loadCorrespInfo(dest: string) {
    this.app
      .getCorrespInfo(dest)
      .pipe(take(1))
      .subscribe((dto) => {
        if (dto.correspId) {
          this.correspInfo = dto;
          this.changeDetector.detectChanges();
        }
      });
  }

  setCurrentResource(res: ResourceDTO) {
    let dest = res.id;
    if (dest.indexOf('/') >= 0) {
      dest = dest.substring(dest.lastIndexOf('/') + 1);
    }
    dest = dest + '#' + this.id;
    this.loadCorrespInfo(dest);
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  viewMe(): void {
    this.highlight = true;
    this.changeDetector.detectChanges();
    setTimeout(() => {
      this.myAnchor.nativeElement.scrollIntoView({
        block: 'start',
        behavior: 'smooth',
      });
    });
  }

  openCorrespModal(): void {
    if (this.correspInfo?.places.length) {
      this.modal.openCorrespInfo(this.correspInfo);
    }
  }
}
