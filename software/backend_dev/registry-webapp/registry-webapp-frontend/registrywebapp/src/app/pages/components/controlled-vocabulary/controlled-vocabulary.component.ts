import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { AuthorityUtil, ControlledVocabulary } from 'src/app/model';

@Component({
  selector: 'app-controlled-vocabulary',
  templateUrl: './controlled-vocabulary.component.html',
  styleUrls: ['./controlled-vocabulary.component.scss'],
})
export class ControlledVocabularyComponent implements OnInit {
  @Input()
  controlledVocabulary: ControlledVocabulary;

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit(): void {}

  controlledLink(): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(
      AuthorityUtil.getUrl(this.controlledVocabulary)
    );
  }
}
