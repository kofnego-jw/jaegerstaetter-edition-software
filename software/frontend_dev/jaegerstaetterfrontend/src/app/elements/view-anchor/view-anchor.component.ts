import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-view-anchor',
  templateUrl: './view-anchor.component.html',
  styleUrls: ['./view-anchor.component.scss'],
})
export class ViewAnchorComponent implements OnInit, OnDestroy {
  @Input()
  aim: string;

  highlight: boolean = false;

  destroyed$: Subject<boolean> = new Subject();

  constructor(private scrollIntoView: ScrollIntoViewService) {}

  ngOnInit(): void {
    this.scrollIntoView.highlightId$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((id) => {
        this.highlight = this.aim === id;
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }

  viewDestination(): void {
    this.scrollIntoView.view(this.aim);
  }
}
