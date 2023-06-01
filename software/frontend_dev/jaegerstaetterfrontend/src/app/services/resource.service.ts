import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { ResourceFWDTO, SearchSortField } from '../models/dto';
import {
  PaginationInfo,
  ResourceFilter,
  ResourceFilterCategory,
  ResourceFilterInfo,
  ResourceListCurrentState,
  ResourceListState,
} from '../models/frontend';
import { ApplicationService } from './application.service';
import { take } from 'rxjs/operators';
import { CloseMenuService } from './close-menu.service';

@Injectable({
  providedIn: 'root',
})
export class ResourceService implements ResourceListState {
  resourceList: ResourceFWDTO[] = [];

  filteredResources: ResourceFWDTO[] = [];

  paginationInfo: PaginationInfo<ResourceFWDTO> = null;

  shownResources$: ReplaySubject<PaginationInfo<ResourceFWDTO>> =
    new ReplaySubject(1);

  filters: ResourceFilter[] = [];
  filters$: ReplaySubject<ResourceFilter[]> = new ReplaySubject(1);

  sortField: SearchSortField = null;
  sortField$: ReplaySubject<SearchSortField> = new ReplaySubject(1);

  sortAsc: boolean = true;
  sortAsc$: ReplaySubject<boolean> = new ReplaySubject(1);

  completePeriods: ResourceFilter[] = ResourceFilter.PERIOD_FILTERS;
  periodFilters$: ReplaySubject<ResourceFilterInfo[]> = new ReplaySubject(1);
  completeAuthorFilters: ResourceFilter[] = [];
  authorFilters$: ReplaySubject<ResourceFilterInfo[]> = new ReplaySubject(1);
  completeRecipientFilters: ResourceFilter[] = [];
  recipientFilters$: ReplaySubject<ResourceFilterInfo[]> = new ReplaySubject(1);
  completePlaceFilters: ResourceFilter[] = [];
  placeFilters$: ReplaySubject<ResourceFilterInfo[]> = new ReplaySubject(1);
  completeObjectTypeFilters: ResourceFilter[] = [];
  objectTypeFilters$: ReplaySubject<ResourceFilterInfo[]> = new ReplaySubject(
    1
  );

  currentState: ResourceListCurrentState = ResourceListCurrentState.EMPTY;
  currentState$: ReplaySubject<ResourceListCurrentState> = new ReplaySubject(1);
  currentHit: number = 0;

  constructor(
    private application: ApplicationService,
    private closeMenu: CloseMenuService
  ) {
    this.shownResources$.next(null);
    this.filters$.next([]);
    this.sortField$.next(null);
    this.sortAsc$.next(true);

    this.loadResources();
    this.setSortField(SearchSortField.BY_DATING);
    this.currentState$.next(this.currentState);
  }

  setCurrent(res: ResourceFWDTO): void {
    if (!res || !this.filteredResources.length) {
      this.currentHit = 0;
    } else {
      const found = this.filteredResources.filter((re) => re.id === res.id);
      if (found.length) {
        const f = found[0];
        this.currentHit = this.filteredResources.indexOf(f);
      }
    }
    this.createNextResourceListCurrentState();
  }

  createNextResourceListCurrentState(): void {
    const nextState = new ResourceListCurrentState(
      this.getTotalCount(),
      this.currentHit,
      this.hasPrev(),
      this.hasNext(),
      'Zum Verzeichnis'
    );
    this.currentState = nextState;
    this.currentState$.next(this.currentState);
  }

  getCurrNumber(): number {
    return this.currentHit;
  }

  getCurr(): ResourceFWDTO {
    if (
      this.filteredResources.length &&
      this.filteredResources.length > this.currentHit
    ) {
      return this.filteredResources[this.currentHit];
    }
    return null;
  }

  getTotalCount(): number {
    if (this.filteredResources.length) {
      return this.filteredResources.length;
    }
    return 0;
  }

  hasNext(): boolean {
    return this.currentHit < this.filteredResources.length - 1;
  }

  next(): void {
    if (this.hasNext()) {
      const nextHitNum = this.currentHit + 1;
      const resource = this.filteredResources[nextHitNum];
      if (!resource) {
        throw new Error('Cannot find next.');
      }
      this.application.setResourceById(resource.id);
      this.closeMenu.closeMenu();
    }
  }

  hasPrev(): boolean {
    if (this.filteredResources.length) {
      return this.currentHit > 0;
    }
    return false;
  }

  prev(): void {
    if (this.hasPrev()) {
      const prevHitNumb = this.currentHit - 1;
      const resource = this.filteredResources[prevHitNumb];
      this.application.setResourceById(resource.id);
      this.closeMenu.closeMenu();
    }
  }

  getCurrentState$(): ReplaySubject<ResourceListCurrentState> {
    return this.currentState$;
  }

  loadResources(): void {
    this.application.resourceList$.subscribe((list) =>
      this.resetResourceList(list)
    );
    this.application.currentResource$.subscribe((cur) => this.setCurrent(cur));
    this.application
      .getAuthorKeys()
      .pipe(take(1))
      .subscribe((keys) => {
        this.completeAuthorFilters = keys.map((key) =>
          ResourceFilter.createMatchAuthor(key)
        );
        this.recreateSelectableFilters();
      });
    this.application
      .getRecipientKeys()
      .pipe(take(1))
      .subscribe((keys) => {
        this.completeRecipientFilters = keys.map((key) =>
          ResourceFilter.createMatchRecipient(key)
        );
        this.recreateSelectableFilters();
      });
    this.application
      .getPlaceKeys()
      .pipe(take(1))
      .subscribe((keys) => {
        this.completePlaceFilters = keys.map((key) =>
          ResourceFilter.createMatchPlace(key)
        );
        this.recreateSelectableFilters();
      });
    this.application
      .getObjectTypeKeys()
      .pipe(take(1))
      .subscribe((keys) => {
        this.completeObjectTypeFilters = keys.map((key) =>
          ResourceFilter.createMatchObjectType(key)
        );
        this.recreateSelectableFilters();
      });
  }

  removeSpecialFilters(): void {
    const toRemove = this.filters.filter(
      (x) => x.category === ResourceFilterCategory.SPECIAL
    );
    toRemove.forEach((rem) => {
      const index = this.filters.indexOf(rem);
      this.filters.splice(index, 1);
    });
  }

  addFilter(filter: ResourceFilter): void {
    const same = this.filters.filter(
      (f) => f.category === filter.category && f.key === filter.key
    ).length;
    if (!same) {
      if (filter.category === ResourceFilterCategory.CORPUS) {
        this.removeCorpusFilter();
        this.removeSpecialFilters();
      }
      /* if (
        filter.category === ResourceFilterCategory.AUTHOR ||
        filter.category === ResourceFilterCategory.RECIPIENT
      ) {
        this.removeSpecialFilters();
      } */
      this.filters.push(filter);
      this.filters$.next(this.filters);
      this.filterResources();
      this.sortResources();
      this.paginateResources();
    }
  }

  addSpecialFilter(filter: ResourceFilter): void {
    this.clearAllFilters();
    this.filters.push(filter);
    this.filters$.next(this.filters);
    this.filterResources();
    this.sortResources();
    this.paginateResources();
  }

  removeCorpusFilter(): void {
    const toRemove = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.CORPUS
    );
    if (toRemove.length) {
      toRemove.forEach((f) => this.removeFilter(f));
    }
  }

  removeFilter(filter: ResourceFilter): void {
    const same = this.filters.filter(
      (f) => f.category === filter.category && f.key === filter.key
    );
    for (let f of same) {
      const index = this.filters.indexOf(f);
      this.filters.splice(index, 1);
    }
    this.filters$.next(this.filters);
    this.filterResources();
    this.sortResources();
    this.paginateResources();
  }

  clearAllFilters(): void {
    this.filters = [];
    this.filters$.next(this.filters);
    this.filterResources();
    this.sortResources();
    this.paginateResources();
  }

  setSortField(sortField: SearchSortField): void {
    this.sortField = sortField;
    this.sortField$.next(this.sortField);
    this.sortResources();
    this.paginateResources();
  }

  setSortAsc(asc: boolean): void {
    this.sortAsc = asc;
    this.sortAsc$.next(this.sortAsc);
    this.sortResources();
    this.paginateResources();
  }

  resetResourceList(list: ResourceFWDTO[]): void {
    this.resourceList = list;
    this.filterResources();
    this.sortResources();
    this.paginateResources();
  }

  paginateResources(): void {
    this.shownResources$.next(new PaginationInfo(this.filteredResources, 10));
  }

  createNaturalSorter(): (e1: ResourceFWDTO, e2: ResourceFWDTO) => number {
    const sortField = this.sortField ? this.sortField.sortFieldname : '';
    return (e1, e2) => {
      if (!sortField) {
        return this.resourceList.indexOf(e1) - this.resourceList.indexOf(e2);
      }
      let v1, v2;
      switch (sortField) {
        case 'DATING':
          v1 = e1.dating;
          v2 = e2.dating;
          break;
        case 'RESOURCE_ID':
          v1 = e1.id;
          v2 = e2.id;
          break;
        case 'TITLE':
          v1 = e1.title;
          v2 = e2.title;
          break;
        default:
          return this.resourceList.indexOf(e1) - this.resourceList.indexOf(e2);
      }
      if (v1 < v2) {
        return -1;
      }
      if (v1 > v2) {
        return 1;
      }
      return 0;
    };
  }

  createSorter(): (e1: ResourceFWDTO, e2: ResourceFWDTO) => number {
    if (this.sortAsc) {
      return this.createNaturalSorter();
    }
    return (e1, e2) => {
      const comp = this.createNaturalSorter();
      return -1 * comp(e1, e2);
    };
  }

  sortResources(): void {
    if (this.sortField) {
      const sorter = this.createSorter();
      this.filteredResources = this.filteredResources.sort(sorter);
    }
  }

  filterResources(): void {
    const corpusFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.CORPUS
    );
    const periodFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.TIMEPERIOD
    );
    const authorFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.AUTHOR
    );
    const recipientFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.RECIPIENT
    );
    const placeFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.PLACE
    );
    const typeFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.OBJECTTYPE
    );
    const specialFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.SPECIAL
    );
    let filtered = [...this.resourceList];
    if (specialFilters.length) {
      filtered = this.filterWithOr(filtered, specialFilters);
    }
    filtered = this.filterWithOr(filtered, corpusFilters);
    filtered = this.filterWithOr(filtered, periodFilters);
    filtered = this.filterWithOr(filtered, authorFilters);
    filtered = this.filterWithOr(filtered, recipientFilters);
    filtered = this.filterWithOr(filtered, placeFilters);
    filtered = this.filterWithOr(filtered, typeFilters);
    this.filteredResources = filtered;
    this.recreateSelectableFilters();
  }

  recreateSelectableFilters(): void {
    const corpusFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.CORPUS
    );
    const periodFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.TIMEPERIOD
    );
    const authorFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.AUTHOR
    );
    const recipientFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.RECIPIENT
    );
    const placeFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.PLACE
    );
    const typeFilters = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.OBJECTTYPE
    );
    const specialFilter = this.filters.filter(
      (f) => f.category === ResourceFilterCategory.SPECIAL
    );
    let filtered = [...this.resourceList];
    filtered = this.filterWithOr(filtered, specialFilter);
    filtered = this.filterWithOr(filtered, corpusFilters);
    const corpusFiltered = [...filtered];
    let forPeriod = [...corpusFiltered];
    forPeriod = this.filterWithOr(forPeriod, authorFilters);
    forPeriod = this.filterWithOr(forPeriod, recipientFilters);
    forPeriod = this.filterWithOr(forPeriod, placeFilters);
    forPeriod = this.filterWithOr(forPeriod, typeFilters);

    // Periods
    const periodFss: ResourceFilterInfo[] = [];
    for (let af of this.completePeriods) {
      const count = forPeriod.filter((r) => af.include(r)).length;
      if (count) {
        periodFss.push(new ResourceFilterInfo(count, af));
      }
    }
    this.periodFilters$.next(periodFss);

    const authorFss: ResourceFilterInfo[] = [];
    let forAuthor = [...corpusFiltered];
    forAuthor = this.filterWithOr(forAuthor, periodFilters);
    forAuthor = this.filterWithOr(forAuthor, recipientFilters);
    forAuthor = this.filterWithOr(forAuthor, placeFilters);
    forAuthor = this.filterWithOr(forAuthor, typeFilters);
    for (let af of this.completeAuthorFilters) {
      const count = forAuthor.filter((r) => af.include(r)).length;
      if (count) {
        authorFss.push(new ResourceFilterInfo(count, af));
      }
    }
    this.authorFilters$.next(authorFss);

    const recipientFss: ResourceFilterInfo[] = [];
    let forRecipient = [...corpusFiltered];
    forRecipient = this.filterWithOr(forRecipient, periodFilters);
    forRecipient = this.filterWithOr(forRecipient, authorFilters);
    forRecipient = this.filterWithOr(forRecipient, placeFilters);
    forRecipient = this.filterWithOr(forRecipient, typeFilters);
    for (let af of this.completeRecipientFilters) {
      const count = forRecipient.filter((r) => af.include(r)).length;
      if (count) {
        recipientFss.push(new ResourceFilterInfo(count, af));
      }
    }
    this.recipientFilters$.next(recipientFss);

    const placeFss: ResourceFilterInfo[] = [];
    let forPlace = [...corpusFiltered];
    forPlace = this.filterWithOr(forPlace, periodFilters);
    forPlace = this.filterWithOr(forPlace, authorFilters);
    forPlace = this.filterWithOr(forPlace, recipientFilters);
    forPlace = this.filterWithOr(forPlace, typeFilters);
    for (let af of this.completePlaceFilters) {
      const count = forPlace.filter((r) => af.include(r)).length;
      if (count) {
        placeFss.push(new ResourceFilterInfo(count, af));
      }
    }
    this.placeFilters$.next(placeFss);

    const typeFss: ResourceFilterInfo[] = [];
    let forType = [...corpusFiltered];
    forType = this.filterWithOr(forType, periodFilters);
    forType = this.filterWithOr(forType, authorFilters);
    forType = this.filterWithOr(forType, recipientFilters);
    forType = this.filterWithOr(forType, placeFilters);
    for (let af of this.completeObjectTypeFilters) {
      const count = forType.filter((r) => af.include(r)).length;
      if (count) {
        typeFss.push(new ResourceFilterInfo(count, af));
      }
    }
    this.objectTypeFilters$.next(typeFss);
  }

  filterWithOr(
    resources: ResourceFWDTO[],
    filters: ResourceFilter[]
  ): ResourceFWDTO[] {
    if (filters?.length && resources?.length) {
      const taken: ResourceFWDTO[] = [];
      resources.forEach((res) => {
        for (let filter of filters) {
          if (filter.include(res)) {
            taken.push(res);
            break;
          }
        }
      });
      return taken;
    } else {
      return resources;
    }
  }

  correspondencePartnerFilter(
    resources: ResourceFWDTO[],
    party1: string[],
    party2: string[]
  ) {
    if (!party1.length || !party2.length) {
      return resources;
    }
    const party1AuthorFilters = party1.map((x) =>
      ResourceFilter.createMatchAuthor(x)
    );
    const party1RecipientFilters = party1.map((x) =>
      ResourceFilter.createMatchRecipient(x)
    );
    const party2AuthorFilters = party2.map((x) =>
      ResourceFilter.createMatchAuthor(x)
    );
    const party2RecipientFilters = party2.map((x) =>
      ResourceFilter.createMatchRecipient(x)
    );

    return resources.filter((res) => {
      let resArray = [res];
      resArray = this.filterWithOr(resArray, party1AuthorFilters);
      resArray = this.filterWithOr(resArray, party2RecipientFilters);
      if (resArray.length) {
        return true;
      }
      resArray = [res];
      resArray = this.filterWithOr(resArray, party2AuthorFilters);
      resArray = this.filterWithOr(resArray, party1RecipientFilters);
      return !!resArray.length;
    });
  }
}
