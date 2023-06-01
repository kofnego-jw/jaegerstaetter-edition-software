import { Component, Input, OnInit } from '@angular/core';
import { StatAttrFullDesc } from 'src/app/models/dto';

@Component({
  selector: 'app-stat-attr-full-desc',
  templateUrl: './stat-attr-full-desc.component.html',
  styleUrls: ['./stat-attr-full-desc.component.scss'],
})
export class StatAttrFullDescComponent implements OnInit {
  @Input()
  statAttrFullDesc: StatAttrFullDesc;

  showDetails: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  toggleDetails(): void {
    this.showDetails = !this.showDetails;
  }
}
