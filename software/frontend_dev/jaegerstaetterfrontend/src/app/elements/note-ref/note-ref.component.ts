import {
  Component,
  Input,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { DomSanitizer, SafeHtml, SafeUrl } from '@angular/platform-browser';
import { faPenToSquare } from '@fortawesome/free-solid-svg-icons';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { PhotoDocumentGroup, ResourceFWDTO } from 'src/app/models/dto';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';
import { ApplicationService } from 'src/app/services/application.service';
import { ViewVersionService } from 'src/app/services/view-version.service';

@Component({
  selector: 'app-note-ref',
  templateUrl: './note-ref.component.html',
  styleUrls: ['./note-ref.component.scss'],
})
export class NoteRefComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  faPenToSquare = faPenToSquare;

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

  commentType: string = '';

  mylink: SafeUrl;

  mylinkText: string;

  mySrc: SafeUrl[] = [];

  myContent: SafeHtml;

  linkedSources: ResourceFWDTO[] = [];

  show: boolean = true;

  constructor(
    private application: ApplicationService,
    private bsModalService: BsModalService,
    private annotationView: AnnotationViewService,
    private viewVersion: ViewVersionService,
    private sanitizer: DomSanitizer
  ) {
    this.setAnnotationView(annotationView.currentAnnotationView);
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.commentType = this.application.note_translateNoteType(this.subtype);
    if (this.target) {
      if (this.targettype === 'internal_link') {
        const links = this.target.split(/\s*;\s*/);
        this.application.resourceList$.pipe(take(1)).subscribe((resList) => {
          this.linkedSources = resList.filter(
            (res) => links.indexOf(res.id) >= 0
          );
        });
      } else if (this.targettype === 'external_link') {
        this.mylink = this.sanitizer.bypassSecurityTrustUrl(this.target);
        this.mylinkText = this.target.replace(/\//, '/&#x200b;');
      } else if (this.targettype === 'internal_image') {
        const links = this.target.split(/\s*;\s*/);
        this.mySrc = links.map((link) => {
          const myLink = link.endsWith('.jpg')
            ? link.replace('.jpg', '')
            : link;
          return this.sanitizer.bypassSecurityTrustResourceUrl(
            this.application.pd_imageSourceWithJpgAsString(
              PhotoDocumentGroup.DUMMY,
              myLink
            )
          );
        });
      }
    }
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
    if (this.viewVersion.date) {
      this.application
        .getNoteWithDate(this.noteid, this.viewVersion.date)
        .pipe(take(1))
        .subscribe((note) => {
          this.myContent = this.sanitizer.bypassSecurityTrustHtml(
            note.noteContent
          );
        });
    } else {
      this.application
        .getNote(this.noteid)
        .pipe(take(1))
        .subscribe((note) => {
          this.myContent = this.sanitizer.bypassSecurityTrustHtml(
            note.noteContent.replace(/<[^>]+>/g, '')
          );
        });
    }
  }

  showInfo(): void {
    if (this.directlink === 'true') {
      if (this.targettype === 'external_link') {
        window.open(this.target, '_blank');
        return;
      }
    }
    this.bsModalService.show(this.myModalTemplate);
  }

  getHref(fw: ResourceFWDTO): SafeUrl {
    return this.application.res_getFloatingHrefFromResource(fw);
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    this.show = !view || view !== AnnotationViewEnum.NONE;
  }
}
