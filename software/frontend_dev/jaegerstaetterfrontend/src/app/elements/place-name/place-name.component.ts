import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { RegistryEntryPlace } from 'src/app/models/dto';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';
import { ApplicationService } from 'src/app/services/application.service';
import { ModalService } from 'src/app/services/modal.service';

@Component({
  selector: 'app-place-name',
  templateUrl: './place-name.component.html',
  styleUrls: ['./place-name.component.scss'],
})
export class PlaceNameComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  @Input()
  name: string;

  @Input()
  key: string;

  @Input()
  highlight: string;

  readableName: string = '';

  myPlace: RegistryEntryPlace | undefined = undefined;

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
        .re_getPlaceRegistry()
        .pipe(take(1))
        .subscribe((places) => {
          const found = places.filter((place) => place.key === this.key);
          if (found.length) {
            this.myPlace = found[0];
            this.readableName = this.myPlace.generatedName;
          }
        });
    }
    this.annotationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
  }

  showInfo(): void {
    if (this.myPlace) {
      this.modal.openGeographical(this.myPlace);
    }
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    this.show = !view || view !== AnnotationViewEnum.NONE;
  }
}
