import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-view-place-registry',
  templateUrl: './view-place-registry.component.html',
  styleUrls: ['./view-place-registry.component.scss'],
})
export class ViewPlaceRegistryComponent {
  @Input()
  key: string;

  constructor(private router: Router) {}

  viewPlaceRegistry(): void {
    this.router.navigate(['/register/orte'], {
      queryParams: { key: this.key },
    });
  }
}
