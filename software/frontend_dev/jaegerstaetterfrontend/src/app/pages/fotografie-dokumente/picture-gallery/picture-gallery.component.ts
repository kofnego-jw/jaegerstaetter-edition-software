import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Gallery, GalleryItem, ImageItem } from 'ng-gallery';
import { Lightbox } from 'ng-gallery/lightbox';
import { Subject, Subscription } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  PhotoDocumentGroup,
  PhotoDocumentGroupType,
  PhotoDocumentItem,
} from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-picture-gallery',
  templateUrl: './picture-gallery.component.html',
  styleUrls: ['./picture-gallery.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PictureGalleryComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  type: PhotoDocumentGroupType = null;

  groups: PhotoDocumentGroup[] = [];

  waitForSetupSubscription: Subscription;

  constructor(
    private application: ApplicationService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.waitForSetupSubscription =
      this.application.photoDocumentGroups$.subscribe((groups) => {
        if (groups?.length) {
          this.initLoadGroup();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  initLoadGroup(): void {
    this.activatedRoute.paramMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((paramMap) => {
        const groupId = paramMap.get('groupType');
        if (groupId == 'document') {
          this.type = PhotoDocumentGroupType.DOCUMENT;
        } else {
          this.type = PhotoDocumentGroupType.PHOTO;
        }
        if (groupId) {
          this.loadGroup();
          if (
            this.waitForSetupSubscription &&
            !this.waitForSetupSubscription.closed
          ) {
            this.waitForSetupSubscription.unsubscribe();
          }
        }
      });
  }

  setupGallery(): void {
    this.changeDetector.detectChanges();
  }

  loadGroup(): void {
    this.application.photoDocumentGroups$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((groups) => {
        this.groups = groups.filter((g) => g.type == this.type);
        this.setupGallery();
      });
  }

  showPicture(group: PhotoDocumentGroup, item: PhotoDocumentItem): void {
    const link = this.application.pd_routerLink(group, item);
    this.router.navigate([link]);
  }

  imageSource(
    group: PhotoDocumentGroup,
    item: PhotoDocumentItem
  ): SafeResourceUrl {
    return this.application.pd_imageSource(group, item);
  }
}
