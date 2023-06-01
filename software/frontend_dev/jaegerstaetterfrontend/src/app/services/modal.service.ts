import { ChangeDetectorRef, Injectable } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Observable } from 'rxjs';
import { BiographicalInfoModalComponent } from '../modals/biographical-info-modal/biographical-info-modal.component';
import { CitationModalComponent } from '../modals/citation-modal/citation-modal.component';
import { CorporationInfoModalComponent } from '../modals/corporation-info-modal/corporation-info-modal.component';
import { GeographicalInfoModalComponent } from '../modals/geographical-info-modal/geographical-info-modal.component';
import { SaintInfoModalComponent } from '../modals/saint-info-modal/saint-info-modal.component';
import { SortFieldModalComponent } from '../modals/sort-field-modal/sort-field-modal.component';
import {
  Biography,
  CorrespInfo,
  RegistryEntryCorporation,
  RegistryEntryPerson,
  RegistryEntryPlace,
  RegistryEntrySaint,
  ResourceDTO,
  VersionInfo,
} from '../models/dto';
import { LetterSorterField } from '../models/frontend';
import { CorrespPlacesModalComponent } from '../modals/corresp-places-modal/corresp-places-modal.component';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  constructor(private bsModalService: BsModalService) {}

  openSortFieldModal(
    currentSortField: LetterSorterField
  ): Observable<LetterSorterField> {
    return this.bsModalService.show(SortFieldModalComponent, {
      initialState: { currentSortField },
    }).content.selectedSortField$;
  }

  openBiographicalInfo(entries: RegistryEntryPerson[]): void {
    this.bsModalService.show(BiographicalInfoModalComponent, {
      initialState: { entries },
    });
  }

  openGeographical(entry: RegistryEntryPlace): void {
    this.bsModalService.show(GeographicalInfoModalComponent, {
      initialState: { entry },
    });
  }

  openOrganisationInfo(entry: RegistryEntryCorporation): void {
    this.bsModalService.show(CorporationInfoModalComponent, {
      initialState: { entry },
    });
  }

  openSaintInfo(entries: RegistryEntrySaint[]): void {
    this.bsModalService.show(SaintInfoModalComponent, {
      initialState: { entries },
    });
  }

  openCitationInfo(
    resource: Biography | ResourceDTO,
    version: VersionInfo
  ): void {
    const initState = { resource: resource, version: version };
    this.bsModalService.show(CitationModalComponent, {
      initialState: initState,
      animated: false,
    });
  }

  openCorrespInfo(info: CorrespInfo): void {
    const initState = { correspInfo: info };
    this.bsModalService.show(CorrespPlacesModalComponent, {
      initialState: initState,
    });
  }
}
