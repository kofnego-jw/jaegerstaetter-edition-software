import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import {
  faDownload,
  faAngleLeft,
  faAngleRight,
  faAngleDoubleLeft,
  faAngleDoubleRight,
  faTriangleExclamation,
} from '@fortawesome/free-solid-svg-icons';
import { ReplaySubject, Subject, Subscription } from 'rxjs';
import { takeUntil, take } from 'rxjs/operators';
import { ResourceDTO, VersionInfo } from 'src/app/models/dto';
import {
  BriefPanelView,
  PanelPosition,
  ResourceListCurrentState,
  ResourceListState,
} from 'src/app/models/frontend';
import { ApplicationService } from 'src/app/services/application.service';
import { SearchStateService } from 'src/app/services/search-state.service';
import { ResourceService } from 'src/app/services/resource.service';
import { RegistryStateService } from 'src/app/services/registry-state.service';
import { ViewPageService } from 'src/app/services/view-page.service';
import {
  Referrer,
  ReferrerToResourceService,
} from 'src/app/services/referrer-to-resource.service';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';
import { ViewVersionService } from 'src/app/services/view-version.service';

@Component({
  selector: 'app-edition-anzeige',
  templateUrl: './edition-anzeige.component.html',
  styleUrls: ['./edition-anzeige.component.scss'],
})
export class EditionAnzeigeComponent implements OnInit, OnDestroy {
  PanelPosition = PanelPosition;
  BriefPanelView = BriefPanelView;
  faDownload = faDownload;
  faAngleLeft = faAngleLeft;
  faAngleRight = faAngleRight;
  faAngleDoubleLeft = faAngleDoubleLeft;
  faAngleDoubleRight = faAngleDoubleRight;
  faTriangleExclamation = faTriangleExclamation;

  destroyed$: Subject<boolean>;

  resource: ResourceDTO;

  referrer: Referrer = Referrer.EDITION;

  resourceListState: ResourceListState = null;

  resourceListStateSubscription: Subscription = null;

  currentState: ResourceListCurrentState = ResourceListCurrentState.EMPTY;

  useSingle$: ReplaySubject<boolean> = new ReplaySubject();

  versionString: string = '';

  viewOldVersion: boolean = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private application: ApplicationService,
    private searchState: SearchStateService,
    private resourceService: ResourceService,
    private referrerService: ReferrerToResourceService,
    private registryState: RegistryStateService,
    private viewPage: ViewPageService,
    private router: Router,
    private location: Location,
    private scrollIntoView: ScrollIntoViewService,
    private viewVersion: ViewVersionService
  ) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.useSingle$.complete();
    this.viewVersion.reset();
  }

  ngOnInit(): void {
    const checkSize = () => {
      return document.body.offsetWidth < 1505;
    };
    this.useSingle$.next(checkSize());
    window.addEventListener('resize', () => {
      this.useSingle$.next(checkSize());
    });
    this.referrerService.referrer$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((ref) => this.setReferrer(ref));
    this.application.currentResource$
      .pipe(takeUntil(this.destroyed$))
      .subscribe({
        next: (res) => this.setResource(res),
      });
    this.activatedRoute.queryParamMap.pipe(take(1)).subscribe((qParams) => {
      const scrollTo = qParams.get('scrollTo');
      if (scrollTo) {
        this.scrollIntoView.view(scrollTo);
      }
    });
    this.viewVersion.reset();

    this.loadResource();
  }

  setReferrer(ref: Referrer): void {
    this.referrer = ref;
    if (ref.isFromSearch()) {
      this.setResourceListState(this.searchState);
    } else if (ref.isFromRegistry()) {
      this.setResourceListState(this.registryState);
    } else {
      this.setResourceListState(this.resourceService);
    }
  }

  setResourceListState(state: ResourceListState): void {
    if (this.resourceListStateSubscription) {
      this.resourceListStateSubscription.unsubscribe();
      this.resourceListStateSubscription = null;
    }
    this.resourceListState = state;
    if (this.resourceListState) {
      this.resourceListStateSubscription = this.resourceListState
        .getCurrentState$()
        .pipe(takeUntil(this.destroyed$))
        .subscribe((state) => this.setResourceListCurrentState(state));
      this.setResource(this.resource);
    } else {
      this.setResourceListCurrentState(ResourceListCurrentState.EMPTY);
    }
  }

  setResourceListCurrentState(state: ResourceListCurrentState): void {
    this.currentState = state;
  }

  loadResource(): void {
    this.activatedRoute.params
      .pipe(takeUntil(this.destroyed$))
      .subscribe((params) => {
        if (params.id && params.id.length) {
          this.application.setResourceById(params.id);
        }
      });
  }

  setVersion(version: number): void {
    const found = this.resource.versions.filter(
      (v) => v.versionNumber === version
    );
    if (found.length) {
      this.viewVersion.setDate(found[0].creationTimestamp);
      this.application.setResourceByIdAndDate(
        this.resource.id,
        this.viewVersion.date
      );
      this.versionString = this.getVersionString(found[0]);
      this.viewOldVersion = this.versionString.startsWith('Version');
    }
  }

  getVersionString(version: VersionInfo): string {
    if (!version) {
      return '';
    }
    if (this.resource.versions[0] === version) {
      return (
        'Aktuelle Version (' +
        this.application.dateStringToDate(version.creationTimestamp) +
        ')'
      );
    }
    return (
      'Version vom ' +
      this.application.dateStringToDate(version.creationTimestamp)
    );
  }

  resetVersionString(): void {
    if (this.resource) {
      this.versionString = this.getVersionString(this.resource.versions[0]);
    } else {
      this.versionString = '';
    }
  }

  setResource(res: ResourceDTO): void {
    if (this.resource && this.resource.id !== res.id) {
      const url: string = this.router
        .createUrlTree(['/edition/view/' + res.id], {
          relativeTo: this.activatedRoute,
        })
        .toString();
      this.location.go(url);
      this.resetVersionString();
    }
    this.viewPage.viewPage(-1, true, '', 'new');
    this.resource = res;
    if (this.searchState) {
      this.searchState.setCurrent(this.resource);
    }
    if (this.resourceService) {
      this.resourceService.setCurrent(this.resource);
    }
    if (!this.versionString) {
      this.resetVersionString();
    }
  }

  getLeftView(): BriefPanelView {
    return BriefPanelView.FACSIMILE;
  }

  getRightView(): BriefPanelView {
    return BriefPanelView.NORM;
  }
}
