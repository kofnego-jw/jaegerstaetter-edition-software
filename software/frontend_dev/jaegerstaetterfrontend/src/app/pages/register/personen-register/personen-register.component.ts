import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import {
  debounceTime,
  distinctUntilChanged,
  take,
  takeUntil,
} from 'rxjs/operators';
import {
  CommentDoc,
  IndexEntryPerson,
  RegistryEntryPerson,
  RegistryType,
} from 'src/app/models/dto';
import {
  RegistryEntryView,
  RegistryEntryViewGroup,
  RegistryHelper,
} from 'src/app/models/helper';
import { ApplicationService } from 'src/app/services/application.service';
import { ReferrerToResourceService } from 'src/app/services/referrer-to-resource.service';
import { RegistryStateService } from 'src/app/services/registry-state.service';

@Component({
  selector: 'app-personen-register',
  templateUrl: './personen-register.component.html',
  styleUrls: ['./personen-register.component.scss'],
})
export class PersonenRegisterComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  selectedLetter: string = '';

  selectedKey: string = '';

  personIndex: IndexEntryPerson = null;

  persons: RegistryEntryPerson[] = [];

  viewPersons: RegistryEntryView[] = [];

  viewGroups: RegistryEntryViewGroup[] = [];

  filterStringInput: FormControl = new FormControl<string | null>('');

  registryDocContent: SafeHtml = null;

  constructor(
    private application: ApplicationService,
    private activatedRoute: ActivatedRoute,
    private registryState: RegistryStateService,
    private referrerService: ReferrerToResourceService,
    private sanitizer: DomSanitizer,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.queryParamMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((paramMap) => {
        if (paramMap?.get('select')) {
          this.selectedLetter = paramMap.get('select');
        }
        if (paramMap?.get('key')) {
          const newKey = paramMap.get('key');
          if (newKey !== this.selectedKey) {
            this.viewRegistryByKey(newKey);
          }
        } else {
          this.personIndex = null;
          this.selectedKey = '';
        }
      });

    this.filterStringInput.valueChanges
      .pipe(
        takeUntil(this.destroyed$),
        debounceTime(1000),
        distinctUntilChanged()
      )
      .subscribe(() => this.createViewGroups());

    this.application
      .re_getPersonRegistry()
      .pipe(takeUntil(this.destroyed$))
      .subscribe((entries) => this.setPersonEntries(entries));

    this.application
      .re_getRegistryDoc(RegistryType.PERSON)
      .pipe(takeUntil(this.destroyed$))
      .subscribe((doc) => {
        if (doc?.content) {
          this.registryDocContent = this.sanitizer.bypassSecurityTrustHtml(
            doc.content
          );
        }
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  setSelectedLetter(letter: string): void {
    if (this.selectedLetter !== letter) {
      this.selectedLetter = letter;
    }
  }

  viewRegistryByKey(key: string): void {
    this.selectedKey = key;
    this.application.re_getPersonIndex(this.selectedKey).subscribe((entry) => {
      this.personIndex = entry;
      if (this.personIndex?.resources?.length) {
        this.registryState.setResourceList(
          this.personIndex.resources,
          'personen',
          this.personIndex.entry.key,
          this.personIndex.entry.preferredName
        );
      }
    });
  }

  setPersonEntries(entries: RegistryEntryPerson[]): void {
    this.persons = entries;
    this.viewPersons = RegistryHelper.personsToRegistryEntryView(this.persons);
    this.createViewGroups();
  }

  createViewGroups(): void {
    const filterString = this.filterStringInput.value;
    const myPersons = !filterString
      ? this.viewPersons
      : this.viewPersons.filter(
          (entry) =>
            entry.entryTitle
              .toLowerCase()
              .indexOf(filterString.toLowerCase()) >= 0
        );
    this.viewGroups = RegistryEntryViewGroup.fromRegistryEntryViews(myPersons);
  }

  viewRegistry(key: string): void {
    if (!key) {
      return;
    }
    this.router.navigate(['/register/personen'], {
      queryParams: { key: key },
    });
  }

  onViewRegistry(event: any): void {
    if (this.personIndex) {
      const text = 'Register: ' + this.personIndex.entry.generatedOfficialName;
      const link = '/register/personen';
      this.referrerService.setReferrerWithQueryParam(
        text,
        link,
        'key',
        this.personIndex.entry.key
      );
    }
  }

  closeInfo(): void {
    this.router.navigate(['/register/personen']);
  }
}
