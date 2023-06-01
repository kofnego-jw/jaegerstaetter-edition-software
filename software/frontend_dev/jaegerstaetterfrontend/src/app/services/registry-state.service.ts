import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ReplaySubject } from 'rxjs';
import { ResourceFWDTO } from '../models/dto';
import {
  ResourceListCurrentState,
  ResourceListState,
} from '../models/frontend';
import { ApplicationService } from './application.service';
import { CloseMenuService } from './close-menu.service';

@Injectable({
  providedIn: 'root',
})
export class RegistryStateService implements ResourceListState {
  currentState$: ReplaySubject<ResourceListCurrentState> = new ReplaySubject(1);
  currentState: ResourceListCurrentState = ResourceListCurrentState.EMPTY;

  currentHit: number = 0;

  resourceList: ResourceFWDTO[] = [];

  registryType: string;

  registryKey: string;

  preferredName: string;

  constructor(
    private application: ApplicationService,
    private closeMenu: CloseMenuService
  ) {
    this.application.currentResource$.subscribe((cur) => this.setCurrent(cur));
  }

  goBack(router: Router): void {
    if (this.registryType && this.registryKey) {
      router.navigate(['/register/' + this.registryType], {
        queryParams: { key: this.registryKey },
      });
    }
  }

  setResourceList(
    list: ResourceFWDTO[],
    type: string,
    key: string,
    preferredName: string
  ) {
    this.resourceList = list;
    this.registryType = type;
    this.registryKey = key;
    this.preferredName = preferredName;
  }

  setCurrent(res: ResourceFWDTO): void {
    if (!res || !this.resourceList.length) {
      this.currentHit = 0;
    } else {
      const found = this.resourceList.filter((re) => re.id === res.id);
      if (found.length) {
        const f = found[0];
        this.currentHit = this.resourceList.indexOf(f);
      }
    }
    this.createNextResourceListCurrentState();
  }

  createNextResourceListCurrentState(): void {
    const backtitle = 'Registereintrag: ' + this.preferredName;
    const nextState = new ResourceListCurrentState(
      this.getTotalCount(),
      this.currentHit,
      this.hasPrev(),
      this.hasNext(),
      this.preferredName
    );
    this.currentState = nextState;
    this.currentState$.next(this.currentState);
  }

  getCurrNumber(): number {
    return this.currentHit;
  }

  getCurr(): ResourceFWDTO {
    if (
      this.resourceList.length &&
      this.resourceList.length > this.currentHit
    ) {
      return this.resourceList[this.currentHit];
    }
    return null;
  }

  getTotalCount(): number {
    if (this.resourceList.length) {
      return this.resourceList.length;
    }
    return 0;
  }

  hasNext(): boolean {
    return this.currentHit < this.resourceList.length - 1;
  }

  next(): void {
    if (this.hasNext()) {
      const nextHitNum = this.currentHit + 1;
      const resource = this.resourceList[nextHitNum];
      if (!resource) {
        throw new Error('Cannot find next.');
      }
      this.application.setResourceById(resource.id);
      this.closeMenu.closeMenu();
    }
  }

  hasPrev(): boolean {
    if (this.resourceList.length) {
      return this.currentHit > 0;
    }
    return false;
  }

  prev(): void {
    if (this.hasPrev()) {
      const prevHitNumb = this.currentHit - 1;
      const resource = this.resourceList[prevHitNumb];
      this.application.setResourceById(resource.id);
      this.closeMenu.closeMenu();
    }
  }

  getCurrentState$(): ReplaySubject<ResourceListCurrentState> {
    return this.currentState$;
  }
}
