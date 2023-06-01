import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RegistryEntryCorporation } from 'src/app/models/dto';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-organisationen-info',
  templateUrl: './organisationen-info.component.html',
  styleUrls: ['./organisationen-info.component.scss'],
})
export class OrganisationenInfoComponent {
  faTimes = faTimes;
  @Input()
  corporation: RegistryEntryCorporation;

  constructor(private sanitizer: DomSanitizer, private router: Router) {}

  authorityLink(link: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(link);
  }

  close(): void {
    this.router.navigate(['/register/organisationen']);
  }
}
