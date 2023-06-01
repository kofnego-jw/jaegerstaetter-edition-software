import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-commentdoc-ref',
  templateUrl: './commentdoc-ref.component.html',
  styleUrls: ['./commentdoc-ref.component.scss'],
})
export class CommentdocRefComponent implements OnInit {
  @Input()
  target: string = '';

  type: string = 'internal';

  link: SafeUrl;

  constructor(
    private application: ApplicationService,
    private sanitizer: DomSanitizer
  ) {
    this.link = this.sanitizer.bypassSecurityTrustUrl('');
  }

  ngOnInit(): void {
    if (
      this.target.startsWith('http://') ||
      this.target.startsWith('https://')
    ) {
      this.link = this.sanitizer.bypassSecurityTrustUrl(this.target);
      this.type = 'external';
    } else {
      let myLink = this.target;
      while (myLink.startsWith('/')) {
        myLink = myLink.substring(1);
      }
      if (myLink.startsWith('biography/')) {
        myLink = myLink.replace('biography/', 'biografien/');
      }
      this.link = this.application.res_getFloatingHrefFromResourceId(myLink);
    }
  }
}
