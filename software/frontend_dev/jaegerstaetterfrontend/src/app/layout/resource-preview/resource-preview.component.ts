import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ResourceFWDTO } from 'src/app/models/dto';
import { ReferrerToResourceService } from 'src/app/services/referrer-to-resource.service';

@Component({
  selector: 'app-resource-preview',
  templateUrl: './resource-preview.component.html',
  styleUrls: ['./resource-preview.component.scss'],
})
export class ResourcePreviewComponent {
  @Input()
  resource: ResourceFWDTO;

  constructor(
    private referrer: ReferrerToResourceService,
    private router: Router
  ) {}

  navigateTo(): void {
    this.referrer.clearReferrer();
    this.router.navigate([this.viewResourceUrl()]);
  }

  viewResourceUrl(): string {
    return '/edition/view/' + this.resource.id;
  }
}
