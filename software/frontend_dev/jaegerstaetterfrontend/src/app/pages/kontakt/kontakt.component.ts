import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { CommentDoc } from 'src/app/models/dto';
import { MenuControllerService } from 'src/app/services/http/menu-controller.service';

@Component({
  selector: 'app-kontakt',
  templateUrl: './kontakt.component.html',
  styleUrls: ['./kontakt.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class KontaktComponent implements OnInit {
  commentDoc: CommentDoc;

  constructor(
    private menuController: MenuControllerService,
    private changeDetector: ChangeDetectorRef,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.menuController
      .contactCommentDoc()
      .subscribe((msg) => this.setDocument(msg.commentDoc));
  }

  setDocument(doc: CommentDoc): void {
    this.commentDoc = doc;
    this.changeDetector.detectChanges();
  }

  safeHtml(s: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(s);
  }
}
