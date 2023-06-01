import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import {
  icon,
  Icon,
  LatLng,
  latLng,
  marker,
  Marker,
  popup,
  tileLayer,
} from 'leaflet';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';
import {
  IndexEntryPlace,
  RegistryEntryPlace,
  RegistryType,
} from 'src/app/models/dto';
import { RegistryEntryViewGroup, RegistryHelper } from 'src/app/models/helper';
import { ApplicationService } from 'src/app/services/application.service';
import { MapConsentService } from 'src/app/services/map-consent.service';
import { ReferrerToResourceService } from 'src/app/services/referrer-to-resource.service';
import { RegistryStateService } from 'src/app/services/registry-state.service';
import { faGear } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-orts-register',
  templateUrl: './orts-register.component.html',
  styleUrls: ['./orts-register.component.scss'],
})
export class OrtsRegisterComponent implements OnInit, OnDestroy {
  destroyed$: Subject<boolean> = new Subject();

  faGear = faGear;

  consent: boolean = false;

  selectedLetter: string = '';

  selectedKey: string = '';

  placeIndex: IndexEntryPlace = null;

  places: RegistryEntryPlace[] = [];

  viewPlaces: RegistryEntryPlace[] = [];

  viewGroups: RegistryEntryViewGroup[] = [];

  filterStringInput: FormControl = new FormControl<string | null>('');

  defaultCenter = latLng(48.095, 12.774);

  center = this.defaultCenter;

  myMap: L.Map;

  options = {
    layers: [
      tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 12,
        attribution:
          '<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap-Mitwirkende</a>',
      }),
    ],
    zoom: 5,
    center: this.defaultCenter,
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

  registryDocContent: SafeHtml = null;

  constructor(
    private application: ApplicationService,
    private activatedRoute: ActivatedRoute,
    private registryState: RegistryStateService,
    private referrerService: ReferrerToResourceService,
    private sanitizer: DomSanitizer,
    private router: Router,
    private mapConsent: MapConsentService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.queryParamMap
      .pipe(takeUntil(this.destroyed$))
      .subscribe((paramMap) => {
        if (paramMap?.get('select')) {
          this.selectedLetter = paramMap.get('select');
        }
        if (paramMap?.get('key')) {
          const newKey = paramMap.get('key');
          if (newKey !== this.selectedKey) {
            this.viewRegistryByKey(newKey);
          }
        } else {
          this.closeInfo();
        }
      });

    this.filterStringInput.valueChanges
      .pipe(
        takeUntil(this.destroyed$),
        debounceTime(1000),
        distinctUntilChanged()
      )
      .subscribe(() => this.createViewGroups());

    this.application
      .re_getPlaceRegistry()
      .pipe(takeUntil(this.destroyed$))
      .subscribe((entries) => this.setPlaceEntries(entries));

    this.application
      .re_getRegistryDoc(RegistryType.PLACE)
      .pipe(takeUntil(this.destroyed$))
      .subscribe((doc) => {
        if (doc?.content) {
          this.registryDocContent = this.sanitizer.bypassSecurityTrustHtml(
            doc.content
          );
        }
      });

    this.mapConsent
      .hasConsentInMap()
      .subscribe((consent) => (this.consent = consent));
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  revokeConsent(): void {
    this.mapConsent.revoke().subscribe((consent) => (this.consent = consent));
  }

  onMapReady(map: L.Map) {
    this.myMap = map;
    this.myMap.panTo(this.center);
  }

  setSelectedLetter(letter: string): void {
    if (this.selectedLetter !== letter) {
      this.selectedLetter = letter;
    }
  }

  viewRegistryByKey(key: string): void {
    this.selectedKey = key;
    this.application
      .re_getPlaceIndex(this.selectedKey)
      .subscribe((entry) => this.setPlaceIndex(entry));
  }

  setPlaceIndex(place: IndexEntryPlace): void {
    this.placeIndex = place;
    if (place) {
      this.registryState.setResourceList(
        place.resources,
        'orte',
        place.entry.key,
        place.entry.preferredName
      );
    }
    this.createMarkers();
  }

  setPlaceEntries(entries: RegistryEntryPlace[]): void {
    this.places = entries;
    this.createViewGroups();
  }

  createViewGroups(): void {
    const filterString = this.filterStringInput.value;
    this.viewPlaces = !filterString
      ? this.places
      : this.places.filter(
          (entry) =>
            entry.locationName
              .toLowerCase()
              .indexOf(filterString.toLowerCase()) >= 0
        );
    this.viewGroups = RegistryHelper.groupPlaceEntryTogether(this.viewPlaces);
    this.createMarkers();
  }

  createMarkers(): void {
    this.layers = [];
    let center: LatLng;
    let zoom: number;
    if (this.placeIndex?.entry?.geoLocation) {
      const name = this.placeIndex.entry.preferredName
        ? this.placeIndex.entry.preferredName
        : this.placeIndex.entry.locationName;
      const newMarker = marker(
        [
          this.placeIndex.entry.geoLocation.latitude,
          this.placeIndex.entry.geoLocation.longitude,
        ],
        { title: name, icon: this.mapIcon }
      );
      const newPopUp = popup().setContent(name);
      newMarker.bindPopup(newPopUp);
      this.layers.push(newMarker);
      center = latLng(
        this.placeIndex.entry.geoLocation.latitude,
        this.placeIndex.entry.geoLocation.longitude
      );
      zoom = 10;
    } else {
      this.viewPlaces.forEach((place) => {
        if (place.geoLocation?.latitude && place.geoLocation?.longitude) {
          const name = place.preferredName
            ? place.preferredName
            : place.locationName;
          const newMarker = marker(
            [place.geoLocation.latitude, place.geoLocation.longitude],
            { title: name, icon: this.mapIcon }
          );
          const newPopUp = popup().setContent(
            `<ffji-viewplace key="${place.key}">${name}</ffji-viewplace>`
          );
          newMarker.bindPopup(newPopUp);
          this.layers.push(newMarker);
        }
      });
      center = this.defaultCenter;
      zoom = 5;
    }
    this.options = { ...this.options, center, zoom };
    this.center = center;
    if (this.myMap) {
      this.myMap.panTo(center);
    }
  }

  viewRegistry(key: string): void {
    if (!key) {
      return;
    }
    this.router.navigate(['/register/orte'], {
      queryParams: { key: key },
    });
  }

  onViewRegistry(event: any): void {
    if (this.placeIndex) {
      const text = 'Register: ' + this.placeIndex.entry.preferredName;
      const link = '/register/orte';
      this.referrerService.setReferrerWithQueryParam(
        text,
        link,
        'key',
        this.placeIndex.entry.key
      );
    }
  }

  closeInfo(): void {
    this.placeIndex = null;
    this.selectedKey = '';
    this.createMarkers();
  }
}
