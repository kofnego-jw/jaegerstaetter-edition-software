import { Component, OnDestroy, OnInit } from '@angular/core';
import { DomSanitizer, SafeHtml, SafeUrl } from '@angular/platform-browser';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MetadataGroup, ResourceDTO, VersionInfo } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { ModalService } from 'src/app/services/modal.service';

@Component({
  selector: 'app-metadata-view',
  templateUrl: './metadata-view.component.html',
  styleUrls: ['./metadata-view.component.scss'],
})
export class MetadataViewComponent implements OnInit, OnDestroy {
  faDownload = faDownload;

  correspDesc: MetadataGroup = null;

  msIdentifier: MetadataGroup = null;

  physDesc: MetadataGroup = null;

  respStmt: MetadataGroup = null;

  history: MetadataGroup = null;

  summary: SafeHtml = null;

  objectType: string = '';

  destroyed$: Subject<boolean> = new Subject();

  resource: ResourceDTO;

  author: string;

  title: string;

  versionInfo: VersionInfo;

  constructor(
    private application: ApplicationService,
    private sanitizer: DomSanitizer,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.application.currentResource$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((res) => this.setResource(res));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  setResource(res: ResourceDTO) {
    this.resource = res;
    if (res?.metadata.length) {
      this.correspDesc = res.getMetadata('correspDesc');
      this.msIdentifier = res.getMetadata('msIdentifier');
      this.physDesc = res.getMetadata('physDesc');
      this.respStmt = res.getMetadata('respStmt');
      this.history = res.getMetadata('history');
      this.summary = this.sanitizer.bypassSecurityTrustHtml(res.summary);
      this.objectType = res.objectTypes.join(' / ');
      this.author = res.authors.reduce((prev, curr) => prev + '; ' + curr);
      this.title = res.title;
      this.versionInfo = res.versions.length
        ? res.versions[0]
        : new VersionInfo(1, '2023-06-01');
    } else {
      this.correspDesc = null;
      this.msIdentifier = null;
      this.summary = '';
      this.objectType = '';
    }
  }

  safeHtml(s: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(s);
  }

  pdfUrl(): SafeUrl {
    if (this.resource) {
      return this.application.getPdfUrl(this.resource);
    }
    return undefined;
  }

  xmlUrl(): SafeUrl {
    if (this.resource) {
      return this.application.getXmlUrl(this.resource);
    }
    return undefined;
  }

  showCitation(): void {
    this.modalService.openCitationInfo(this.resource, this.versionInfo);
  }
}
