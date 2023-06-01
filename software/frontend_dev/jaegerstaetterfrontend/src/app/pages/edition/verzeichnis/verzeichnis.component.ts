import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  faTimesCircle,
  faChevronDown,
  faAngleLeft,
  faAngleDoubleLeft,
  faAngleRight,
  faAngleDoubleRight,
  faSort,
} from '@fortawesome/free-solid-svg-icons';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { ResourceFWDTO, SearchSortField } from 'src/app/models/dto';
import {
  PaginationInfo,
  PredefinedCorpus,
  ResourceFilter,
  ResourceFilterCategory,
  ResourceFilterInfo,
} from 'src/app/models/frontend';
import { ApplicationService } from 'src/app/services/application.service';
import { ReferrerToResourceService } from 'src/app/services/referrer-to-resource.service';
import { ResourceService } from 'src/app/services/resource.service';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-verzeichnis',
  templateUrl: './verzeichnis.component.html',
  styleUrls: ['./verzeichnis.component.scss'],
})
export class VerzeichnisComponent implements OnInit, OnDestroy {
  Category = ResourceFilterCategory;
  categoryFilters: ResourceFilterInfo[] = ResourceFilter.CORPUS_FILTERS.map(
    (x) => new ResourceFilterInfo(-1, x)
  );
  periodFilters: ResourceFilterInfo[] = [];
  authorFilters: ResourceFilterInfo[] = [];
  recipientFilters: ResourceFilterInfo[] = [];
  placeFilters: ResourceFilterInfo[] = [];
  objectTypeFilters: ResourceFilterInfo[] = [];

  faTimesCircle = faTimesCircle;
  faChevronDown = faChevronDown;
  faAngleLeft = faAngleLeft;
  faAngleRight = faAngleRight;
  faAngleDoubleLeft = faAngleDoubleLeft;
  faAngleDoubleRight = faAngleDoubleRight;
  faSort = faSort;

  destroyed$: Subject<boolean>;

  resourcePaginationInfo: PaginationInfo<ResourceFWDTO>;

  shownResources: ResourceFWDTO[] = [];

  sortFields = [SearchSortField.BY_DATING];

  sortAsc: boolean = true;
  sortField: SearchSortField = null;

  pageNumber: number = 0;
  pageSize: number = 10;
  totalHits: number = 0;
  maxPageCount: number = 0;
  fromNumber: number = 0;
  toNumber: number = 0;

  selectedCorpus: PredefinedCorpus = PredefinedCorpus.COMPLETE;

  hasSpecialCorresp: boolean = false;

  constructor(
    private application: ApplicationService,
    private resourceService: ResourceService,
    private activatedRoute: ActivatedRoute,
    private referrerService: ReferrerToResourceService,
    private scrollIntoView: ScrollIntoViewService
  ) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.application.isPreview.pipe(take(1)).subscribe((isPreview) => {
      if (isPreview) {
        this.sortFields.push(SearchSortField.BY_RESOURCE_ID);
      }
    });
    this.activatedRoute.queryParamMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((paramMap) => {
        if (paramMap.get('corpus')) {
          this.setPredefinedCorpus(paramMap.get('corpus'));
        }
        if (paramMap.get('partners1') && paramMap.get('partners2')) {
          this.setCorrespPartners(
            paramMap.get('partners1'),
            paramMap.get('partners2')
          );
        } else {
          this.hasSpecialCorresp = false;
        }
      });
    this.resourceService.shownResources$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((info) => this.setPaginationInfo(info));
    this.resourceService.sortField$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((sf) => (this.sortField = sf));
    this.resourceService.sortAsc$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((asc) => (this.sortAsc = asc));
    this.resourceService.periodFilters$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((filters) => (this.periodFilters = filters));

    this.resourceService.authorFilters$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((filters) => (this.authorFilters = filters));
    this.resourceService.recipientFilters$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((filters) => (this.recipientFilters = filters));
    this.resourceService.placeFilters$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((filters) => (this.placeFilters = filters));
    this.resourceService.objectTypeFilters$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((filters) => (this.objectTypeFilters = filters));
    this.loadResources();
  }

  setCorrespPartners(part1: string, part2: string) {
    const party1 = part1.split(/;/);
    const party2 = part2.split(/;/);
    const specialFilter = ResourceFilter.createPartnerFilter(party1, party2);
    this.resourceService.addSpecialFilter(specialFilter);
    this.selectedCorpus = new PredefinedCorpus(
      '/edition/ausgewaehlte-briefwechsel',
      'Ausgewählte Briefwechsel',
      specialFilter
    );
    this.hasSpecialCorresp = true;
    this.referrerService.setReferrer(
      'Ausgewählte Briefwechsel',
      '/ausgewaehlte-briefwechsel'
    );
  }

  setPredefinedCorpus(corpus: string): void {
    const defined = PredefinedCorpus.PREDEFINED.filter(
      (corp) => corp.pathComponent === corpus
    );
    if (defined.length) {
      this.selectedCorpus = defined[0];
      this.resourceService.addFilter(this.selectedCorpus.filter);
    }
  }

  loadResources(): void {
    this.resourceService.loadResources();
  }

  setPaginationInfo(info: PaginationInfo<ResourceFWDTO>): void {
    this.resourcePaginationInfo = info;
    if (!this.resourcePaginationInfo) {
      return;
    }
    this.totalHits = this.resourcePaginationInfo.totalHits();
    this.maxPageCount = this.resourcePaginationInfo.getPageCount();
    this.setPage(0);
  }

  setPage(pn: number): void {
    this.pageNumber = pn;
    this.shownResources = this.resourcePaginationInfo.getPage(pn);
    this.fromNumber = this.resourcePaginationInfo.getHitFrom();
    this.toNumber = this.resourcePaginationInfo.getHitTo();
    this.scrollIntoView.view('verzeichnisTop');
  }

  setSort(field: SearchSortField): void {
    this.resourceService.setSortField(field);
  }

  changeSortDir(asc: boolean): void {
    this.resourceService.setSortAsc(asc);
  }

  clearFilters(): void {
    this.resourceService.clearAllFilters();
  }
}
