import { Component, Input, OnInit } from '@angular/core';
import { TocEntry } from 'src/app/models/dto';
import { ScrollIntoViewService } from 'src/app/services/scroll-into-view.service';

@Component({
  selector: 'app-toc-entry',
  templateUrl: './toc-entry.component.html',
  styleUrls: ['./toc-entry.component.scss'],
})
export class TocEntryComponent implements OnInit {
  @Input()
  tocEntry: TocEntry;

  constructor(private scrollService: ScrollIntoViewService) {}

  ngOnInit(): void {}

  scrollToId(): void {
    if (this.tocEntry?.id) {
      this.scrollService.view(this.tocEntry.id);
    }
  }
}
