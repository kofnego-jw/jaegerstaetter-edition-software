import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  SearchField,
  SearchFieldParam,
  SearchHit,
  SearchOccur,
  SearchRequest,
  SearchResult,
  SearchSortField,
} from 'src/app/models/dto';
import { PaginationInfo } from 'src/app/models/frontend';
import { ApplicationService } from 'src/app/services/application.service';
import { SearchStateService } from 'src/app/services/search-state.service';

@Component({
  selector: 'app-suche',
  templateUrl: './suche.component.html',
  styleUrls: ['./suche.component.scss'],
})
export class SucheComponent implements OnInit, OnDestroy {
  isPreview: boolean = false;
  SearchType = SearchType;
  sortFields: SearchSortField[] = SearchSortField.SORT_FIELDS;
  sortField: SearchSortField = SearchSortField.BY_RELEVANCE;
  destroyed$: Subject<boolean> = new Subject();
  simpleSearchForm: FormGroup;
  searchResult: SearchResult;
  shownHits: SearchHit[] = [];
  pagedHits: SearchHit[] = [];
  paginationInfo: PaginationInfo<SearchHit> = new PaginationInfo([], 20);
  sortAsc: boolean;
  searchType: SearchType = SearchType.FULLTEXT;

  pageNumber: number = 0;
  pageSize: number = 20;
  totalHits: number = 0;
  maxPageCount: number = 0;
  fromNumber: number = 0;
  toNumber: number = 0;

  constructor(
    private app: ApplicationService,
    private searchState: SearchStateService,
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
    this.resetSimpleSearchForm();
  }

  ngOnInit(): void {
    this.searchState.searchResult$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((result) => this.setSearchResult(result));
    this.searchState.shownResult$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((result) => this.setShownResult(result));
    this.app.isPreview
      .pipe(takeUntil(this.destroyed$))
      .subscribe((preview) => (this.isPreview = preview));
    this.activatedRoute.queryParamMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((params) => {
        const q = params.get('q');
        if (q && !this.simpleSearchForm.get('simpleSearch').value) {
          this.simpleSearchForm.get('simpleSearch').setValue(q);
          this.performSimpleSearch();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }

  setPage(pn: number): void {
    this.pageNumber = pn;
    this.pageResults();
  }

  search(request: SearchRequest): void {
    this.searchType = SearchType.FULLTEXT;
    this.searchState.search(request);
  }

  setShownResult(result: SearchResult): void {
    this.shownHits = result.hits;
    this.paginationInfo = new PaginationInfo(this.shownHits, 20);
    this.pageNumber = 0;
    this.pageResults();
  }

  pageResults(): void {
    this.pagedHits = this.paginationInfo.getPage(this.pageNumber);
    this.totalHits = this.paginationInfo.totalHits();
    this.maxPageCount = this.paginationInfo.getPageCount();
    this.fromNumber = this.paginationInfo.getHitFrom();
    this.toNumber = this.paginationInfo.getHitTo();
  }

  setSearchResult(result: SearchResult): void {
    this.shownHits = [];
    this.pagedHits = [];
    this.searchResult = result;
    if (!result) {
      return;
    }
    if (
      !this.simpleSearchForm.get('simpleSearch').value &&
      result.searchRequest.queryParams.length
    ) {
      this.simpleSearchForm
        .get('simpleSearch')
        .setValue(this.searchResult.searchRequest.queryParams[0].queryString);
    }
    if (!this.searchResult.searchRequest.sortField) {
      this.sortField = SearchSortField.BY_RELEVANCE;
      this.sortAsc = false;
    } else {
      const fields = SearchSortField.SORT_FIELDS.filter(
        (f) => f.sortFieldname === this.searchResult.searchRequest.sortField
      );
      if (fields.length) {
        this.sortField = fields[0];
        this.sortAsc = this.searchResult.searchRequest.sortAsc;
      } else {
        this.sortField = SearchSortField.BY_RELEVANCE;
        this.sortAsc = false;
      }
    }
  }

  resetSimpleSearchForm(): void {
    this.simpleSearchForm = this.formBuilder.group({
      simpleSearch: [],
    });
  }

  performSimpleSearch(): void {
    const queryString = this.simpleSearchForm.get('simpleSearch').value;
    if (queryString) {
      const request = new SearchRequest(
        [
          new SearchFieldParam(
            SearchField.ALL,
            queryString,
            SearchOccur.SHOULD
          ),
        ],
        0,
        10000,
        null,
        true
      );
      this.searchState.search(request);
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: { q: queryString },
        queryParamsHandling: 'merge',
      });
    }
  }

  setSort(sort: SearchSortField): void {
    const request = this.searchResult.searchRequest;
    const nextRequest = new SearchRequest(
      request.queryParams,
      request.pageNumber,
      request.pageSize,
      sort.sortFieldname,
      request.sortAsc
    );
    this.search(nextRequest);
  }

  setSortDir(dir: boolean): void {
    const request = this.searchResult.searchRequest;
    const nextRequest = new SearchRequest(
      request.queryParams,
      request.pageNumber,
      request.pageSize,
      request.sortField,
      dir
    );
    this.search(nextRequest);
  }

  toggleSignatureSynopsis(): void {
    if (this.searchType === SearchType.FULLTEXT) {
      this.searchType = SearchType.SIGNATURE;
    } else {
      this.searchType = SearchType.FULLTEXT;
    }
  }
}

enum SearchType {
  FULLTEXT,
  SIGNATURE,
}
