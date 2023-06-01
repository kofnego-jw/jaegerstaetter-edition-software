import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-special-corresp',
  templateUrl: './special-corresp.component.html',
  styleUrls: ['./special-corresp.component.scss'],
})
export class SpecialCorrespComponent implements OnInit {
  @Input()
  partners1: string;

  @Input()
  partners2: string;

  constructor(private router: Router) {}

  ngOnInit(): void {}

  navigateToSpecialCorresp(): void {
    this.router.navigate(['/edition/verzeichnis'], {
      queryParams: {
        partners1: this.partners1,
        partners2: this.partners2,
      },
    });
  }
}
