import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeHtml, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { RegistryEntrySaint } from 'src/app/models/dto';

@Component({
  selector: 'app-saint-info-modal',
  templateUrl: './saint-info-modal.component.html',
  styleUrls: ['./saint-info-modal.component.scss'],
})
export class SaintInfoModalComponent implements OnInit {
  @Input()
  entries: RegistryEntrySaint[] = [];

  constructor(
    public modalRef: BsModalRef,
    private sanitizer: DomSanitizer,
    private router: Router
  ) {}

  ngOnInit(): void {}

  safeUrl(link: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(link);
  }

  addZeroWidthSpaces(link: string): SafeHtml {
    if (!link) {
      return this.sanitizer.bypassSecurityTrustHtml('');
    }
    return this.sanitizer.bypassSecurityTrustHtml(
      link.replace(/\//g, '/&#x200b;')
    );
  }

  gotoRegistry(entry: RegistryEntrySaint): void {
    this.modalRef.hide();
    this.router.navigate(['/register/heilige'], {
      queryParams: { key: entry.key },
    });
  }
}
