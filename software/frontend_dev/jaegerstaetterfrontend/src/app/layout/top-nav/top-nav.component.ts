import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { faBars, faSearch } from '@fortawesome/free-solid-svg-icons';
import { Subject } from 'rxjs';
import { filter, take, takeUntil } from 'rxjs/operators';
import { BiographyFW, MenuItem } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { CloseMenuService } from 'src/app/services/close-menu.service';
import { ResourceService } from 'src/app/services/resource.service';

@Component({
  selector: 'app-top-nav',
  templateUrl: './top-nav.component.html',
  styleUrls: ['./top-nav.component.scss'],
})
export class TopNavComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();
  subMenuShown: string;
  isCollapsed: boolean = true;
  faBars = faBars;
  faSearch = faSearch;

  deMenuItems: MenuItem[] = [];

  bgMenuItems: BiographyFW[] = [];

  maMenuItems: MenuItem[] = [];

  constructor(
    private router: Router,
    private resourceService: ResourceService,
    private closeMenu: CloseMenuService,
    private application: ApplicationService
  ) {}

  ngOnInit(): void {
    this.subMenuShown = '';
    this.application
      .de_getMenuItems()
      .pipe(take(1))
      .subscribe((items) => (this.deMenuItems = items));
    this.application
      .bg_getBiographies()
      .pipe(take(1))
      .subscribe((biographies) => (this.bgMenuItems = biographies));
    this.application
      .ma_getMenuItems()
      .pipe(take(1))
      .subscribe((items) => (this.maMenuItems = items));
    this.closeMenu.closeMenu$
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => this.hideSubMenu());
    this.router.events
      .pipe(
        takeUntil(this.destroyed$),
        filter((ev) => ev instanceof NavigationEnd)
      )
      .subscribe(() => this.hideSubMenu());
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  hideSubMenu(): void {
    this.subMenuShown = '';
  }

  showSubMenu(submenuName: string): void {
    this.subMenuShown = submenuName;
  }

  toggleCollapsed(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  navigateToGesamtverzeichnis(): void {
    this.resourceService.clearAllFilters();
    this.router.navigate(['/edition/verzeichnis'], {
      queryParams: { corpus: 'gesamt' },
    });
  }
}
