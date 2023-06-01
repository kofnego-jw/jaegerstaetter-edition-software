import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { PhotoDocumentGroup, PhotoDocumentGroupType } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-foto-dok-verzeichnis',
  templateUrl: './foto-dok-verzeichnis.component.html',
  styleUrls: ['./foto-dok-verzeichnis.component.scss'],
})
export class FotoDokVerzeichnisComponent implements OnInit, OnDestroy {
  PHOTO = PhotoDocumentGroupType.PHOTO;
  DOCUMENT = PhotoDocumentGroupType.DOCUMENT;

  destroyed$: Subject<any> = new Subject();

  constructor(private application: ApplicationService) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }
}
