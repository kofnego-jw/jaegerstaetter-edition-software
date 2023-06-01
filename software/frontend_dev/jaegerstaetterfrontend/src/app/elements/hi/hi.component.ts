import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-hi',
  templateUrl: './hi.component.html',
  styleUrls: ['./hi.component.scss'],
})
export class HiComponent implements OnInit {
  @Input()
  rend: string;

  classes: string[] = [];

  constructor() {}

  ngOnInit(): void {
    if (this.rend) {
      this.classes = this.rend.split(/\s+/);
    }
  }
}
