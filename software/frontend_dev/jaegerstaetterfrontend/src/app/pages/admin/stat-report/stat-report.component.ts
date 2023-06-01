import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnInit,
} from '@angular/core';
import { StatReport } from 'src/app/models/dto';

@Component({
  selector: 'app-stat-report',
  templateUrl: './stat-report.component.html',
  styleUrls: ['./stat-report.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StatReportComponent implements OnChanges {
  @Input()
  statReport: StatReport = null;

  constructor(private changeDetector: ChangeDetectorRef) {}

  ngOnChanges(): void {
    this.changeDetector.markForCheck();
  }
}
