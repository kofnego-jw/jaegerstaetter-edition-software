import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { PhotoDocumentGroup } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-fotografie-dokumente',
  templateUrl: './fotografie-dokumente.component.html',
  styleUrls: ['./fotografie-dokumente.component.scss'],
})
export class FotografieDokumenteComponent implements OnInit, OnDestroy {
  destroyed$: Subject<any> = new Subject();

  groups: PhotoDocumentGroup[] = [];

  constructor(private application: ApplicationService) {}

  ngOnInit(): void {
    this.application.photoDocumentGroups$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((groups) => (this.groups = groups));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }
}
