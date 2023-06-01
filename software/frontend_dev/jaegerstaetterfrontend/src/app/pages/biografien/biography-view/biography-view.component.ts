import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { DomSanitizer, SafeHtml, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Biography, VersionInfo } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { map, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
import { ModalService } from 'src/app/services/modal.service';

@Component({
  selector: 'app-biography-view',
  templateUrl: './biography-view.component.html',
  styleUrls: ['./biography-view.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BiographyViewComponent implements OnInit, OnDestroy {
  faDownload = faDownload;
  destroyed$: Subject<boolean> = new Subject();

  biography: Biography = null;

  showToc: boolean = false;

  constructor(
    private application: ApplicationService,
    private activatedRoute: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private changeDetector: ChangeDetectorRef,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((paramMap) => {
        const id = paramMap.get('id');
        if (id) {
          this.loadBiography(id);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  loadBiography(id: string): void {
    this.application.bg_getSingleBiography(id).subscribe((biog) => {
      this.biography = biog;
      if (this.biography.toc) {
        this.showToc = true;
      } else {
        this.showToc = false;
      }
      this.changeDetector.detectChanges();
    });
  }

  pdfLink(): SafeUrl {
    const url = '/api/biography/pdf/' + this.biography.filename;
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  safeHtml(con: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(con);
  }

  showCitation(): void {
    const version = new VersionInfo(1, '2023-06-01');
    setTimeout(() =>
      this.modalService.openCitationInfo(this.biography, version)
    );
  }
}
