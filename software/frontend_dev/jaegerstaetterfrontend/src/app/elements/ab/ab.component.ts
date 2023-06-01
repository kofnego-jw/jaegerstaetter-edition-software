import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-ab',
  templateUrl: './ab.component.html',
  styleUrls: ['./ab.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AbComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
