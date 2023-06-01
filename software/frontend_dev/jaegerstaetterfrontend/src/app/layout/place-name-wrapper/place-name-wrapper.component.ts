import { Component, Input, OnInit } from '@angular/core';
import { PlaceInfo } from 'src/app/models/frontend';

@Component({
  selector: 'app-place-name-wrapper',
  templateUrl: './place-name-wrapper.component.html',
  styleUrls: ['./place-name-wrapper.component.scss'],
})
export class PlaceNameWrapperComponent {
  @Input()
  place: PlaceInfo;

  @Input()
  last: boolean;

  constructor() {}
}
