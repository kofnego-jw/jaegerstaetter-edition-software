import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { Subject } from 'rxjs';
import { BriefPanelView } from 'src/app/models/frontend';

@Component({
  selector: 'app-view-switch',
  templateUrl: './view-switch.component.html',
  styleUrls: ['./view-switch.component.scss'],
})
export class ViewSwitchComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  BriefPanelView = BriefPanelView;

  @Output()
  activeViewChange: EventEmitter<BriefPanelView> = new EventEmitter();

  @Input()
  activeView: BriefPanelView;

  constructor() {}

  ngOnInit(): void {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  setActive(view: BriefPanelView) {
    this.activeView = view;
    this.activeViewChange.emit(this.activeView);
  }
}
