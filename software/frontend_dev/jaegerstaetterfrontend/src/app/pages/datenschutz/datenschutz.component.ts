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
  selector: 'app-datenschutz',
  templateUrl: './datenschutz.component.html',
  styleUrls: ['./datenschutz.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DatenschutzComponent implements OnInit {
  commentDoc: CommentDoc;

  constructor(
    private menuController: MenuControllerService,
    private changeDetector: ChangeDetectorRef,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.menuController
      .gdprCommentDoc()
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
