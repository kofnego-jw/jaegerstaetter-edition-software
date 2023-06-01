import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { DomSanitizer, SafeHtml, SafeUrl } from '@angular/platform-browser';
import { BsModalService } from 'ngx-bootstrap/modal';
import { take } from 'rxjs/operators';
import {
  BiographyFW,
  PhotoDocumentGroup,
  ResourceFWDTO,
} from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-ref',
  templateUrl: './ref.component.html',
  styleUrls: ['./ref.component.scss'],
})
export class RefComponent implements OnInit, AfterViewInit {
  @ViewChild('myModalTemplate')
  myModalTemplate: TemplateRef<any>;

  @Input()
  target: string;

  @Input()
  directlink: string;

  @Input()
  targettype: string;

  mylink: SafeUrl;

  mylinkText: string;

  mySrc: SafeUrl;

  myContent: SafeHtml;

  linkedSources: ResourceFWDTO[] = [];

  linkedBiografies: BiographyFW[] = [];

  constructor(
    private application: ApplicationService,
    private bsModalService: BsModalService,
    private sanitizer: DomSanitizer
  ) {}

  ngAfterViewInit(): void {}

  ngOnInit(): void {
    if (this.target) {
      if (this.targettype === 'internal_link') {
        const links = this.target.split(/\s*;\s*/);
        const editionLinks = links.filter((x) => !x.startsWith('biography/'));
        this.application.resourceList$.pipe(take(1)).subscribe((resList) => {
          this.linkedSources = resList.filter(
            (res) => editionLinks.indexOf(res.id) >= 0
          );
        });
        const biographyLinks = links
          .filter((x) => x.startsWith('biography/'))
          .map((x) => x.substring('biography/'.length));
        if (biographyLinks.length) {
          this.application
            .bg_getBiographies()
            .pipe(take(1))
            .subscribe((bioList) => {
              this.linkedBiografies = bioList.filter(
                (bio) => biographyLinks.indexOf(bio.filename) >= 0
              );
            });
        }
      } else if (this.targettype === 'external_link') {
        this.mylink = this.sanitizer.bypassSecurityTrustUrl(this.target);
        this.mylinkText = this.target.replace(/\//, '/&#x200b;');
      } else if (this.targettype === 'internal_image') {
        const withoutEnding = this.target.endsWith('.jpg')
          ? this.target.replace('.jpg', '')
          : this.target;
        this.mySrc = this.sanitizer.bypassSecurityTrustResourceUrl(
          this.application.pd_imageSourceWithJpgAsString(
            PhotoDocumentGroup.DUMMY,
            withoutEnding
          )
        );
      }
    }
  }

  showInfo(): void {
    if (!this.targettype || this.targettype === 'unknown') {
      return;
    }
    if (this.directlink === 'true') {
      if (this.targettype === 'external_link') {
        window.open(this.target, '_blank');
        return;
      }
      if (this.linkedBiografies.length === 1) {
        const bioLink = '/#/biografien/' + this.linkedBiografies[0].filename;
        window.open(bioLink, '_blank');
        return;
      }
    }
    this.bsModalService.show(this.myModalTemplate);
  }

  getHref(fw: ResourceFWDTO): SafeUrl {
    return this.application.res_getFloatingHrefFromResource(fw);
  }

  getBioHref(bio: BiographyFW): SafeUrl {
    return this.application.res_getFloatingHrefFromResourceId(
      'biografien/' + bio.filename
    );
  }
}
