import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import {
  Authority,
  ControlledVocabulary,
  GeonamesResult,
  GndRecord,
  VocabularyType,
} from 'src/app/model';
import { AppService } from 'src/app/services/app.service';
import {
  faCheck,
  faSearch,
  faMinusCircle,
  faPlusCircle,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-vocabulary-search-modal',
  templateUrl: './vocabulary-search-modal.component.html',
  styleUrls: ['./vocabulary-search-modal.component.scss'],
})
export class VocabularySearchModalComponent implements OnInit {
  faCheck = faCheck;
  faSearch = faSearch;
  faMinusCircle = faMinusCircle;
  faPlusCircle = faPlusCircle;

  @Input()
  type: VocabularyType;
  @Input()
  key: string;
  @Input()
  queryString: string;
  @Input()
  selectedVocabularies: ControlledVocabulary[];

  currentQueryString: string;

  changePreferredName: Subject<string> = new Subject();

  queryForm: FormGroup;

  selected: Subject<ControlledVocabulary[]> = new Subject();

  gndPageNumber: number = 0;

  gndHits: GndRecord[] = [];

  geonamesHits: GeonamesResult[] = [];

  wikidataHits: ControlledVocabulary[] = [];

  constructor(
    private app: AppService,
    private modalRef: BsModalRef,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.resetForm();
    this.performSearch();
  }

  resetForm(): void {
    this.queryForm = this.formBuilder.group({
      queryString: [this.queryString],
    });
  }

  selectPreferredName(voc: ControlledVocabulary): void {
    this.changePreferredName.next(voc.preferredTitle);
  }

  performSearch(): void {
    const qs = this.queryForm.get('queryString').value;
    if (!qs) {
      return;
    }
    this.gndPageNumber = 0;
    this.app
      .searchGnd(qs, this.type, this.gndPageNumber)
      .subscribe((vocs) => (this.gndHits = vocs));
    this.app
      .searchGeonames(qs, 0)
      .subscribe((vocs) => (this.geonamesHits = vocs));
    this.app
      .searchWikidata(qs, this.type, 0)
      .subscribe((vocs) => (this.wikidataHits = vocs));
  }

  previewVocabulary(voc: ControlledVocabulary): void {
    if (!voc) {
      return;
    }
    window.open(
      voc.url,
      'controlledVocabularyPreview',
      'height=500, width=600'
    );
  }

  previewGnd(rec: GndRecord): void {
    if (!rec) {
      return;
    }
    window.open(
      rec.getUrl(),
      'controlledVocabularyPreview',
      'height=500, width=600'
    );
  }

  previewGeonames(rec: GeonamesResult): void {
    if (!rec) {
      return;
    }
    window.open(
      rec.controlledVocabulary.url,
      'controlledVocabularyPreview',
      'height=500, width=600'
    );
  }

  removeVocabulary(voc: ControlledVocabulary): void {
    const index = this.selectedVocabularies.indexOf(voc);
    if (index >= 0) {
      this.selectedVocabularies.splice(index, 1);
    }
  }

  addVocabulary(voc: ControlledVocabulary): void {
    if (!voc) {
      return;
    }
    const found = this.selectedVocabularies.filter(
      (v) =>
        v.authority === voc.authority && v.controlledId === voc.controlledId
    );
    if (found.length) {
      return;
    }
    this.selectedVocabularies.push(voc);
  }

  addGeonamesVocabulary(voc: GeonamesResult): void {
    if (voc?.controlledVocabulary) {
      this.addVocabulary(voc.controlledVocabulary);
    }
  }

  addGndVocabulary(voc: GndRecord): void {
    if (voc?.id) {
      const controlled = new ControlledVocabulary(
        Authority.GND,
        voc.id,
        voc.names,
        voc.preferredName
      );
      this.addVocabulary(controlled);
    }
  }

  nextGndPage(): void {
    this.gndPageNumber = this.gndPageNumber + 1;
    const qs = this.queryForm.get('queryString').value;
    this.app
      .searchGnd(qs, this.type, this.gndPageNumber)
      .subscribe((results) => {
        if (results.length > 0) {
          this.gndHits.push(...results);
        } else {
          this.gndPageNumber = -1;
        }
      });
  }

  select(): void {
    this.selected.next(this.selectedVocabularies);
    this.modalRef.hide();
  }

  cancel(): void {
    this.selected.complete();
    this.modalRef.hide();
  }
}
