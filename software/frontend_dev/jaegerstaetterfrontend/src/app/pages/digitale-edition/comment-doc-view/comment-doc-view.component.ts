import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { CommentDoc } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-comment-doc-view',
  templateUrl: './comment-doc-view.component.html',
  styleUrls: ['./comment-doc-view.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CommentDocViewComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  commentDoc: CommentDoc = null;
  constructor(
    private application: ApplicationService,
    private activatedRoute: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private scrollIntoView: ScrollIntoViewService,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((paramMap) => {
        const id = paramMap.get('id');
        if (id) {
          this.loadCommentDoc(id);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  loadCommentDoc(id: string): void {
    this.application.de_loadCommentDoc(id).subscribe((doc) => {
      this.commentDoc = doc;
      this.changeDetector.detectChanges();
    });
  }

  safeHtml(con: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(con);
  }
}
