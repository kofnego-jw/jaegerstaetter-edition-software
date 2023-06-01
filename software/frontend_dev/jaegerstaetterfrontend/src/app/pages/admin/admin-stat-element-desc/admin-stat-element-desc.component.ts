import { Component, Input, OnInit } from '@angular/core';
import { StatElementFullDesc } from 'src/app/models/dto';

@Component({
  selector: 'app-admin-stat-element-desc',
  templateUrl: './admin-stat-element-desc.component.html',
  styleUrls: ['./admin-stat-element-desc.component.scss'],
})
export class AdminStatElementDescComponent implements OnInit {
  @Input()
  elementFullDesc: StatElementFullDesc;

  constructor() {}

  ngOnInit(): void {}
}
