import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RegistryEntryBiblePosition, ResourceFWDTO } from 'src/app/models/dto';

@Component({
  selector: 'app-bibelstellen-info',
  templateUrl: './bibelstellen-info.component.html',
  styleUrls: ['./bibelstellen-info.component.scss'],
})
export class BibelstellenInfoComponent {
  @Input()
  entry: RegistryEntryBiblePosition;

  @Output()
  view: EventEmitter<RegistryEntryBiblePosition> = new EventEmitter();

  constructor() {}

  onView(res: ResourceFWDTO): void {
    this.view.emit(this.entry);
  }
}
