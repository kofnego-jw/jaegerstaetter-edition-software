import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ColoringService } from 'src/app/services/coloring.service';

@Component({
  selector: 'app-hands-info',
  templateUrl: './hands-info.component.html',
  styleUrls: ['./hands-info.component.scss'],
})
export class HandsInfoComponent implements OnInit {
  @Input()
  hands: string;

  constructor(private coloring: ColoringService) {}

  ngOnInit(): void {
    if (this.hands.length) {
      const hands = this.hands.split(';');
      this.coloring.setHands(hands);
    } else {
      this.coloring.resetColoringInfo();
    }
  }
}
