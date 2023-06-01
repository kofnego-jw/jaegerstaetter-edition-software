import { Component, OnDestroy, OnInit } from '@angular/core';
import { icon, Icon, latLng, marker, Marker, popup, tileLayer } from 'leaflet';
import { Subject, takeUntil } from 'rxjs';
import { PlaceInfo } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';

@Component({
  selector: 'app-world-map',
  templateUrl: './world-map.component.html',
  styleUrls: ['./world-map.component.scss'],
})
export class WorldMapComponent implements OnInit, OnDestroy {
  destroyed$: Subject<any>;
  options = {
    layers: [
      tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 12,
        attribution:
          '<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap-Mitwirkende</a>',
      }),
    ],
    zoom: 5,
    center: latLng(48.095, 12.774),
  };

  mapIcon: Icon = icon({
    iconUrl: 'assets/marker-icon.png',
    iconRetinaUrl: 'assets/marker-icon-2x.png',
    shadowUrl: 'assets/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    tooltipAnchor: [16, -28],
    shadowSize: [41, 41],
  });

  layers: Marker[] = [];

  placeList: PlaceInfo[] = [];

  constructor(private app: AppService) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.app.placeList$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((dtos) => this.setPlaceInfoList(dtos));
    this.app.placeFindAll();
  }

  setPlaceInfoList(dtos: PlaceInfo[]): void {
    this.layers = [];
    this.placeList = dtos;
    if (this.placeList?.length) {
      for (let place of this.placeList) {
        if (place.geoLocation?.latitude && place.geoLocation?.longitude) {
          const name =
            (place.preferredName ? place.preferredName : place.locationName) +
            ' (key: ' +
            place.key +
            ')';

          const newMarker = marker(
            [place.geoLocation.latitude, place.geoLocation.longitude],
            { title: name, icon: this.mapIcon }
          );
          const newPopUp = popup().setContent(name);
          newMarker.bindPopup(newPopUp);
          this.layers.push(newMarker);
        }
      }
    }
  }
}
