import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { take } from 'rxjs/operators';
import {
  ResourceFWDTO,
  SearchHit,
  SearchRequest,
  SearchResult,
} from '../models/dto';
import {
  ResourceListCurrentState,
  ResourceListState,
} from '../models/frontend';
import { ApplicationService } from './application.service';
import { CloseMenuService } from './close-menu.service';

@Injectable({
  providedIn: 'root',
})
export class SearchStateService implements ResourceListState {
  readonly MAX_RESULT_NUMBER = 10000;

  searchResult: SearchResult = null;

  searchResult$: ReplaySubject<SearchResult> = new ReplaySubject(1);

  shownResult: SearchResult = null;

  shownResult$: ReplaySubject<SearchResult> = new ReplaySubject(1);

  resourceListState$: ReplaySubject<ResourceListCurrentState> =
    new ReplaySubject(1);

  currentHit: number;

  currentListState: ResourceListCurrentState = ResourceListCurrentState.EMPTY;

  constructor(
    private application: ApplicationService,
    private closeMenu: CloseMenuService
  ) {
    this.setSearchResult(null);
    this.application.currentResource$.subscribe((curr) =>
      this.setCurrent(curr)
    );
    this.resourceListState$.next(this.currentListState);
  }

  reset(): void {
    this.setSearchResult(null);
    this.setShownResult([]);
    this.setCurrent(null);
  }

  setCurrent(res: ResourceFWDTO): void {
    if (!res || !this.shownResult?.hits?.length) {
      this.currentHit = 0;
    } else {
      const found = this.shownResult.hits.filter(
        (re) => re.resource.id === res.id
      );
      if (found.length) {
        const f = found[0];
        this.currentHit = this.shownResult.hits.indexOf(f);
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
      'Suchergebnisliste'
    );
    this.currentListState = nextState;
    this.resourceListState$.next(this.currentListState);
  }

  getCurrNumber(): number {
    return this.currentHit;
  }

  getCurr(): ResourceFWDTO {
    if (this.shownResult?.hits?.length) {
      const found: SearchHit = this.shownResult.hits[this.currentHit];
      if (found) {
        return found.resource;
      }
    }
    return null;
  }

  getTotalCount(): number {
    if (this.shownResult) {
      return this.shownResult.totalHits;
    }
    return 0;
  }

  hasNext(): boolean {
    if (!this.shownResult) {
      return false;
    }
    return this.currentHit < this.shownResult.totalHits - 1;
  }

  next(): void {
    if (this.hasNext()) {
      const nextHitNum = this.currentHit + 1;
      const found = this.shownResult.hits[nextHitNum];
      if (!found) {
        throw new Error(
          'Inconsistent state: hasNext but cannot find the next hit.'
        );
      }
      const resource = found.resource;
      this.application.setResourceById(resource.id);
      this.closeMenu.closeMenu();
    }
  }

  hasPrev(): boolean {
    if (this.shownResult) {
      return this.currentHit > 0;
    }
    return false;
  }

  prev(): void {
    if (this.hasPrev()) {
      const prevHitNumb = this.currentHit - 1;
      const found: SearchHit = this.shownResult.hits[prevHitNumb];
      if (!found) {
        throw new Error(
          'Inconsistent state: hasPrev but cannot find the prev hit.'
        );
      }
      const resource = found.resource;
      this.application.setResourceById(resource.id);
      this.closeMenu.closeMenu();
    }
  }

  getCurrentState$(): ReplaySubject<ResourceListCurrentState> {
    return this.resourceListState$;
  }

  search(request: SearchRequest): void {
    if (!request) {
      return;
    }
    const req = new SearchRequest(
      request.queryParams,
      0,
      this.MAX_RESULT_NUMBER,
      request.sortField,
      request.sortAsc
    );
    this.application
      .se_search(request)
      .subscribe((res) => this.setSearchResult(res));
  }

  setSearchResult(res: SearchResult): void {
    this.searchResult = res;
    this.searchResult$.next(this.searchResult);
  }

  setShownResult(filteredResources: ResourceFWDTO[]): void {
    if (!this.searchResult) {
      return;
    }
    const resourceIds = filteredResources.map((res) => res.id);
    const filteredHits = this.searchResult.hits.filter((hit) =>
      resourceIds.indexOf(hit.resource.id) >= 0 ? true : false
    );
    this.shownResult = new SearchResult(
      this.searchResult.searchRequest,
      filteredResources.length,
      0,
      filteredResources.length,
      filteredHits
    );
    this.shownResult$.next(this.shownResult);
  }
}
