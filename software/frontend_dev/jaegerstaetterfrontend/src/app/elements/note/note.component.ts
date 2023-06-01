import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { faPenToSquare } from '@fortawesome/free-solid-svg-icons';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';
import { ApplicationService } from 'src/app/services/application.service';
import { ViewVersionService } from 'src/app/services/view-version.service';

@Component({
  selector: 'app-note',
  templateUrl: './note.component.html',
  styleUrls: ['./note.component.scss'],
})
export class NoteComponent implements OnInit, AfterViewInit, OnDestroy {
  faPenToSquare = faPenToSquare;

  destroyed$: Subject<boolean> = new Subject();

  @ViewChild('myModalTemplate')
  myModalTemplate: TemplateRef<any>;

  @Input()
  n: string;

  @Input()
  noteid: string;

  @Input()
  subtype: string;

  @Input()
  target: string;

  @Input()
  directlink: string;

  @Input()
  targettype: string;

  @Input()
  highlight: string = '';

  commentType: string = '';

  myTemplateCopy: SafeHtml;

  noteContent: SafeHtml;

  show: boolean = true;

  constructor(
    private bsModalService: BsModalService,
    private annotationView: AnnotationViewService,
    private application: ApplicationService,
    private viewVersion: ViewVersionService,
    private sanitizer: DomSanitizer
  ) {
    this.noteContent = this.sanitizer.bypassSecurityTrustHtml(
      'Kommentar wird geladen.'
    );
    this.setAnnotationView(annotationView.currentAnnotationView);
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.commentType = this.application.note_translateNoteType(this.subtype);
    if (this.noteid) {
      if (this.viewVersion.date) {
        this.application
          .getNoteWithDate(this.noteid, this.viewVersion.date)
          .pipe(take(1))
          .subscribe((noteDTO) => {
            this.noteContent = this.sanitizer.bypassSecurityTrustHtml(
              noteDTO.noteContent
            );
          });
      } else {
        this.application
          .getNote(this.noteid)
          .pipe(take(1))
          .subscribe((noteDTO) => {
            this.noteContent = this.sanitizer.bypassSecurityTrustHtml(
              noteDTO.noteContent
            );
          });
      }
    }
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
  }

  ngAfterViewInit(): void {}

  showNote(): void {
    this.bsModalService.show(this.myModalTemplate);
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    this.show = !view || view !== AnnotationViewEnum.NONE;
  }
}
