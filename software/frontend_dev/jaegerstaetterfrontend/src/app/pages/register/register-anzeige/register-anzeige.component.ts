import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ResourceFWDTO } from 'src/app/models/dto';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { RegistryStateService } from 'src/app/services/registry-state.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-anzeige',
  templateUrl: './register-anzeige.component.html',
  styleUrls: ['./register-anzeige.component.scss'],
})
export class RegisterAnzeigeComponent {
  faArrowRight = faArrowRight;

  @Input()
  resources: ResourceFWDTO[];

  @Output()
  viewRegistry: EventEmitter<ResourceFWDTO> = new EventEmitter();

  constructor(private router: Router) {}

  viewResource(resource: ResourceFWDTO): void {
    this.viewRegistry.emit(resource);
    this.router.navigate(['/edition/view/' + resource.id], {
      queryParams: { from: 'register' },
    });
  }
}
