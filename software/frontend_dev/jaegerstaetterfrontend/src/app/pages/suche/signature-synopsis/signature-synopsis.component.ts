import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { Subject, debounceTime, takeUntil } from 'rxjs';
import { ResourceFWDTO } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-signature-synopsis',
  templateUrl: './signature-synopsis.component.html',
  styleUrls: ['./signature-synopsis.component.scss'],
})
export class SignatureSynopsisComponent implements OnInit, OnDestroy {
  faArrowLeft = faArrowLeft;

  resourceList: ResourceFWDTO[] = [];

  shownList: ResourceFWDTO[] = [];

  destroyed$: Subject<boolean> = new Subject();

  filterControl: FormControl<string> = new FormControl('');

  filterString: string = '';

  constructor(private app: ApplicationService, private router: Router) {}

  ngOnInit(): void {
    this.app.resourceList$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((list) => this.setResourceList(list));
    if (!this.app.resourceList.length) {
      this.app.loadResourceList();
    }
    this.filterControl.valueChanges
      .pipe(takeUntil(this.destroyed$), debounceTime(500))
      .subscribe((val) => this.setFilterString(val));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  setFilterString(s: string): void {
    this.filterString = s;
    this.filterResources();
  }

  setResourceList(list: ResourceFWDTO[]) {
    this.resourceList = list;
    this.filterResources();
  }

  filterResources(): void {
    if (!this.filterString) {
      this.shownList = this.resourceList.slice(0, this.resourceList.length);
    } else {
      const regex = new RegExp('\\W*' + this.filterString + '\\W*');
      this.shownList = this.resourceList.filter((fw) => {
        return (
          fw.altSignature.search(regex) >= 0 || fw.signature.search(regex) >= 0
        );
      });
    }
  }

  showResource(res: ResourceFWDTO): void {
    this.router.navigate(['/edition/view/' + res.id]);
  }
}
