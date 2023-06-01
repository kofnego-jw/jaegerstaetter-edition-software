import {
  Component,
  Input,
  OnDestroy,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import {
  faChevronDown,
  faChevronUp,
  faSquare,
  faSquareCheck,
} from '@fortawesome/free-solid-svg-icons';
import { Subject, takeUntil } from 'rxjs';
import {
  ResourceFilterCategory,
  ResourceFilterInfo,
  ResourceFilter,
} from 'src/app/models/frontend';
import { ResourceService } from 'src/app/services/resource.service';

@Component({
  selector: 'app-search-filter-controller',
  templateUrl: './search-filter-controller.component.html',
  styleUrls: ['./search-filter-controller.component.scss'],
})
export class SearchFilterControllerComponent implements OnInit, OnDestroy {
  faChevronDown = faChevronDown;
  faChevronUp = faChevronUp;
  faSquareCheck = faSquareCheck;
  faSquare = faSquare;
  destroyed$: Subject<boolean> = new Subject();

  @Input()
  title: string;

  @Input()
  category: ResourceFilterCategory;

  @Input()
  filters: ResourceFilterInfo[];

  resourceFilters: ResourceFilter[] = [];

  activatedFilters: ResourceFilter[] = [];

  isCollapsed: boolean = true;

  constructor(private resource: ResourceService) {}

  ngOnInit(): void {
    this.resource.filters$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((fs) => this.processSelectedFilters(fs));
    this.resourceFilters = this.filters?.length
      ? this.filters.map((f) => f.filter)
      : [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.resourceFilters = this.filters.map((f) => f.filter);
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  processSelectedFilters(fs: ResourceFilter[]): void {
    const activatedFilters: ResourceFilter[] = [];
    fs.forEach((f) => {
      if (f.category === this.category) {
        activatedFilters.push(f);
      }
    });
    this.activatedFilters = activatedFilters;
  }

  isActivated(f: ResourceFilter): boolean {
    return this.activatedFilters.indexOf(f) >= 0;
  }

  toggleFilter(f: ResourceFilter): void {
    if (this.isActivated(f)) {
      this.resource.removeFilter(f);
    } else {
      this.resource.addFilter(f);
    }
  }
}
