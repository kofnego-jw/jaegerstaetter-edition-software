import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { ResourceFWDTO, SearchResult } from 'src/app/models/dto';
import {
  ResourceFilter,
  ResourceFilterCategory,
  ResourceFilterInfo,
} from 'src/app/models/frontend';
import { ResourceService } from 'src/app/services/resource.service';
import { SearchStateService } from 'src/app/services/search-state.service';

@Component({
  selector: 'app-search-hit-filter-control',
  templateUrl: './search-hit-filter-control.component.html',
  styleUrls: ['./search-hit-filter-control.component.scss'],
})
export class SearchHitFilterControlComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  searchResult: SearchResult;

  Category = ResourceFilterCategory;

  periodFilters: ResourceFilterInfo[] = [];
  authorFilters: ResourceFilterInfo[] = [];
  recipientFilters: ResourceFilterInfo[] = [];
  placeFilters: ResourceFilterInfo[] = [];
  objectTypeFilters: ResourceFilterInfo[] = [];

  filters: ResourceFilter[] = [];

  completePeriods: ResourceFilter[] = [...ResourceFilter.PERIOD_FILTERS];
  completeAuthorFilters: ResourceFilter[] = [];
  completeRecipientFilters: ResourceFilter[] = [];
  completePlaceFilters: ResourceFilter[] = [];
  completeObjectTypeFilters: ResourceFilter[] = [];

  constructor(
    private searchState: SearchStateService,
    private resource: ResourceService
  ) {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }

  ngOnInit(): void {
    this.filterResources();
    this.searchState.searchResult$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((result) => this.setSearchResult(result));
    this.resource.filters$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((fs) => this.setFilters(fs));
  }

  setFilters(filters: ResourceFilter[]): void {
    this.filters = filters;
    this.filterResources();
  }

  setSearchResult(result: SearchResult): void {
    this.searchResult = result;
    this.filterResources();
  }

  emitResources(resources: ResourceFWDTO[]): void {
    this.searchState.setShownResult(resources);
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

  recreateSelectableFilters(): void {
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
    let filtered = this.searchResult?.hits?.length
      ? this.searchResult.hits.map((h) => h.resource)
      : [];
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
    this.periodFilters = periodFss;

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
    this.authorFilters = authorFss;

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
    this.recipientFilters = recipientFss;

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
    this.placeFilters = placeFss;

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
    this.objectTypeFilters = typeFss;
  }

  filterResources(): void {
    const hits = this.searchResult?.hits?.length
      ? this.searchResult.hits.map((h) => h.resource)
      : [];
    const periodsInResult = [];
    const authorsInResult = [];
    const recipientsInResult = [];
    const placesInResult = [];
    const typesInResult = [];
    for (let hit of hits) {
      if (hit.periods?.length) {
        for (let p of hit.periods) {
          if (!periodsInResult.includes(p)) {
            periodsInResult.push(p);
          }
        }
      }
      if (hit.authors?.length) {
        for (let a of hit.authors) {
          if (!authorsInResult.includes(a)) {
            authorsInResult.push(a);
          }
        }
      }
      if (hit.recipients?.length) {
        for (let r of hit.recipients) {
          if (!recipientsInResult.includes(r)) {
            recipientsInResult.push(r);
          }
        }
      }
      if (hit.places?.length) {
        for (let p of hit.places) {
          if (!placesInResult.includes(p)) {
            placesInResult.push(p);
          }
        }
      }
      if (hit.objectTypes?.length) {
        for (let t of hit.objectTypes) {
          if (!typesInResult.includes(t)) {
            typesInResult.push(t);
          }
        }
      }
    }
    this.completePeriods = [];
    if (periodsInResult.includes('EARLY')) {
      this.completePeriods.push(ResourceFilter.TAKE_EARLY);
    }
    if (periodsInResult.includes('MILITARY')) {
      this.completePeriods.push(ResourceFilter.TAKE_MILITARY);
    }
    if (periodsInResult.includes('TO_REFUSAL')) {
      this.completePeriods.push(ResourceFilter.TAKE_TO_REFUSAL);
    }
    if (periodsInResult.includes('PRISON')) {
      this.completePeriods.push(ResourceFilter.TAKE_PRISON);
    }
    if (periodsInResult.includes('AFTERLIFE')) {
      this.completePeriods.push(ResourceFilter.TAKE_AFTERLIFE);
    }
    this.completeAuthorFilters = authorsInResult.map(
      (a) =>
        new ResourceFilter(ResourceFilterCategory.AUTHOR, a, (fw) =>
          fw.authors.includes(a)
        )
    );
    this.completeRecipientFilters = recipientsInResult.map(
      (r) =>
        new ResourceFilter(ResourceFilterCategory.RECIPIENT, r, (fw) =>
          fw.recipients.includes(r)
        )
    );
    this.completePlaceFilters = placesInResult.map(
      (p) =>
        new ResourceFilter(ResourceFilterCategory.PLACE, p, (fw) =>
          fw.places.includes(p)
        )
    );
    this.completeObjectTypeFilters = typesInResult.map(
      (t) =>
        new ResourceFilter(ResourceFilterCategory.OBJECTTYPE, t, (fw) =>
          fw.objectTypes.includes(t)
        )
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
    let filtered = [...hits];
    filtered = this.filterWithOr(filtered, periodFilters);
    filtered = this.filterWithOr(filtered, authorFilters);
    filtered = this.filterWithOr(filtered, recipientFilters);
    filtered = this.filterWithOr(filtered, placeFilters);
    filtered = this.filterWithOr(filtered, typeFilters);
    this.emitResources(filtered);
    this.recreateSelectableFilters();
  }

  addFilter(filter: ResourceFilter): void {
    const same = this.filters.filter(
      (f) => f.category === filter.category && f.key === filter.key
    ).length;
    if (!same) {
      this.filters.push(filter);
      this.filterResources();
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
    this.filterResources();
  }

  clearAllFilters(): void {
    this.filters = [];
    this.filterResources();
  }
}
