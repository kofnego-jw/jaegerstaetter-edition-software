import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { RegistryEntryCorporation } from 'src/app/models/dto';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';
import { ApplicationService } from 'src/app/services/application.service';
import { ModalService } from 'src/app/services/modal.service';

@Component({
  selector: 'app-org-name',
  templateUrl: './org-name.component.html',
  styleUrls: ['./org-name.component.scss'],
})
export class OrgNameComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();
  @Input()
  name: string;

  @Input()
  key: string;

  @Input()
  highlight: string;

  readableName: string = '';

  myOrg: RegistryEntryCorporation | undefined = undefined;

  show: boolean = true;

  constructor(
    private modal: ModalService,
    private annotationView: AnnotationViewService,
    private application: ApplicationService
  ) {
    this.setAnnotationView(annotationView.currentAnnotationView);
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    if (this.key) {
      this.application
        .re_getCorporationRegistry()
        .pipe(take(1))
        .subscribe((orgs) => {
          const found = orgs.filter((org) => org.key === this.key);
          if (found.length) {
            this.myOrg = found[0];
            this.readableName = this.myOrg.generatedName;
          }
        });
    }
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
  }

  showInfo(): void {
    if (this.myOrg) {
      this.modal.openOrganisationInfo(this.myOrg);
    }
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    this.show = !view || view !== AnnotationViewEnum.NONE;
  }
}
