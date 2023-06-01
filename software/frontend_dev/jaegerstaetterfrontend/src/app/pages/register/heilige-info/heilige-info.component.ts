import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RegistryEntrySaint } from 'src/app/models/dto';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-heilige-info',
  templateUrl: './heilige-info.component.html',
  styleUrls: ['./heilige-info.component.scss'],
})
export class HeiligeInfoComponent {
  faTimes = faTimes;
  @Input()
  saint: RegistryEntrySaint;

  constructor(private sanitizer: DomSanitizer, private router: Router) {}

  encyclopediaLink(): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(this.saint.encyclopediaLink);
  }

  authorityLink(link: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(link);
  }

  close(): void {
    this.router.navigate(['/register/heilige']);
  }
}
