import { Component, Input, OnInit } from '@angular/core';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-gap',
  templateUrl: './gap.component.html',
  styleUrls: ['./gap.component.scss'],
})
export class GapComponent implements OnInit {
  @Input()
  rend: string;

  @Input()
  reason: string;

  @Input()
  quantity: string;

  @Input()
  gaptype: string;

  constructor(private app: ApplicationService) {}

  ngOnInit(): void {}

  getReason(): string {
    return this.app.gap_translateReason(this.reason);
  }

  getGapText(): string {
    if (this.quantity) {
      const regexResult = /(\d+)\s+(.*)/.exec(this.quantity);
      if (regexResult && regexResult.length) {
        const repeat = Number.parseInt(regexResult[1]);
        if (regexResult[2]?.toLowerCase().startsWith('c')) {
          let gapText = '';
          for (let i = 0; i < repeat; i++) {
            gapText += '?';
          }
          return gapText;
        } else {
          let gapText = '';
          for (let i = 0; i < repeat; i++) {
            gapText += '???? ';
          }
          return gapText.trim();
        }
      } else {
        return '[?]';
      }
    } else if (this.reason === 'filler') {
      return '‒‒‒';
    } else {
      return '[?]';
    }
  }
}
