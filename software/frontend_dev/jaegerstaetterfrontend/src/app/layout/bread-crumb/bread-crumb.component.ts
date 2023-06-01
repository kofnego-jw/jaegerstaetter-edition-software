import { Component, OnDestroy, OnInit } from '@angular/core';
import { Event, NavigationEnd, Router } from '@angular/router';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  Referrer,
  ReferrerToResourceService,
} from 'src/app/services/referrer-to-resource.service';

@Component({
  selector: 'app-bread-crumb',
  templateUrl: './bread-crumb.component.html',
  styleUrls: ['./bread-crumb.component.scss'],
})
export class BreadCrumbComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();
  faHome = faHome;
  crumbPieces: NavState[] = [];
  show: boolean = false;
  referrer: Referrer = Referrer.EDITION;

  constructor(
    private router: Router,
    private referrerService: ReferrerToResourceService
  ) {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }

  ngOnInit(): void {
    this.referrerService.referrer$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((ref) => (this.referrer = ref));
    this.router.events
      .pipe(takeUntil(this.destroyed$))
      .subscribe((event) => this.useEvents(event));
  }

  useEvents(event: Event) {
    if (event instanceof NavigationEnd) {
      const url = this.router.url;
      this.show = url !== '/startseite';
      if (this.show) {
        this.parseUrlForSegments(url);
      }
    }
  }

  parseUrlForSegments(url: string): void {
    const pieces = [];
    let myUrl = url;
    let query = '';
    if (myUrl.indexOf('?') >= 0) {
      myUrl = myUrl.substring(0, myUrl.indexOf('?'));
      query = url.substring(url.indexOf('?'));
    }
    if (myUrl.startsWith('/')) {
      myUrl = myUrl.substring(1);
    }
    const segments = myUrl.split(/\//g);
    if (
      segments[0] === 'edition' &&
      segments.length === 2 &&
      query.indexOf('partners1') >= 0
    ) {
      pieces.push(
        new NavState(
          'Ausgewählte Briefwechsel',
          '/edition/ausgewaehlte-briefwechsel'
        )
      );
    } else if (segments[0] === 'edition' && segments.length > 2) {
      if (this.referrer.queryParamKey) {
        pieces.push(
          new NavState(
            this.referrer.name,
            this.referrer.routerLink,
            this.referrer.queryParamKey,
            this.referrer.queryParamValue
          )
        );
      } else {
        pieces.push(new NavState(this.referrer.name, this.referrer.routerLink));
      }
    } else if (segments[0] === 'register' && segments.length > 1) {
      pieces.push(new NavState('Register', '/register'));
      if (query.length) {
        switch (segments[1]) {
          case 'personen':
            pieces.push(new NavState('Personen', '/register/personen'));
            break;
          case 'orte':
            pieces.push(new NavState('Orte', '/register/orte'));
            break;
          case 'organisationen':
            pieces.push(
              new NavState('Organisationen', '/register/organisationen')
            );
            break;
          case 'heilige':
            pieces.push(new NavState('Heilige', '/register/heilige'));
            break;
          case 'bibelstellen':
            pieces.push(new NavState('Bibelstellen', '/register/bibelstellen'));
            break;
        }
      }
    } else if (segments[0] === 'fotografien-dokumente') {
      if (segments[1] !== 'verzeichnis') {
        pieces.push(
          new NavState('Fotografien und Dokumente', '/fotografien-dokumente')
        );
        if (segments.length > 2) {
          switch (segments[1]) {
            case 'photo':
              pieces.push(
                new NavState('Fotos', '/fotografien-dokumente/photo')
              );
              break;
            case 'document':
              pieces.push(
                new NavState('Dokumente', '/fotografien-dokumente/document')
              );
              break;
          }
        }
      }
    }
    this.crumbPieces = pieces;
  }

  navigateTo(piece: NavState): void {
    if (piece.queryKey) {
      const param = new Object();
      param[piece.queryKey] = piece.queryValue;
      this.router.navigate([piece.routerLink], { queryParams: param });
    } else {
      this.router.navigate([piece.routerLink]);
    }
  }
}

class NavState {
  constructor(
    public navText: string,
    public routerLink: string,
    public queryKey?: string,
    public queryValue?: string
  ) {}
}
