export enum Authority {
  GND = 'GND',
  VIAF = 'VIAF',
  WIKIDATA = 'WIKIDATA',
  GEONAMES = 'GEONAMES',
}

export class AuthorityUtil {
  static getBaseUrlByAuthority(authority: Authority): string {
    if (!authority) {
      return '';
    }
    switch (authority) {
      case Authority.GND:
        return 'https://d-nb.info/gnd/';
      case Authority.VIAF:
        return 'https://viaf.org/viaf/';
      case Authority.WIKIDATA:
        return 'https://www.wikidata.org/wiki/';
      case Authority.GEONAMES:
        return 'https://www.geonames.org/';
    }
  }
  static getUrl(voc: ControlledVocabulary): string {
    if (!voc?.authority) {
      return null;
    }
    return (
      AuthorityUtil.getBaseUrlByAuthority(voc.authority) + voc.controlledId
    );
  }
}

export class ControlledVocabulary {
  static fromDTOs(dtos: ControlledVocabulary[]): ControlledVocabulary[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => ControlledVocabulary.fromDTO(dto));
  }
  static fromDTO(dto: ControlledVocabulary): ControlledVocabulary {
    if (!dto) {
      return null;
    }
    return new ControlledVocabulary(
      dto.authority,
      dto.controlledId,
      dto.titles,
      dto.preferredTitle
    );
  }
  constructor(
    public authority: Authority,
    public controlledId: string,
    public titles: string[],
    public preferredTitle: string
  ) {}
  get url(): string {
    return AuthorityUtil.getUrl(this);
  }
}

export class GeoLocation {
  static fromDTOs(dtos: GeoLocation[]): GeoLocation[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => GeoLocation.fromDTO(dto));
  }

  static fromDTO(dto: GeoLocation): GeoLocation {
    if (!dto) {
      return null;
    }
    return new GeoLocation(dto.latitude, dto.longitude);
  }
  constructor(public latitude: number, public longitude: number) {}
}

export enum Sex {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
}

export enum VocabularyType {
  PERSON = 'PERSON',
  PLACE = 'Place',
  KEYWORD = 'KEYWORD',
  CORPORATION = 'CORPORATION',
  SAINT = 'SAINT',
}

export class GeonamesResult {
  static fromDTO(dto: GeonamesResult): GeonamesResult {
    if (!dto) {
      return null;
    }
    return new GeonamesResult(
      ControlledVocabulary.fromDTO(dto.controlledVocabulary),
      GeoLocation.fromDTO(dto.geoLoaction)
    );
  }
  static fromDTOs(dtos: GeonamesResult[]): GeonamesResult[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => GeonamesResult.fromDTO(dto));
  }
  constructor(
    public controlledVocabulary: ControlledVocabulary,
    public geoLoaction: GeoLocation
  ) {}
}

export class GndRecord {
  static fromDTO(dto: GndRecord): GndRecord {
    if (!dto) {
      return null;
    }
    return new GndRecord(
      dto.id,
      dto.names,
      dto.preferredName,
      GeoLocation.fromDTO(dto.geoLocation)
    );
  }
  static fromDTOs(dtos: GndRecord[]): GndRecord[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => GndRecord.fromDTO(dto));
  }
  constructor(
    public id: string,
    public names: string[],
    public preferredName: string,
    public geoLocation: GeoLocation
  ) {}
  getUrl(): string {
    return `https://d-nb.info/gnd/${this.id}`;
  }
}

export class PersonInfo {
  static fromDTOs(dtos: PersonInfo[]): PersonInfo[] {
    return dtos.map((dto) => PersonInfo.fromDTO(dto));
  }
  static fromDTO(dto: PersonInfo): PersonInfo {
    if (!dto) {
      return null;
    }
    const vocs = ControlledVocabulary.fromDTOs(dto.controlledVocabularies);
    return new PersonInfo(
      dto.key,
      dto.preferredName,
      dto.surname,
      dto.forename,
      dto.addNames,
      dto.roleNames,
      dto.birthDate,
      dto.birthPlace,
      dto.residences,
      dto.deathDate,
      dto.deathPlace,
      dto.sex,
      dto.nationality,
      dto.note,
      dto.internalNotes,
      vocs,
      dto.generatedReadableName,
      dto.generatedOfficialName
    );
  }
  constructor(
    public key: string,
    public preferredName: string,
    public surname: string,
    public forename: string,
    public addNames: string[],
    public roleNames: string[],
    public birthDate: string,
    public birthPlace: string,
    public residences: string[],
    public deathDate: string,
    public deathPlace: string,
    public sex: string,
    public nationality: string,
    public note: string,
    public internalNotes: string,
    public controlledVocabularies: ControlledVocabulary[],
    public generatedReadableName: string,
    public generatedOfficialName: string
  ) {}
  get addnameString(): string {
    if (this.addNames?.length) {
      return this.addNames.join(', ');
    }
    return '';
  }
  get rolenameString(): string {
    if (this.roleNames?.length) {
      return this.roleNames.join(', ');
    }
    return '';
  }
  get residenceString(): string {
    if (this.residences?.length) {
      return this.residences.join(', ');
    }
    return '';
  }
  get stringRepresentation(): string {
    return this.key + ' (' + this.forename + ', ' + this.surname + ')';
  }
}

export class PlaceInfo {
  static fromDTOs(dtos: PlaceInfo[]): PlaceInfo[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => PlaceInfo.fromDTO(dto));
  }
  static fromDTO(dto: PlaceInfo): PlaceInfo {
    if (!dto) {
      return null;
    }
    return new PlaceInfo(
      dto.locationName,
      dto.region,
      dto.key,
      dto.preferredName,
      dto.note,
      dto.todo,
      ControlledVocabulary.fromDTOs(dto.controlledVocabularies),
      GeoLocation.fromDTO(dto.geoLocation),
      dto.generatedName
    );
  }
  constructor(
    public locationName: string,
    public region: string,
    public key: string,
    public preferredName: string,
    public note: string,
    public todo: string,
    public controlledVocabularies: ControlledVocabulary[],
    public geoLocation: GeoLocation,
    public generatedName: string
  ) {}
  get stringRepresentation(): string {
    let s: string = this.locationName;
    if (this.region) {
      s += ' (' + this.region + ')';
    }
    return s;
  }
}

export class CorporationInfo {
  static fromDTOs(dtos: CorporationInfo[]): CorporationInfo[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => CorporationInfo.fromDTO(dto));
  }
  static fromDTO(dto: CorporationInfo): CorporationInfo {
    if (!dto) {
      return null;
    }
    return new CorporationInfo(
      dto.organisation,
      dto.key,
      dto.preferredName,
      dto.note,
      dto.todo,
      ControlledVocabulary.fromDTOs(dto.controlledVocabularies),
      dto.generatedName
    );
  }
  constructor(
    public organisation: string,
    public key: string,
    public preferredName: string,
    public note: string,
    public todo: string,
    public controlledVocabularies: ControlledVocabulary[],
    public generatedName: string
  ) {}
}

export class SaintInfo {
  static fromDTOs(dtos: SaintInfo[]): SaintInfo[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => SaintInfo.fromDTO(dto));
  }
  static fromDTO(dto: SaintInfo): SaintInfo {
    if (!dto) {
      return null;
    }
    return new SaintInfo(
      dto.key,
      dto.title,
      dto.preferredName,
      dto.encyclopediaLink,
      ControlledVocabulary.fromDTOs(dto.controlledVocabularies),
      dto.surname,
      dto.forename,
      dto.addNames,
      dto.rolenames,
      dto.birthDate,
      dto.birthPlace,
      dto.residences,
      dto.deathDate,
      dto.deathPlace,
      dto.sex,
      dto.note,
      dto.todo,
      dto.generatedName
    );
  }
  constructor(
    public key: string,
    public title: string,
    public preferredName: string,
    public encyclopediaLink: string,
    public controlledVocabularies: ControlledVocabulary[],
    public surname: string,
    public forename: string,
    public addNames: string[],
    public rolenames: string[],
    public birthDate: string,
    public birthPlace: string,
    public residences: string[],
    public deathDate: string,
    public deathPlace: string,
    public sex: string,
    public note: string,
    public todo: string,
    public generatedName: string
  ) {}
  get addNameString(): string {
    if (this.addNames?.length) {
      return this.addNames.join(', ');
    }
    return '';
  }
  get roleNameString(): string {
    if (this.rolenames?.length) {
      return this.rolenames.join(', ');
    }
    return '';
  }
  get stringRepresentation(): string {
    return this.title + ' (' + this.forename + ', ' + this.surname + ')';
  }
}

export class TeiDocumentDTO {
  static fromDTO(dto: TeiDocumentDTO): TeiDocumentDTO {
    if (!dto) {
      return null;
    }
    return new TeiDocumentDTO(
      dto.documentPath,
      dto.creationTimestamp,
      dto.version,
      dto.xmlContent
    );
  }
  static fromDTOs(dtos: TeiDocumentDTO[]): TeiDocumentDTO[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => TeiDocumentDTO.fromDTO(dto));
  }
  constructor(
    public documentPath: string,
    public creationTimestamp: number,
    public version: number,
    public xmlContent: string
  ) {}
}

export class RegistryPreviewDTO {
  static fromDTO(dto: RegistryPreviewDTO): RegistryPreviewDTO {
    if (!dto) {
      return null;
    }
    return new RegistryPreviewDTO(dto.type, dto.key, dto.html);
  }
  constructor(public type: string, public key: string, public html: string) {}
}
