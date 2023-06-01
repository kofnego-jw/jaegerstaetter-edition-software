import { Injectable } from '@angular/core';
import {
  Biography,
  ResourceDTO,
  ResourceType,
  VersionInfo,
} from '../models/dto';
import { ApplicationService } from './application.service';

@Injectable({
  providedIn: 'root',
})
export class CitationRuleService {
  static readonly EDITION =
    'Andreas Schmoller/Verena Lorber (Hg.<sup>in</sup>), Franz und Franziska Jägerstätter Edition';

  constructor(private app: ApplicationService) {}

  getCitation(res: ResourceDTO | Biography, version: VersionInfo): string {
    let citation: string = '';
    if (res instanceof ResourceDTO) {
      citation = this.getTitleFromResource(res as ResourceDTO, version);
    } else if (res instanceof Biography) {
      citation = this.getTitleFromBiography(res as Biography, version);
    }
    return citation;
  }

  getTitleFromResource(resource: ResourceDTO, version: VersionInfo): string {
    if (!resource) {
      return '';
    }
    const date: string =
      version?.creationTimestamp?.length >= 10
        ? this.app.dateStringToDate(version.creationTimestamp.substring(0, 10))
        : '1.6.2023';
    let title = resource.title;
    if (resource.type === ResourceType.LETTER) {
      title = title.startsWith('Von ') ? title.substring(4) : title;
      return `${title}, in ${CitationRuleService.EDITION}, Version ${date}.`;
    } else {
      const sig = resource.signature ? resource.signature : 'Ohne Signatur';
      return `Von Franz Jägerstätter, ${sig}, in ${CitationRuleService.EDITION}, Version ${date}.`;
    }
  }

  getTitleFromBiography(biography: Biography, version: VersionInfo): string {
    if (!biography) {
      return '';
    }
    const date: string =
      version?.creationTimestamp?.length >= 10
        ? this.app.dateStringToDate(version.creationTimestamp.substring(0, 10))
        : '1.6.2023';
    const author: string = biography.author ? biography.author : '';
    let title = biography.title;
    return `${author}, ${title}, in ${CitationRuleService.EDITION}, Version ${date}.`;
  }
}
