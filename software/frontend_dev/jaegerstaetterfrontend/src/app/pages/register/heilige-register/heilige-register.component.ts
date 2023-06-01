import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil, debounceTime, distinctUntilChanged } from 'rxjs/operators';
import {
  IndexEntrySaint,
  RegistryEntrySaint,
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
  selector: 'app-heilige-register',
  templateUrl: './heilige-register.component.html',
  styleUrls: ['./heilige-register.component.scss'],
})
export class HeiligeRegisterComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  selectedLetter: string = '';

  selectedKey: string = '';

  saintIndex: IndexEntrySaint = null;

  saints: RegistryEntrySaint[] = [];

  viewSaints: RegistryEntryView[] = [];

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
          this.closeInfo();
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
      .re_getSaintRegistry()
      .pipe(takeUntil(this.destroyed$))
      .subscribe((entries) => this.setSaintEntries(entries));

    this.application
      .re_getRegistryDoc(RegistryType.SAINT)
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
    this.application.re_getSaintIndex(this.selectedKey).subscribe((entry) => {
      this.saintIndex = entry;
      if (this.saintIndex?.resources?.length) {
        this.registryState.setResourceList(
          this.saintIndex.resources,
          'heilige',
          this.saintIndex.entry.key,
          this.saintIndex.entry.preferredName
        );
      }
    });
  }

  setSaintEntries(entries: RegistryEntrySaint[]): void {
    this.saints = entries;
    this.viewSaints = RegistryHelper.saintsToRegistryEntryView(this.saints);
    this.createViewGroups();
  }

  createViewGroups(): void {
    const filterString = this.filterStringInput.value;
    const mySaints = !filterString
      ? this.viewSaints
      : this.viewSaints.filter(
          (entry) =>
            entry.entryTitle
              .toLowerCase()
              .indexOf(filterString.toLowerCase()) >= 0
        );
    this.viewGroups = RegistryEntryViewGroup.fromRegistryEntryViews(mySaints);
  }

  viewRegistry(key: string): void {
    if (!key) {
      return;
    }
    this.router.navigate(['/register/heilige'], {
      queryParams: { key: key },
    });
  }

  onViewRegistry(event: any): void {
    if (this.saintIndex) {
      const text = 'Register: ' + this.saintIndex.entry.title;
      const link = '/register/heilige';
      this.referrerService.setReferrerWithQueryParam(
        text,
        link,
        'key',
        this.saintIndex.entry.key
      );
    }
  }

  closeInfo(): void {
    this.saintIndex = null;
    this.selectedKey = '';
  }
}
