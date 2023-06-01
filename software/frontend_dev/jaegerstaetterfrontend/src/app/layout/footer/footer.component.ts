import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit, OnDestroy {
  isPreview: boolean = false;
  destroyed$: Subject<boolean> = new Subject();
  constructor(private application: ApplicationService) {}

  ngOnInit(): void {
    this.application.isPreview
      .pipe(takeUntil(this.destroyed$))
      .subscribe((preview) => (this.isPreview = preview));
  }
  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }
}
