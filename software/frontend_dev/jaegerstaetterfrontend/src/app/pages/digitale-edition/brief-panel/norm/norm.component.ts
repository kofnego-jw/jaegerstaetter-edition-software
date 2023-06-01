import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { MarkRsRequest, RegistryType, ResourceDTO } from 'src/app/models/dto';
import { PanelPosition } from 'src/app/models/frontend';
import { ApplicationService } from 'src/app/services/application.service';
import {
  Referrer,
  ReferrerToResourceService,
} from 'src/app/services/referrer-to-resource.service';
import { RegistryStateService } from 'src/app/services/registry-state.service';
import { SearchStateService } from 'src/app/services/search-state.service';

@Component({
  selector: 'app-norm',
  templateUrl: './norm.component.html',
  styleUrls: ['./norm.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NormComponent implements OnInit, OnChanges, OnDestroy {
  PanelPosition = PanelPosition;
  destroyed$: Subject<boolean> = new Subject();

  fullScreen: boolean = false;

  showAnnotation: boolean = false;

  content: SafeHtml = null;

  @Input()
  panelPosition: PanelPosition;

  @ViewChild('FullComponent')
  fullComponent: ElementRef;

  @Input()
  resource: ResourceDTO;

  referrer: Referrer = Referrer.EDITION;

  constructor(
    private sanitizer: DomSanitizer,
    private searchState: SearchStateService,
    private registryState: RegistryStateService,
    private referrerService: ReferrerToResourceService,
    private application: ApplicationService,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.changeContent();
  }

  ngOnInit(): void {
    this.referrerService.referrer$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((ref) => this.setReferrer(ref));
    this.changeContent();
  }

  setReferrer(ref: Referrer): void {
    this.referrer = ref;
    this.changeContent();
  }

  changeContent(): void {
    if (this.referrer.isFromSearch()) {
      const request = this.searchState?.searchResult?.searchRequest;
      if (request) {
        this.application
          .getNormWithSearch(this.resource, request)
          .pipe(take(1))
          .subscribe((cont) => {
            this.content = this.sanitizer.bypassSecurityTrustHtml(cont);
            this.changeDetector.detectChanges();
          });
      } else {
        this.content = this.sanitizer.bypassSecurityTrustHtml(
          this.resource.normalizedRepresentation
        );
        this.changeDetector.detectChanges();
      }
    } else if (this.referrer.isFromRegistry()) {
      const key = this.registryState.registryKey;
      const type = this.registryState.registryType;
      let registryType: RegistryType;
      switch (type) {
        case 'orte':
          registryType = RegistryType.PLACE;
          break;
        case 'organisationen':
          registryType = RegistryType.CORPORATION;
          break;
        case 'heilige':
        case 'personen':
          registryType = RegistryType.PERSON;
          break;
        case 'bibelstellen':
          registryType = RegistryType.BIBLE;
      }
      if (!registryType) {
        this.content = this.sanitizer.bypassSecurityTrustHtml(
          this.resource.normalizedRepresentation
        );
        this.changeDetector.detectChanges();
      } else {
        const mark = new MarkRsRequest(key, registryType);
        this.application
          .getNormWithMarkRs(this.resource, mark)
          .pipe(take(1))
          .subscribe((cont) => {
            this.content = this.sanitizer.bypassSecurityTrustHtml(cont);
            this.changeDetector.detectChanges();
          });
      }
    } else {
      this.content = this.sanitizer.bypassSecurityTrustHtml(
        this.resource.normalizedRepresentation
      );
      this.changeDetector.detectChanges();
    }
  }

  toggleFullscreen() {
    const element = this.fullComponent.nativeElement;
    if (!this.fullScreen) {
      this.fullScreen = true;
      element.requestFullscreen();
    } else {
      this.fullScreen = false;
      document.exitFullscreen();
    }
  }
}
