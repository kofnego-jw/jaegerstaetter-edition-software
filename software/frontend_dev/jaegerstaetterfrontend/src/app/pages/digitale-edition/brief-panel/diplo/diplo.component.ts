import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Subject } from 'rxjs';
import { ResourceDTO } from 'src/app/models/dto';
import { PanelPosition } from 'src/app/models/frontend';

@Component({
  selector: 'app-diplo',
  templateUrl: './diplo.component.html',
  styleUrls: ['./diplo.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DiploComponent implements OnInit, OnChanges, OnDestroy {
  PanelPosition = PanelPosition;
  destroyed$: Subject<boolean> = new Subject();

  fullScreen: boolean = false;

  @Input()
  panelPosition: PanelPosition;

  @ViewChild('FullComponent')
  fullComponent: ElementRef;

  @Input()
  resource: ResourceDTO;

  content: SafeHtml = null;

  constructor(
    private sanitizer: DomSanitizer,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    this.changeContent();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.changeContent();
  }

  changeContent(): void {
    this.content = this.sanitizer.bypassSecurityTrustHtml(
      this.resource.diplomaticRepresentation
    );
    this.changeDetector.detectChanges();
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
