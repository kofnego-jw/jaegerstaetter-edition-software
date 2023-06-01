import {
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Subject, take } from 'rxjs';
import { ResourceDTO } from 'src/app/models/dto';
import { PanelPosition } from 'src/app/models/frontend';
import { ApplicationService } from 'src/app/services/application.service';
import { ViewVersionService } from 'src/app/services/view-version.service';

@Component({
  selector: 'app-xml-view',
  templateUrl: './xml-view.component.html',
  styleUrls: ['./xml-view.component.scss'],
})
export class XmlViewComponent implements OnChanges, OnDestroy {
  PanelPosition = PanelPosition;

  destroyed$: Subject<boolean> = new Subject();

  @Input()
  panelPosition: PanelPosition;

  @Input()
  resource: ResourceDTO;

  fullScreen: boolean = false;

  @ViewChild('FullComponent')
  fullComponent: ElementRef;

  xmlCodes: string;

  constructor(
    private application: ApplicationService,
    private viewVersion: ViewVersionService
  ) {}

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnChanges(): void {
    this.xmlCodes = '';
    if (this.resource) {
      this.setResource(this.resource);
    }
  }

  setResource(dto: ResourceDTO): void {
    this.resource = dto;
    if (this.resource) {
      if (this.viewVersion.date) {
        this.application
          .getXmlWithDate(this.resource, this.viewVersion.date, false)
          .pipe(take(1))
          .subscribe((dto) => (this.xmlCodes = dto));
      } else {
        this.application
          .getXml(this.resource, false)
          .pipe(take(1))
          .subscribe((result) => {
            this.xmlCodes = result;
          });
      }
    }
  }

  toggleFullscreen() {
    const element = this.fullComponent.nativeElement;
    if (!this.fullScreen) {
      this.fullScreen = true;
      element.requestFullscreen();
    } else {
      this.fullScreen = false;
      document.exitFullscreen();
    }
  }
}
