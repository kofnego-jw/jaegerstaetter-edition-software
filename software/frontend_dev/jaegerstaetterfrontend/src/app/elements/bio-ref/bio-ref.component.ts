import { Component, Input, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-bio-ref',
  templateUrl: './bio-ref.component.html',
  styleUrls: ['./bio-ref.component.scss'],
})
export class BioRefComponent implements OnInit {
  @Input()
  target: string = '';

  constructor(private application: ApplicationService) {}

  ngOnInit(): void {}

  getResourceLink(): SafeUrl {
    return this.application.res_getFloatingHrefFromResourceId(this.target);
  }
}
