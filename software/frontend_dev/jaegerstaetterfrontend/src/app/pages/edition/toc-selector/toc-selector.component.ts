import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ResourceDTO, ResourceType, TocList } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-toc-selector',
  templateUrl: './toc-selector.component.html',
  styleUrls: ['./toc-selector.component.scss'],
})
export class TocSelectorComponent implements OnInit, OnDestroy {
  showToc: boolean = false;

  destroyed$: Subject<boolean> = new Subject();

  tocList: TocList = new TocList([]);

  constructor(private application: ApplicationService) {}

  ngOnInit(): void {
    this.application.currentResource$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((res) => this.setResource(res));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  setResource(res: ResourceDTO): void {
    if (res.type === ResourceType.DOCUMENT) {
      this.tocList = res.tocList;
    } else {
      this.tocList = new TocList([]);
    }
  }
}
