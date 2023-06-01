import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ResourceDTO } from 'src/app/models/dto';
import {
  BriefPanelView,
  LetterDTO,
  PanelPosition,
} from 'src/app/models/frontend';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-brief-panel',
  templateUrl: './brief-panel.component.html',
  styleUrls: ['./brief-panel.component.scss'],
})
export class BriefPanelComponent implements OnInit, OnDestroy {
  BriefPanelView = BriefPanelView;

  @Input()
  activeView: BriefPanelView;

  @Input()
  panelPosition: PanelPosition;

  resource: ResourceDTO;
  destroyed$: Subject<any> = new Subject();

  constructor(private application: ApplicationService) {}

  ngOnInit(): void {
    if (!this.activeView) {
      this.activeView = BriefPanelView.FACSIMILE;
    }
    this.application.currentResource$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((res) => this.setResource(res));
  }

  setResource(res: ResourceDTO): void {
    this.resource = res;
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }
}
