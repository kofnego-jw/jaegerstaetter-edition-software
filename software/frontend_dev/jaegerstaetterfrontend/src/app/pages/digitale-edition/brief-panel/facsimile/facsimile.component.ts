import {
  Component,
  Input,
  NgZone,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import * as OpenSeadragon from 'openseadragon';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ResourceDTO } from 'src/app/models/dto';
import { PanelPosition } from 'src/app/models/frontend';
import { ViewPageService } from 'src/app/services/view-page.service';

@Component({
  selector: 'app-facsimile',
  templateUrl: './facsimile.component.html',
  styleUrls: ['./facsimile.component.scss'],
})
export class FacsimileComponent implements OnInit, OnDestroy {
  PanelPosition = PanelPosition;

  destroyed$: Subject<boolean> = new Subject();

  @Input()
  panelPosition: PanelPosition;

  @Input()
  resource: ResourceDTO;

  facsimileUrls: string[] = [];

  currentFacsimileId: string;

  viewerId: string;

  openSeadragonViewer: OpenSeadragon.Viewer;

  constructor(private viewPage: ViewPageService, private ngZone: NgZone) {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.viewerId = 'OSD_' + this.panelPosition;
    this.viewPage.viewPage$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((pbEvent) => {
        if (pbEvent) {
          this.setPage(pbEvent.facs);
        }
      });
    this.setResource(this.resource);
  }

  getPageNumber(): number {
    if (this.resource?.facsimileIds) {
      return this.resource.facsimileIds.indexOf(this.currentFacsimileId);
    } else {
      return -1;
    }
  }

  hasPrev(): boolean {
    const pn = this.getPageNumber();
    if (pn < 1) {
      return false;
    }
    return true;
  }

  showPrev(): void {
    if (!this.hasPrev()) {
      return;
    }
    const pn = this.getPageNumber();
    this.setPageNumber(pn - 1);
  }

  setPageNumber(pn: number): void {
    if (!this.resource) {
      return;
    }
    if (pn < 0) {
      return;
    }
    if (pn >= this.resource.facsimileIds.length - 1) {
      return;
    }
    this.currentFacsimileId = this.resource.facsimileIds[pn];
    this.renderFacsimile();
  }

  hasNext(): boolean {
    const pn = this.getPageNumber();
    if (pn < 0 || pn >= this.resource.facsimileIds.length - 1) {
      return false;
    }
    return true;
  }

  showNext(): void {
    if (!this.hasNext()) {
      return;
    }
    const pn = this.getPageNumber();
    this.setPageNumber(pn + 1);
  }

  setResource(resource: ResourceDTO): void {
    this.resource = resource;
    this.openSeadragonViewer = null;
    if (this.resource?.facsimileIds?.length) {
      this.facsimileUrls = this.resource.facsimileIds;
      if (this.facsimileUrls.length) {
        this.currentFacsimileId = this.facsimileUrls[0];
        this.renderFacsimile();
        return;
      }
    }
  }

  setPage(facs: string): void {
    if (!facs || !this.resource?.facsimileIds?.length) {
      return;
    }
    this.currentFacsimileId = facs;
    this.renderFacsimile();
  }

  renderFacsimile(): void {
    setTimeout(() => {
      this.ngZone.runOutsideAngular(() => {
        if (!this.openSeadragonViewer) {
          this.openSeadragonViewer = OpenSeadragon({
            id: this.viewerId,
            prefixUrl: 'assets/openseadragon/images_kenon/',
            showRotationControl: true,
            minZoomLevel: 0.5,
            // homeFillsViewer: true,
            tileSources: {
              type: 'image',
              url: this.createUrl(this.currentFacsimileId),
            },
          });
        } else {
          const tileSource = {
            type: 'image',
            url: this.createUrl(this.currentFacsimileId),
          };
          this.openSeadragonViewer.open(tileSource);
        }
      });
    });
  }

  createUrl(id: string): string {
    const base = 'api/resource/facs/' + id;
    return base.endsWith('.jpg') ? base : base + '.jpg';
  }
}
