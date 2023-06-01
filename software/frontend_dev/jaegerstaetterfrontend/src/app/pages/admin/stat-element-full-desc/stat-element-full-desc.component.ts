import { Component, Input, OnInit } from '@angular/core';
import { StatElementFullDesc } from 'src/app/models/dto';

@Component({
  selector: 'app-stat-element-full-desc',
  templateUrl: './stat-element-full-desc.component.html',
  styleUrls: ['./stat-element-full-desc.component.scss'],
})
export class StatElementFullDescComponent implements OnInit {
  @Input()
  statElementFullDesc: StatElementFullDesc;

  showDetails: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  toggleDetails(): void {
    this.showDetails = !this.showDetails;
  }
}
