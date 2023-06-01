import { Component, Input, OnInit } from '@angular/core';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-unclear',
  templateUrl: './unclear.component.html',
  styleUrls: ['./unclear.component.scss'],
})
export class UnclearComponent implements OnInit {
  @Input()
  reason: string;

  constructor(private app: ApplicationService) {}

  ngOnInit(): void {}

  getReason(): string {
    return this.app.unclear_translateReason(this.reason);
  }
}
