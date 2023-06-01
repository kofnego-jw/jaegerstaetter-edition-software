import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import { take } from 'rxjs';
import { StatElementFullDesc, StatReport } from 'src/app/models/dto';
import { AdminControllerService } from 'src/app/services/http/admin-controller.service';

@Component({
  selector: 'app-admin-stat-report',
  templateUrl: './admin-stat-report.component.html',
  styleUrls: ['./admin-stat-report.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdminStatReportComponent implements OnInit {
  diplElementNames: string[] = [];

  normElementNames: string[] = [];

  diplElementFullDesc: StatElementFullDesc;

  normElementFullDesc: StatElementFullDesc;

  constructor(
    private adminController: AdminControllerService,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.adminController
      .getElementNames('dipl')
      .pipe(take(1))
      .subscribe((names) => {
        this.diplElementNames = names;
        this.changeDetector.markForCheck();
      });
    this.adminController
      .getElementNames('norm')
      .pipe(take(1))
      .subscribe((names) => {
        this.normElementNames = names;
        this.changeDetector.markForCheck();
      });
  }

  getDiplElementFullDesc(elementName: string): void {
    this.adminController
      .getElementDesc('dipl', elementName)
      .pipe(take(1))
      .subscribe((desc) => {
        this.diplElementFullDesc = desc;
        this.changeDetector.markForCheck();
      });
  }

  getNormElementFullDesc(elementName: string): void {
    this.adminController
      .getElementDesc('norm', elementName)
      .pipe(take(1))
      .subscribe((desc) => {
        this.normElementFullDesc = desc;
        this.changeDetector.markForCheck();
      });
  }
}
