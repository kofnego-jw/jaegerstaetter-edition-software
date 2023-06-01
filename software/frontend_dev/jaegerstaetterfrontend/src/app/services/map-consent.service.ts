import { Injectable } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Observable, ReplaySubject, Subject, map } from 'rxjs';
import { MapConsentModalComponent } from '../modals/map-consent-modal/map-consent-modal.component';

@Injectable({
  providedIn: 'root',
})
export class MapConsentService {
  consentInMap: ConsentInMapObject | undefined;

  constructor(private bsModalService: BsModalService) {
    this.loadConsentInMap();
  }

  loadConsentInMap(): void {
    const consentString = localStorage.getItem('consentInMap');
    const consent: ConsentInMapObject = consentString
      ? JSON.parse(consentString)
      : null;
    if (consent) {
      this.consentInMap = new ConsentInMapObject(
        consent.consent,
        consent.expireDate
      );
      if (this.consentInMap.isExpired()) {
        this.consentInMap = undefined;
      }
    }
  }

  hasConsentInMap(): Observable<boolean> {
    if (!this.consentInMap || this.consentInMap.isExpired()) {
      const component = this.bsModalService.show(MapConsentModalComponent);
      return component.content.onMapConsentSelect$.pipe(
        map((consent) => this.setConsentInMap(consent === 'true'))
      );
    }
    return new Observable<boolean>((observer) => {
      observer.next(this.consentInMap.consent);
      observer.complete();
    });
  }

  revoke(): Observable<boolean> {
    localStorage.removeItem('consentInMap');
    this.consentInMap = undefined;
    return this.hasConsentInMap();
  }

  setConsentInMap(consent: boolean): boolean {
    const obj = new ConsentInMapObject(
      consent,
      new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 30)
    );
    localStorage.setItem('consentInMap', JSON.stringify(obj));
    this.consentInMap = obj;
    return consent;
  }
}

class ConsentInMapObject {
  constructor(public consent: boolean, public expireDate: Date) {}
  isExpired(): boolean {
    return this.expireDate < new Date();
  }
}
