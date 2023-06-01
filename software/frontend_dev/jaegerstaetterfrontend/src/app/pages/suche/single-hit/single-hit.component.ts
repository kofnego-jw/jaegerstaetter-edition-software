import { Component, Input, OnInit, Sanitizer } from '@angular/core';
import { Router } from '@angular/router';
import { SearchHit } from 'src/app/models/dto';
import { ReferrerToResourceService } from 'src/app/services/referrer-to-resource.service';

@Component({
  selector: 'app-single-hit',
  templateUrl: './single-hit.component.html',
  styleUrls: ['./single-hit.component.scss'],
})
export class SingleHitComponent implements OnInit {
  @Input()
  hit: SearchHit;

  constructor(
    private referrerService: ReferrerToResourceService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  viewResource(): void {
    this.referrerService.setReferrer('Suche', '/suche');
    this.router.navigate([this.viewResourceUrl()]);
  }

  viewResourceUrl(): string {
    return '/edition/view/' + this.hit.resource.id;
  }
}
