import { Component, Input, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { RegistryEntryPerson } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-personen-info',
  templateUrl: './personen-info.component.html',
  styleUrls: ['./personen-info.component.scss'],
})
export class PersonenInfoComponent implements OnChanges, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();
  faTimes = faTimes;
  @Input()
  person: RegistryEntryPerson;

  biographyAvailable: boolean = false;

  biographyLink: string = '';

  constructor(
    private sanitizer: DomSanitizer,
    private application: ApplicationService,
    private router: Router
  ) {}

  ngOnChanges(): void {
    this.application
      .bg_getBiographies()
      .pipe(takeUntil(this.destroyed$))
      .subscribe((biographies) => {
        if (biographies.length) {
          const foundBio = biographies
            .map((biographies) => biographies.persons)
            .reduce((prevArray, currArray) => [...prevArray, ...currArray])
            .filter((bio) => bio.key === this.person.key);
          this.biographyAvailable = foundBio.length > 0;
          if (this.biographyAvailable) {
            const foundBiographies = biographies.filter(
              (bio) =>
                bio.persons
                  .map((person) => person.key)
                  .indexOf(this.person.key) >= 0
            );
            if (foundBiographies.length) {
              this.biographyLink = foundBiographies[0].filename;
            }
          }
        }
      });
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  getBiographyLink(): SafeUrl {
    const url = '#/biografien/' + this.biographyLink + '?floating=true';
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  authorityLink(link: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(link);
  }

  close(): void {
    this.router.navigate(['/register/personen']);
  }
}
