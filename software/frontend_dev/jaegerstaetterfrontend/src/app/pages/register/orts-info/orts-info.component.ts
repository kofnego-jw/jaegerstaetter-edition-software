import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RegistryEntryPlace } from 'src/app/models/dto';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-orts-info',
  templateUrl: './orts-info.component.html',
  styleUrls: ['./orts-info.component.scss'],
})
export class OrtsInfoComponent {
  faTimes = faTimes;

  @Input()
  place: RegistryEntryPlace;

  constructor(private sanitizer: DomSanitizer, private router: Router) {}

  authorityLink(link: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(link);
  }

  close(): void {
    this.router.navigate(['/register/orte']);
  }
}
