import { Component, Input, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { CorrespInfo, DocumentInfo } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-corresp-places-modal',
  templateUrl: './corresp-places-modal.component.html',
  styleUrls: ['./corresp-places-modal.component.scss'],
})
export class CorrespPlacesModalComponent {
  @Input()
  correspInfo: CorrespInfo;

  constructor(private app: ApplicationService) {}

  correspPlaceLink(info: DocumentInfo): SafeUrl {
    const id = info.filename;
    const scrollTo = info.anchorName;
    return this.app.res_getFloatingHrefFromResourceIdAndScroll(id, scrollTo);
  }

  filenameToTitle(info: DocumentInfo): string {
    return this.app.getTitleById(info.filename);
  }
}
