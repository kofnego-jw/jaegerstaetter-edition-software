import { Component, Input, OnInit } from '@angular/core';
import { StatAttrValueOcc } from 'src/app/models/dto';

@Component({
  selector: 'app-stat-attr-value-occ',
  templateUrl: './stat-attr-value-occ.component.html',
  styleUrls: ['./stat-attr-value-occ.component.scss'],
})
export class StatAttrValueOccComponent implements OnInit {
  @Input() statAttrValueOcc: StatAttrValueOcc;

  showDetails: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  toggleDetails(): void {
    this.showDetails = !this.showDetails;
  }
}
