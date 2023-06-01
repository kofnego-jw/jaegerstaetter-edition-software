import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { PersonInfo } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-person',
  templateUrl: './person.component.html',
  styleUrls: ['./person.component.scss'],
})
export class PersonComponent implements OnInit, OnDestroy {
  destroyed$: Subject<any>;
  personList: PersonInfo[];

  constructor(private app: AppService) {
    this.destroyed$ = new Subject();
    this.personList = [];
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.app.personList$.pipe(takeUntil(this.destroyed$)).subscribe({
      next: (dtos) => (this.personList = dtos),
    });
    this.app.personFindAll();
  }
}
