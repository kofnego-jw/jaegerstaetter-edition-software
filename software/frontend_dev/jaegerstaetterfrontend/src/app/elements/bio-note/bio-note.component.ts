import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { faArrowUpLong } from '@fortawesome/free-solid-svg-icons';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-bio-note',
  templateUrl: './bio-note.component.html',
  styleUrls: ['./bio-note.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BioNoteComponent implements OnInit {
  faArrowUpLong = faArrowUpLong;

  @Input()
  n: string;

  @Input()
  dest: string;

  showArrow: boolean = false;

  constructor(
    private scrollIntoView: ScrollIntoViewService,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.dest.endsWith('_text')) {
      this.showArrow = true;
      this.changeDetector.detectChanges();
    }
  }

  scrollToNote(): void {
    this.scrollIntoView.view(this.dest);
  }
}
