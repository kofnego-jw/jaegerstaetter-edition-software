import {
  Component,
  ElementRef,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';
import { ViewPageService } from 'src/app/services/view-page.service';

@Component({
  selector: 'app-pb',
  templateUrl: './pb.component.html',
  styleUrls: ['./pb.component.scss'],
})
export class PbComponent implements OnInit, OnDestroy {
  @ViewChild('pageBreak')
  pageBreak: ElementRef;

  @Input('facs')
  facs: string;

  @Input('n')
  n: string;

  @Input('mode')
  mode: string;

  _pageNumber: number;

  destroyed$: Subject<boolean>;

  observer: IntersectionObserver;

  myId: string;

  show: boolean = true;

  constructor(
    private viewPage: ViewPageService,
    private annotationView: AnnotationViewService
  ) {
    this.destroyed$ = new Subject();
    this.setAnnotationView(annotationView.currentAnnotationView);
  }

  ngOnDestroy(): void {
    this.observer = null;
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.myId = this.viewPage.getNextId();
    this.observer = new IntersectionObserver(
      (entries) => {
        if (entries && entries.length && entries[0].isIntersecting) {
          this.viewThisPage(false);
        }
      },
      { threshold: 1 }
    );
    this.viewPage.viewPage$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((pbEvent) => {
        if (
          pbEvent &&
          pbEvent.source !== this.myId &&
          pbEvent.facs === this.facs &&
          pbEvent.byClick &&
          this.pageBreak
        ) {
          this.pageBreak.nativeElement.scrollIntoView({
            behavior: 'smooth',
            block: 'center',
          });
        }
      });
    if (this.n) {
      this._pageNumber = Number.parseInt(this.n);
    }
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
  }

  ngAfterViewInit(): void {
    if (this.pageBreak) {
      this.observer.observe(this.pageBreak.nativeElement);
    }
  }

  viewThisPage(byClick: boolean): void {
    this.viewPage.viewPage(this._pageNumber, byClick, this.facs, this.myId);
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    if (this.mode === 'norm') {
      this.show = !view || view === AnnotationViewEnum.ALL;
    } else {
      this.show = true;
    }
  }
}
