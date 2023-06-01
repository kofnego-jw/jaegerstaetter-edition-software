import { Component, Input, OnInit } from '@angular/core';
import { icon, Icon, latLng, Layer, marker, tileLayer, Map } from 'leaflet';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PlaceInfo } from 'src/app/model';

@Component({
  selector: 'app-geo-location-modal',
  templateUrl: './geo-location-modal.component.html',
  styleUrls: ['./geo-location-modal.component.scss'],
})
export class GeoLocationModalComponent implements OnInit {
  @Input()
  placeInfo: PlaceInfo;

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

  options = {
    layers: [
      tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 15,
        attribution:
          '<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap-Mitwirkende</a>',
      }),
    ],
    zoom: 6,
    center: latLng(48.095, 12.774),
  };

  layers: Layer[] = [];

  constructor(private modalRef: BsModalRef) {}

  ngOnInit(): void {
    if (this.placeInfo?.geoLocation) {
      this.options.center = latLng(
        this.placeInfo.geoLocation.latitude,
        this.placeInfo.geoLocation.longitude
      );
      this.layers = [];
      const markerLayer = marker(
        latLng(
          this.placeInfo.geoLocation.latitude,
          this.placeInfo.geoLocation.longitude
        ),
        {
          icon: this.mapIcon,
        }
      );
      this.layers.push(markerLayer);
    }
  }

  onMapReady(map: Map): void {
    setTimeout(() => map.invalidateSize());
  }

  close(): void {
    this.modalRef.hide();
  }
}
