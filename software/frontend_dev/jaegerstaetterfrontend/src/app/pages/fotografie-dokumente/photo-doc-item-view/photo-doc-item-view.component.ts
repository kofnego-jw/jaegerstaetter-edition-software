import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Gallery, GalleryRef, ImageItem } from 'ng-gallery';
import { Lightbox } from 'ng-gallery/lightbox';
import {
  faArrowLeft,
  faArrowRight,
  faAngleDoubleLeft,
  faAngleLeft,
  faAngleRight,
  faAngleDoubleRight,
} from '@fortawesome/free-solid-svg-icons';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  PhotoDocumentGroup,
  PhotoDocumentGroupType,
  PhotoDocumentItem,
} from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-photo-doc-item-view',
  templateUrl: './photo-doc-item-view.component.html',
  styleUrls: ['./photo-doc-item-view.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PhotoDocItemViewComponent implements OnInit, OnDestroy {
  faArrowLeft = faArrowLeft;
  faArrowRight = faArrowRight;
  faAngleDoubleLeft = faAngleDoubleLeft;
  faAngleLeft = faAngleLeft;
  faAngleRight = faAngleRight;
  faAngleDoubleRight = faAngleDoubleRight;

  destroyed$: Subject<boolean> = new Subject();

  itemIndex: number = 0;

  parentRouterLink: string = '';

  prevRouterLink: string = '';

  nextRouterLink: string = '';

  type: PhotoDocumentGroupType;

  typeName: string;

  groupKey: string;

  itemId: string;

  group: PhotoDocumentGroup;

  item: PhotoDocumentItem;

  myGallery: GalleryRef;

  galleryId: string;

  imageItems: ImageItem[] = [];

  constructor(
    private activatedRoute: ActivatedRoute,
    private application: ApplicationService,
    private gallery: Gallery,
    private lightbox: Lightbox,
    private changeDetector: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((paramMap) => {
        const groupType = paramMap.get('groupType');
        if (groupType == 'document') {
          this.type = PhotoDocumentGroupType.DOCUMENT;
          this.typeName = 'Dokumente';
        } else {
          this.type = PhotoDocumentGroupType.PHOTO;
          this.typeName = 'Fotos';
        }
        const groupKey = paramMap.get('groupKey');
        if (groupKey) {
          this.groupKey = groupKey;
        }
        const itemSig = paramMap.get('itemId');
        if (itemSig) {
          this.itemId = itemSig;
        }
        this.fetchGroupAndItem();
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  fetchGroupAndItem(): void {
    if (!this.type || !this.groupKey || !this.itemId) {
      return;
    }
    this.application.photoDocumentGroups$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((groups) => {
        const groupOpt = groups.filter(
          (g) => g.type == this.type && g.groupKey === this.groupKey
        );
        if (groupOpt.length) {
          const group = groupOpt[0];
          const itemOpt = group.items.filter((item) => item.id === this.itemId);
          if (itemOpt.length) {
            const item = itemOpt[0];
            this.setGroupAndItem(group, item);
          }
        }
      });
  }

  setGroupAndItem(group: PhotoDocumentGroup, item: PhotoDocumentItem): void {
    this.group = group;
    this.item = item;
    this.galleryId = this.group.groupKey + ' - ' + this.item.signature;
    this.myGallery = this.gallery.ref(this.galleryId);
    this.imageItems = [];
    for (let jpg of item.jpegs) {
      const url = this.application.pd_imageSourceWithJpgAsString(
        this.group,
        jpg
      );
      this.imageItems.push(new ImageItem({ src: url, thumb: url }));
    }
    this.myGallery.load(this.imageItems);
    this.prevRouterLink = this.prevItemUrl();
    this.nextRouterLink = this.nextItemUrl();
    this.itemIndex = this.group.items.indexOf(this.item) + 1;
    this.parentRouterLink = this.application.pd_routerLinkGroup(this.group);
    this.changeDetector.detectChanges();
  }

  imageSource(jpg: string): SafeResourceUrl {
    return this.application.pd_imageSourceWithJpg(this.group, jpg);
  }

  showPicture(index: number): void {
    this.lightbox.open(index, this.galleryId, { panelClass: 'fullscreen' });
  }

  nextItemUrl(): string {
    if (this.group && this.item) {
      const nextIndex = this.group.items.indexOf(this.item) + 1;
      if (nextIndex < this.group.items.length) {
        const nextItem = this.group.items[nextIndex];
        return this.application.pd_routerLink(this.group, nextItem);
      }
    }
    return '';
  }

  prevItemUrl(): string {
    if (this.group && this.item) {
      const prevIndex = this.group.items.indexOf(this.item) - 1;
      if (prevIndex >= 0) {
        const prevItem = this.group.items[prevIndex];
        return this.application.pd_routerLink(this.group, prevItem);
      }
    }
  }

  fastPrev(): void {
    if (this.group) {
      const item = this.group.items[0];
      const link = this.application.pd_routerLink(this.group, item);
      this.router.navigate([link]);
    }
  }

  fastNext(): void {
    if (this.group) {
      const item = this.group.items[this.group.items.length - 1];
      const link = this.application.pd_routerLink(this.group, item);
      this.router.navigate([link]);
    }
  }

  prev(): void {
    if (this.prevRouterLink) {
      this.router.navigate([this.prevRouterLink]);
    }
  }

  next(): void {
    if (this.nextRouterLink) {
      this.router.navigate([this.nextRouterLink]);
    }
  }

  gotoPage(event: Event): void {
    if (this.group) {
      const pn = Number.parseInt((event.target as HTMLInputElement).value);
      if (pn > 0 && pn <= this.group.items.length) {
        const index = pn - 1;
        const item = this.group.items[index];
        const link = this.application.pd_routerLink(this.group, item);
        this.router.navigate([link]);
      }
    }
  }
}
