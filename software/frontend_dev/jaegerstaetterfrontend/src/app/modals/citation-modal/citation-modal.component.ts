import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Biography, ResourceDTO, VersionInfo } from 'src/app/models/dto';
import { CitationRuleService } from 'src/app/services/citation-rule.service';

@Component({
  selector: 'app-citation-modal',
  templateUrl: './citation-modal.component.html',
  styleUrls: ['./citation-modal.component.scss'],
})
export class CitationModalComponent implements OnInit {
  static readonly DEFAULT_DATE: string = '2023-06-01';

  @Input()
  resource: ResourceDTO | Biography | undefined;

  @Input()
  version: VersionInfo = new VersionInfo(
    1,
    CitationModalComponent.DEFAULT_DATE
  );

  citation: string = '';

  constructor(
    private modalRef: BsModalRef,
    private citationRule: CitationRuleService
  ) {}

  ngOnInit(): void {
    this.citation = this.citationRule.getCitation(this.resource, this.version);
  }

  close(): void {
    this.modalRef.hide();
  }

  getLastModifiedDate(): string {
    if (this.version?.creationTimestamp?.length >= 10) {
      return this.version.creationTimestamp.substring(0, 10);
    }
    return CitationModalComponent.DEFAULT_DATE;
  }

  getUrl(): string {
    return window.location.href;
  }
}
