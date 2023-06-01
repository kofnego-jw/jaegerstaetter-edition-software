import { Component, Input } from '@angular/core';
import { PersonInfo } from 'src/app/models/frontend';

@Component({
  selector: 'app-pers-name-wrapper',
  templateUrl: './pers-name-wrapper.component.html',
  styleUrls: ['./pers-name-wrapper.component.scss'],
})
export class PersNameWrapperComponent {
  @Input()
  person: PersonInfo;

  @Input()
  last: boolean;

  constructor() {}
}
