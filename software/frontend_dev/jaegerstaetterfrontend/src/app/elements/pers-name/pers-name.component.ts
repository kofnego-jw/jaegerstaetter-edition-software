import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { RegistryEntryPerson, RegistryEntrySaint } from 'src/app/models/dto';
import {
  AnnotationViewEnum,
  AnnotationViewService,
} from 'src/app/services/annotation-view.service';
import { ApplicationService } from 'src/app/services/application.service';
import { ModalService } from 'src/app/services/modal.service';

@Component({
  selector: 'app-pers-name',
  templateUrl: './pers-name.component.html',
  styleUrls: ['./pers-name.component.scss'],
})
export class PersNameComponent implements OnInit, OnDestroy {
  @Input()
  name: string;

  @Input()
  key: string;

  @Input()
  ref: string;

  @Input()
  targets: string;

  @Input()
  highlight: string;

  @Input()
  cert: string;

  destroyed$: Subject<boolean> = new Subject();

  myKeys: string[] = [];

  readableName: string = '';

  myTargets: RegistryEntryPerson[] = [];

  mySaintTargets: RegistryEntrySaint[] = [];

  show: boolean = true;

  constructor(
    private modal: ModalService,
    private annoationView: AnnotationViewService,
    private application: ApplicationService
  ) {
    this.setAnnotationView(annoationView.currentAnnotationView);
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    if (this.targets) {
      this.myKeys = this.targets
        .split(/\s*;\s*/)
        .map((x) => x.trim())
        .map((x) => (x.startsWith('#') ? x.substring(1) : x))
        .filter((x) => !!x);
      if (this.myKeys.length) {
        const personKeys = this.myKeys.filter((x) => !x.startsWith('H_'));
        const saintKeys = this.myKeys.filter((x) => x.startsWith('H_'));
        this.application
          .re_getPersonRegistry()
          .pipe(take(1))
          .subscribe((persons) => {
            this.myTargets = persons.filter(
              (person) => personKeys.indexOf(person.key) >= 0
            );
            if (this.myTargets.length) {
              this.readableName =
                this.application.cert_translation(this.cert) +
                this.myTargets.map((x) => x.generatedReadableName).join('; ');
            }
          });
        this.application
          .re_getSaintRegistry()
          .pipe(take(1))
          .subscribe((saints) => {
            this.mySaintTargets = saints.filter(
              (s) => saintKeys.indexOf(s.key) >= 0
            );
            if (this.mySaintTargets.length) {
              this.readableName =
                this.application.cert_translation(this.cert) +
                this.mySaintTargets.map((x) => x.generatedName).join('; ');
            }
          });
      }
    }
    this.annoationView.annotationView$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((view) => this.setAnnotationView(view));
  }

  showInfo(): void {
    if (this.myTargets.length) {
      this.modal.openBiographicalInfo(this.myTargets);
    } else if (this.mySaintTargets.length) {
      this.modal.openSaintInfo(this.mySaintTargets);
    }
  }

  setAnnotationView(view: AnnotationViewEnum): void {
    this.show = !view || view !== AnnotationViewEnum.NONE;
  }
}
