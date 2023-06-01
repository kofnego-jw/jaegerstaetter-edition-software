import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Subject, take } from 'rxjs';
import { CommentDoc } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-biography-index',
  templateUrl: './biography-index.component.html',
  styleUrls: ['./biography-index.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BiographyIndexComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  commentDoc: CommentDoc = null;
  constructor(
    private application: ApplicationService,
    private sanitizer: DomSanitizer,
    private scrollIntoView: ScrollIntoViewService,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.application
      .bg_loadIndex()
      .pipe(take(1))
      .subscribe((doc) => {
        this.commentDoc = doc;
        this.changeDetector.detectChanges();
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  safeHtml(con: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(con);
  }
}
