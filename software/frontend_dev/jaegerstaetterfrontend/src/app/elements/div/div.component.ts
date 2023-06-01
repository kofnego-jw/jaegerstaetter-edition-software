import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ColoringService } from 'src/app/services/coloring.service';
import { faSquareArrowUpRight } from '@fortawesome/free-solid-svg-icons';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-div',
  templateUrl: './div.component.html',
  styleUrls: ['./div.component.scss'],
})
export class DivComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('myBeginAnchor')
  myBeginAnchor: ElementRef;
  @ViewChild('myEndAnchor')
  myEndAnchor: ElementRef;

  initFinished: boolean = false;
  faSquareArrowUpRight = faSquareArrowUpRight;

  destroyed$: Subject<boolean> = new Subject();

  @Input()
  hand: string;

  @Input()
  type: string;

  @Input()
  n: string;

  @Input()
  corresp: string;

  @Input('mode')
  mode: string;

  @Input('highlightid')
  highlightid: string;

  handInfo: string = '';

  textColor: string;

  backgroundColor: string = 'inherit';

  beginObserver: IntersectionObserver;
  endObserver: IntersectionObserver;

  constructor(
    private coloring: ColoringService,
    private changeDetector: ChangeDetectorRef,
    private scrollIntoView: ScrollIntoViewService
  ) {
    this.textColor = coloring.defaultColor.toString();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.beginObserver = new IntersectionObserver(
      (entries) => {
        if (entries && entries.length && entries[0].isIntersecting) {
          this.highlightMe();
        }
      },
      { threshold: 1 }
    );
    this.endObserver = new IntersectionObserver(
      (entries) => {
        if (entries && entries.length && entries[0].isIntersecting) {
          this.highlightMe();
        }
      },
      { threshold: 1 }
    );
    this.coloring.coloringInfo$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((info) => {
        if (this.hand && info.coloringInfos?.length) {
          for (let ci of info.coloringInfos) {
            if (ci.hand === this.hand) {
              this.textColor = ci.color;
              this.changeDetector.detectChanges();
              return;
            }
          }
        }
      });
    if (this.type === 'writing_session') {
      if (this.hand) {
        this.handInfo = 'Geschrieben von ' + this.hand;
      }
      const sessionCount = Number.parseInt(this.n);
      if (sessionCount % 2 === 1) {
        this.backgroundColor = '#eee';
      } else {
        this.backgroundColor = '#ddd';
      }
    }
    this.initFinished = true;
    this.changeDetector.detectChanges();
  }

  ngAfterViewInit(): void {
    this.beginObserver.observe(this.myBeginAnchor.nativeElement);
    this.endObserver.observe(this.myEndAnchor.nativeElement);
  }

  highlightMe(): void {
    this.scrollIntoView.highlight(this.highlightid);
  }
}
