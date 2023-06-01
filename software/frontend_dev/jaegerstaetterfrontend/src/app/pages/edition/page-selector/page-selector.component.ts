import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ResourceDTO } from 'src/app/models/dto';
import { ViewPageBreakEvent } from 'src/app/models/frontend';
import { ApplicationService } from 'src/app/services/application.service';
import { ViewPageService } from 'src/app/services/view-page.service';

@Component({
  selector: 'app-page-selector',
  templateUrl: './page-selector.component.html',
  styleUrls: ['./page-selector.component.scss'],
})
export class PageSelectorComponent implements OnInit, OnDestroy {
  currentPage: string = '';

  facsimileIds: string[] = [];

  destroyed$: Subject<boolean> = new Subject();

  constructor(
    private application: ApplicationService,
    private viewPage: ViewPageService
  ) {}

  ngOnInit(): void {
    this.application.currentResource$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((res) => this.setResource(res));

    this.viewPage.viewPage$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((ve) => this.setViewPageEvent(ve));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  setViewPageEvent(ve: ViewPageBreakEvent): void {
    if (ve) {
      this.currentPage = ve.facs;
    } else {
      this.currentPage = '';
    }
  }

  setResource(res: ResourceDTO): void {
    if (!res) {
      this.facsimileIds = [];
    } else {
      this.facsimileIds = res.facsimileIds;
    }
  }

  showPage(facs: string): void {
    this.viewPage.viewPage(
      this.facsimileIds.indexOf(facs) + 1,
      true,
      facs,
      'pageSelector'
    );
  }
}
