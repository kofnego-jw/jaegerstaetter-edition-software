import { Component, OnDestroy, OnInit } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {
  IndexEntryBiblePassages,
  RegistryEntryBibleBook,
  RegistryEntryBiblePosition,
} from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { RegistryStateService } from 'src/app/services/registry-state.service';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { ReferrerToResourceService } from 'src/app/services/referrer-to-resource.service';

@Component({
  selector: 'app-bibelstellen-register',
  templateUrl: './bibelstellen-register.component.html',
  styleUrls: ['./bibelstellen-register.component.scss'],
})
export class BibelstellenRegisterComponent implements OnInit, OnDestroy {
  faTimes = faTimes;
  destroyed$: Subject<boolean> = new Subject();

  bibleBooks: RegistryEntryBibleBook[] = [];

  indexEntry: IndexEntryBiblePassages = null;

  registryDocContent: SafeHtml = null;

  constructor(
    private application: ApplicationService,
    private activatedRoute: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private referrerService: ReferrerToResourceService,
    private registryState: RegistryStateService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.queryParamMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((qMap) => {
        const book = qMap.get('key');
        this.setBookKey(book);
      });
    this.application
      .re_getBibleRegistry()
      .subscribe((books) => (this.bibleBooks = books));
    this.application
      .re_getRegistryDoc('bibelstellen')
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

  setBookKey(book: string): void {
    if (book) {
      this.application
        .re_getBibleBookEntry(book)
        .subscribe((entry) => (this.indexEntry = entry));
    } else {
      this.closeInfo();
    }
  }

  onView(pos: RegistryEntryBiblePosition): void {
    if (this.indexEntry?.book?.book) {
      const key = this.indexEntry.book.book + ' ' + pos.position;
      this.registryState.setResourceList(
        pos.resources,
        'bibelstellen',
        this.indexEntry.book.book,
        this.indexEntry.book.preferredName
      );
      const text = 'Register: ' + key;
      const link = '/register/bibelstellen';
      this.referrerService.setReferrerWithQueryParam(
        text,
        link,
        'key',
        this.indexEntry.book.book
      );
    }
  }

  closeInfo(): void {
    this.indexEntry = null;
  }

  close(): void {
    this.router.navigate(['/register/bibelstellen']);
  }
}
