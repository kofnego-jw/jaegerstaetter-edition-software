import { Component, Input } from '@angular/core';
import { faAngleLeft, faAngleRight } from '@fortawesome/free-solid-svg-icons';
import {
  ResourceListCurrentState,
  ResourceListState,
} from 'src/app/models/frontend';

@Component({
  selector: 'app-resource-list-current-state-view',
  templateUrl: './resource-list-current-state-view.component.html',
  styleUrls: ['./resource-list-current-state-view.component.scss'],
})
export class ResourceListCurrentStateViewComponent {
  faAngleLeft = faAngleLeft;
  faAngleRight = faAngleRight;

  @Input()
  currentState: ResourceListCurrentState;

  @Input()
  resourceListState: ResourceListState;

  constructor() {}

  next(): void {
    if (this.resourceListState) {
      this.resourceListState.next();
    }
  }

  prev(): void {
    if (this.resourceListState) {
      this.resourceListState.prev();
    }
  }
}
