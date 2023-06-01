import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { take } from 'rxjs/operators';
import {
  RegistryEntryBibleBook,
  RegistryEntryCorporation,
  RegistryEntryPerson,
  RegistryEntryPlace,
  RegistryEntrySaint,
} from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { RegistryStateService } from 'src/app/services/registry-state.service';

@Component({
  selector: 'app-register-verzeichnis',
  templateUrl: './register-verzeichnis.component.html',
  styleUrls: ['./register-verzeichnis.component.scss'],
})
export class RegisterVerzeichnisComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  corporations: RegistryEntryCorporation[] = [];
  persons: RegistryEntryPerson[] = [];
  places: RegistryEntryPlace[] = [];
  saints: RegistryEntrySaint[] = [];

  bibleBooks: RegistryEntryBibleBook[] = [];

  constructor(
    private application: ApplicationService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private registryState: RegistryStateService
  ) {}

  ngOnInit(): void {
    this.application
      .re_getCorporationRegistry()
      .subscribe((list) => (this.corporations = list));
    this.application
      .re_getPersonRegistry()
      .subscribe((list) => (this.persons = list));
    this.application
      .re_getPlaceRegistry()
      .subscribe((list) => (this.places = list));
    this.application
      .re_getSaintRegistry()
      .subscribe((list) => (this.saints = list));
    this.application
      .re_getBibleRegistry()
      .subscribe((list) => (this.bibleBooks = list));
    this.activatedRoute.queryParamMap.pipe(take(1)).subscribe((paramMap) => {
      if ('true' === paramMap.get('last')) {
        this.registryState.goBack(this.router);
      }
    });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  createDropdownItems(
    list: Array<{ preferredName: string }>
  ): DropDownMenuItem[] {
    const letters = [];
    list.forEach((item) => {
      const firstLetter = item.preferredName.charAt(0).toUpperCase();
      if (letters.indexOf(firstLetter) < 0) {
        letters.push(firstLetter);
      }
    });
    letters.sort();
    return letters;
  }

  biblePositionCounter(): number {
    return this.bibleBooks.reduce((acc, cur) => cur.counter + acc, 0);
  }
}

class DropDownMenuItem {
  constructor(public letter: string) {}
}
