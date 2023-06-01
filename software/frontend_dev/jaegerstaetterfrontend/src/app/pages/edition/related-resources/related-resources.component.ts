import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ResourceDTO, ResourceFWDTO } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { faAngleLeft, faAngleRight } from '@fortawesome/free-solid-svg-icons';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-related-resources',
  templateUrl: './related-resources.component.html',
  styleUrls: ['./related-resources.component.scss'],
})
export class RelatedResourcesComponent implements OnInit, OnDestroy {
  faAngleLeft = faAngleLeft;
  faAngleRight = faAngleRight;

  destroyed$: Subject<boolean> = new Subject();
  currentResource: ResourceDTO = null;

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
    this.currentResource = res;
  }

  createHref(fw: ResourceFWDTO): SafeUrl {
    return this.application.res_getFloatingHrefFromResource(fw);
  }
}
