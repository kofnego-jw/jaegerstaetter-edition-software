import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { PhotoDocumentGroup, PhotoDocumentGroupType } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-foto-dok-button',
  templateUrl: './foto-dok-button.component.html',
  styleUrls: ['./foto-dok-button.component.scss'],
})
export class FotoDokButtonComponent implements OnInit, OnDestroy {
  @Input()
  type: PhotoDocumentGroupType;

  title: string = '';

  groups: PhotoDocumentGroup[] = [];
  destroyed$: Subject<boolean> = new Subject();

  documentCount: number = 0;

  constructor(
    private application: ApplicationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.application.photoDocumentGroups$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((groups) => {
        this.groups = groups.filter((g) => g.type == this.type);
        this.documentCount = this.groups?.length
          ? this.groups.reduce((prev, curr) => prev + curr.items.length, 0)
          : 0;
      });
    if (this.type === PhotoDocumentGroupType.DOCUMENT) {
      this.title = 'Dokumente';
    } else {
      this.title = 'Fotografien';
    }
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  navigateToGallery() {
    this.router.navigate([
      '/fotografien-dokumente/' + this.type.toString().toLowerCase(),
    ]);
  }
}
