import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnInit,
} from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-bio-figure',
  templateUrl: './bio-figure.component.html',
  styleUrls: ['./bio-figure.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BioFigureComponent implements OnInit {
  @Input()
  src: string = '';

  @Input()
  desc: string = '';

  constructor(
    private application: ApplicationService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {}

  getSource(): SafeResourceUrl {
    const url = this.application.bg_getImageResource(this.src);
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
}
