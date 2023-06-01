import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { CommentDoc } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-materialien-view',
  templateUrl: './materialien-view.component.html',
  styleUrls: ['./materialien-view.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MaterialienViewComponent implements OnInit, OnDestroy {
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
          this.loadMaterialDoc(id);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  loadMaterialDoc(id: string): void {
    this.application.ma_loadMaterialDoc(id).subscribe((doc) => {
      this.commentDoc = doc;
      this.changeDetector.detectChanges();
    });
  }

  safeHtml(con: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(con);
  }
}
