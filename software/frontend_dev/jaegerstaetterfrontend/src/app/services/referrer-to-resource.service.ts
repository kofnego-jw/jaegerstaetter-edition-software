import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReferrerToResourceService {
  referrer$: ReplaySubject<Referrer> = new ReplaySubject(1);

  constructor() {}

  setReferrer(text: string, routerLink: string): void {
    const next = new Referrer(text, routerLink);
    this.referrer$.next(next);
  }

  setReferrerWithQueryParam(
    text: string,
    routerLink: string,
    key: string,
    value: string
  ): void {
    const next = new Referrer(text, routerLink, key, value);
    this.referrer$.next(next);
  }

  clearReferrer(): void {
    this.referrer$.next(Referrer.EDITION);
  }
}

export class Referrer {
  static readonly EDITION = new Referrer('Gesamtverzeichnis', '/edition');
  constructor(
    public readonly name: string,
    public readonly routerLink: string,
    public readonly queryParamKey?: string,
    public readonly queryParamValue?: string
  ) {}

  isFromEdition(): boolean {
    return this.routerLink.startsWith('/edition');
  }

  isFromRegistry(): boolean {
    return this.routerLink.startsWith('/register');
  }

  isFromSearch(): boolean {
    return this.routerLink.startsWith('/suche');
  }
}
