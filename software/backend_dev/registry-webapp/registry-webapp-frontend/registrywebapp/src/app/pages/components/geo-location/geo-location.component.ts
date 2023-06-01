import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { GeoLocationModalComponent } from 'src/app/modals/geo-location-modal/geo-location-modal.component';
import { GeoLocation, PlaceInfo } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-geo-location',
  templateUrl: './geo-location.component.html',
  styleUrls: ['./geo-location.component.scss'],
})
export class GeoLocationComponent implements OnInit {
  @Input()
  placeInfo: PlaceInfo;

  @Output()
  change: EventEmitter<PlaceInfo> = new EventEmitter();

  constructor(private app: AppService, private modalService: BsModalService) {}

  ngOnInit(): void {}

  guessLocationFromControlledVocabulary(): void {
    this.app.placeAutoAddGeoLocation(this.placeInfo);
  }

  removeGeoLocation(): void {
    this.placeInfo.geoLocation = null;
    this.emitChange();
  }

  emitChange(): void {
    this.change.emit(this.placeInfo);
  }

  openGeoLocationModal(): void {
    this.modalService.show(GeoLocationModalComponent, {
      class: 'modal-lg',
      initialState: {
        placeInfo: this.placeInfo,
      },
    });
  }
}
