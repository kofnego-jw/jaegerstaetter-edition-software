import { ArrayHelper } from './helper';

export class MenuItem {
  static fromDTOs(dtos: MenuItem[]): MenuItem[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => MenuItem.fromDTO(dto));
  }
  static fromDTO(dto: MenuItem): MenuItem {
    if (!dto) {
      return null;
    }
    return new MenuItem(dto.displayName, dto.id);
  }
  constructor(public displayName: string, public id: string) {}
}

export class CommentDoc {
  static fromDTO(dto: CommentDoc): CommentDoc {
    if (!dto) {
      return null;
    }
    return new CommentDoc(
      dto.displayName,
      dto.lastModified,
      dto.content,
      dto.toc
    );
  }

  constructor(
    public displayName: string,
    public lastModified: number,
    public content: string,
    public toc: string
  ) {}
}

export class MetadataRecord {
  static fromDTOs(dtos: MetadataRecord[]): MetadataRecord[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => MetadataRecord.fromDTO(dto));
  }
  static fromDTO(dto: MetadataRecord): MetadataRecord {
    if (!dto) {
      return null;
    }
    return new MetadataRecord(
      dto.fieldname,
      dto.content,
      ArrayHelper.trimStringArray(dto.externalLinks)
    );
  }
  constructor(
    public fieldname: string,
    public content: string,
    public externalLinks: string[]
  ) {}
}

export class MetadataGroup {
  static fromDTOs(dtos: MetadataGroup[]): MetadataGroup[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => MetadataGroup.fromDTO(dto));
  }
  static fromDTO(dto: MetadataGroup): MetadataGroup {
    if (!dto) {
      return null;
    }
    return new MetadataGroup(
      dto.groupKey,
      MetadataRecord.fromDTOs(dto.records)
    );
  }
  constructor(public groupKey: string, public records: MetadataRecord[]) {}
  get(recKey: string): string {
    const found = this.records.filter((rec) => rec.fieldname === recKey);
    if (found.length) {
      return found[0].content;
    }
    return '';
  }
}

export enum ResourceType {
  LETTER = 'LETTER',
  DOCUMENT = 'DOCUMENT',
}

export enum EditionCorpus {
  FROM_FRANZ = 'FROM_FRANZ',
  TO_FRANZ = 'TO_FRANZ',
  ABOUT_FRANZ = 'ABOUT_FRANZ',
  CORRESPONDENCE = 'CORRESPONDENCE',
}

export enum EditionTimePeriod {
  EARLY = 'EARLY',
  MILITARY = 'MILITARY',
  TO_REFUSAL = 'TO_REFUSAL',
  PRISON = 'PRISON',
  AFTERLIFE = 'AFTERLIFE',
}

export class ResourceFWDTO {
  static fromDTOs(dtos: ResourceFWDTO[]): ResourceFWDTO[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => ResourceFWDTO.fromDTO(dto));
  }
  static fromDTO(dto: ResourceFWDTO): ResourceFWDTO {
    if (!dto) {
      return null;
    }
    const corpora: EditionCorpus[] = dto.corpora?.length ? dto.corpora : [];
    const periods: EditionTimePeriod[] = dto.periods?.length ? dto.periods : [];
    return new ResourceFWDTO(
      dto.id,
      dto.title,
      dto.dating,
      dto.datingReadable,
      dto.type,
      dto.summary,
      corpora,
      periods,
      ArrayHelper.trimStringArray(dto.authors),
      ArrayHelper.trimStringArray(dto.recipients),
      ArrayHelper.trimStringArray(dto.places),
      ArrayHelper.trimStringArray(dto.objectTypes),
      dto.signature,
      dto.altSignature
    );
  }
  constructor(
    public id: string,
    public title: string,
    public dating: string,
    public datingReadable: string,
    public type: ResourceType,
    public summary: string,
    public corpora: EditionCorpus[],
    public periods: EditionTimePeriod[],
    public authors: string[],
    public recipients: string[],
    public places: string[],
    public objectTypes: string[],
    public signature: string,
    public altSignature: string
  ) {}
}

export class VersionInfo {
  static fromDTOs(dtos: VersionInfo[]): VersionInfo[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => VersionInfo.fromDTO(dto));
  }
  static fromDTO(dto: VersionInfo): VersionInfo {
    if (!dto) {
      return null;
    }
    return new VersionInfo(dto.versionNumber, dto.creationTimestamp);
  }
  constructor(public versionNumber: number, public creationTimestamp: string) {}
}

export class TocEntry {
  static fromDTOs(dtos: TocEntry[]): TocEntry[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => TocEntry.fromDTO(dto));
  }
  static fromDTO(dto: TocEntry): TocEntry {
    if (!dto) {
      return null;
    }
    return new TocEntry(dto.id, dto.title, TocEntry.fromDTOs(dto.children));
  }
  constructor(
    public id: string,
    public title: string,
    public children: TocEntry[]
  ) {}
}

export class TocList {
  static fromDTO(dto: TocList): TocList {
    if (!dto) {
      return new TocList([]);
    }
    return new TocList(TocEntry.fromDTOs(dto.toc));
  }
  constructor(public toc: TocEntry[]) {}
}

export class ResourceDTO extends ResourceFWDTO {
  static fromDTO(dto: ResourceDTO): ResourceDTO {
    if (!dto) {
      return null;
    }
    const corpora: EditionCorpus[] = dto.corpora?.length ? dto.corpora : [];
    const periods: EditionTimePeriod[] = dto.periods?.length ? dto.periods : [];
    return new ResourceDTO(
      dto.id,
      dto.title,
      dto.dating,
      dto.datingReadable,
      dto.type,
      dto.summary,
      corpora,
      periods,
      ArrayHelper.trimStringArray(dto.authors),
      ArrayHelper.trimStringArray(dto.recipients),
      ArrayHelper.trimStringArray(dto.places),
      ArrayHelper.trimStringArray(dto.objectTypes),
      dto.signature,
      dto.altSignature,
      ArrayHelper.trimStringArray(dto.facsimileIds),
      MetadataGroup.fromDTOs(dto.metadata),
      VersionInfo.fromDTOs(dto.versions),
      dto.normalizedRepresentation,
      dto.diplomaticRepresentation,
      ResourceFWDTO.fromDTOs(dto.prevLetters),
      ResourceFWDTO.fromDTOs(dto.nextLetters),
      ResourceFWDTO.fromDTOs(dto.relatedLetters),
      TocList.fromDTO(dto.tocList)
    );
  }
  constructor(
    id: string,
    title: string,
    dating: string,
    datingReadable: string,
    type: ResourceType,
    summary: string,
    corpora: EditionCorpus[],
    periods: EditionTimePeriod[],
    authors: string[],
    recipients: string[],
    places: string[],
    objectTypes: string[],
    signature: string,
    altSignature: string,
    public facsimileIds: string[],
    public metadata: MetadataGroup[],
    public versions: VersionInfo[],
    public normalizedRepresentation: string,
    public diplomaticRepresentation: string,
    public prevLetters: ResourceFWDTO[],
    public nextLetters: ResourceFWDTO[],
    public relatedLetters: ResourceFWDTO[],
    public tocList: TocList
  ) {
    super(
      id,
      title,
      dating,
      datingReadable,
      type,
      summary,
      corpora,
      periods,
      authors,
      recipients,
      places,
      objectTypes,
      signature,
      altSignature
    );
  }
  get allRelatedLetters(): ResourceFWDTO[] {
    return this.prevLetters
      .concat(this.nextLetters)
      .concat(this.relatedLetters)
      .concat(this)
      .sort((a, b) => a.dating.localeCompare(b.dating));
  }
  getMetadata(groupName: string): MetadataGroup {
    const found = this.metadata.filter((g) => g.groupKey === groupName);
    if (found.length) {
      return found[0];
    }
    return null;
  }
}

export enum SearchField {
  ALL = 'ALL',
  TRANSCRIPT = 'TRANSCRIPT',
  COMMENTARY = 'COMMENTARY',
  DATING = 'DATING',
}

export enum SearchOccur {
  MUST = 'MUST',
  SHOULD = 'SHOULD',
  MUST_NOT = 'MUST_NOT',
}

export class SearchFieldParam {
  static fromDTOs(dtos: SearchFieldParam[]): SearchFieldParam[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => SearchFieldParam.fromDTO(dto));
  }
  static fromDTO(dto: SearchFieldParam): SearchFieldParam {
    if (!dto) {
      return null;
    }
    return new SearchFieldParam(dto.field, dto.queryString, dto.occur);
  }
  constructor(
    public field: SearchField,
    public queryString: string,
    public occur: SearchOccur
  ) {}
}

export class SearchRequest {
  static fromDTO(dto: SearchRequest): SearchRequest {
    if (!dto) {
      return null;
    }
    return new SearchRequest(
      SearchFieldParam.fromDTOs(dto.queryParams),
      dto.pageNumber,
      dto.pageSize,
      dto.sortField,
      dto.sortAsc
    );
  }
  constructor(
    public queryParams: SearchFieldParam[],
    public pageNumber: number,
    public pageSize: number,
    public sortField: string,
    public sortAsc: boolean
  ) {}
  prevRequest(): SearchRequest {
    const pn = this.pageNumber > 0 ? this.pageNumber - 1 : 0;
    return new SearchRequest(
      [...this.queryParams],
      pn,
      this.pageSize,
      this.sortField,
      this.sortAsc
    );
  }
  nextRequest(): SearchRequest {
    return new SearchRequest(
      [...this.queryParams],
      this.pageNumber + 1,
      this.pageSize,
      this.sortField,
      this.sortAsc
    );
  }
  toPage(pn: number): SearchRequest {
    return new SearchRequest(
      [...this.queryParams],
      pn,
      this.pageSize,
      this.sortField,
      this.sortAsc
    );
  }
}

export class SearchHitPreview {
  static fromDTOs(dtos: SearchHitPreview[]): SearchHitPreview[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => SearchHitPreview.fromDTO(dto));
  }
  static fromDTO(dto: SearchHitPreview): SearchHitPreview {
    if (!dto) {
      return null;
    }
    return new SearchHitPreview(
      dto.fieldname,
      ArrayHelper.trimStringArray(dto.snippets)
    );
  }
  constructor(public fieldname: string, public snippets: string[]) {}
}

export class SearchHit {
  static fromDTOs(dtos: SearchHit[]): SearchHit[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => SearchHit.fromDTO(dto));
  }
  static fromDTO(dto: SearchHit): SearchHit {
    if (!dto) {
      return null;
    }
    return new SearchHit(
      dto.hitNumber,
      dto.documentId,
      ResourceFWDTO.fromDTO(dto.resource),
      SearchHitPreview.fromDTOs(dto.previews)
    );
  }
  constructor(
    public hitNumber: number,
    public documentId: string,
    public resource: ResourceFWDTO,
    public previews: SearchHitPreview[]
  ) {}

  previewText(): string {
    return this.previews
      .filter(
        (preview) =>
          preview.fieldname === 'transcript' ||
          preview.fieldname === 'commentary'
      )
      .map((preview) => preview.snippets.join(' [...] '))
      .filter((st) => !!st)
      .join(' [...] ');
  }
}

export class SearchResult {
  static fromDTO(dto: SearchResult): SearchResult {
    if (!dto) {
      return null;
    }
    return new SearchResult(
      SearchRequest.fromDTO(dto.searchRequest),
      dto.totalHits,
      dto.fromNumber,
      dto.toNumber,
      SearchHit.fromDTOs(dto.hits)
    );
  }
  maxPageCount: number;
  constructor(
    public searchRequest: SearchRequest,
    public totalHits: number,
    public fromNumber: number,
    public toNumber: number,
    public hits: SearchHit[]
  ) {
    const ps = this.searchRequest.pageSize;
    this.maxPageCount = Math.ceil(this.totalHits / ps);
  }
  toPage(pn: number): SearchRequest {
    return this.searchRequest.toPage(pn);
  }
  hasPrev(): boolean {
    if (this.totalHits === 0) {
      return false;
    }
    if (this.searchRequest.pageNumber > 0) {
      return true;
    }
    return false;
  }
  hasNext(): boolean {
    if (this.totalHits === 0) {
      return false;
    }
    return this.toNumber < this.totalHits;
  }
  prevRequest(): SearchRequest {
    return this.searchRequest.prevRequest();
  }
  nextRequest(): SearchRequest {
    return this.searchRequest.nextRequest();
  }
}

export class SearchSortField {
  static readonly BY_RELEVANCE: SearchSortField = new SearchSortField(
    null,
    'Nach Relevanz'
  );
  static readonly BY_DATING: SearchSortField = new SearchSortField(
    'DATING',
    'Nach Datierung'
  );
  static readonly BY_RESOURCE_ID: SearchSortField = new SearchSortField(
    'RESOURCE_ID',
    'Dateiname'
  );
  static readonly BY_TITLE: SearchSortField = new SearchSortField(
    'TITLE',
    'Titel'
  );

  static readonly SORT_FIELDS: SearchSortField[] = [
    SearchSortField.BY_RELEVANCE,
    SearchSortField.BY_DATING,
    //    SearchSortField.BY_TITLE,
    //    SearchSortField.BY_RESOURCE_ID,
  ];

  static readonly TOC_SORT_FIELDS: SearchSortField[] = [
    SearchSortField.BY_DATING,
    SearchSortField.BY_RESOURCE_ID,
  ];

  constructor(public sortFieldname: string, public readableName: string) {}
}

export class PasswordDTO {
  constructor(public password: string) {}
}

export class RegistryEntryCorporation {
  static fromDTOs(
    dtos: RegistryEntryCorporation[]
  ): RegistryEntryCorporation[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => RegistryEntryCorporation.fromDTO(dto));
  }
  static fromDTO(dto: RegistryEntryCorporation): RegistryEntryCorporation {
    if (!dto) {
      return null;
    }
    const preferredName = dto.preferredName
      ? dto.preferredName
      : dto.organisation;
    return new RegistryEntryCorporation(
      dto.organisation,
      preferredName,
      dto.key,
      dto.note,
      ArrayHelper.trimStringArray(dto.controlledVocabularies),
      dto.generatedName
    );
  }
  constructor(
    public organisation: string,
    public preferredName: string,
    public key: string,
    public note: string,
    public controlledVocabularies: string[],
    public generatedName: string
  ) {}
}

export class IndexEntryCorporation {
  static fromDTO(dto: IndexEntryCorporation): IndexEntryCorporation {
    if (!dto) {
      return null;
    }
    return new IndexEntryCorporation(
      RegistryEntryCorporation.fromDTO(dto.entry),
      ResourceFWDTO.fromDTOs(dto.resources)
    );
  }
  constructor(
    public entry: RegistryEntryCorporation,
    public resources: ResourceFWDTO[]
  ) {}
}

export class RegistryEntryPerson {
  static fromDTOs(dtos: RegistryEntryPerson[]): RegistryEntryPerson[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => RegistryEntryPerson.fromDTO(dto));
  }
  static fromDTO(dto: RegistryEntryPerson): RegistryEntryPerson {
    if (!dto) {
      return null;
    }
    const preferredName = dto.preferredName
      ? dto.preferredName
      : dto.surname && dto.surname !== '?'
      ? dto.surname +
        (dto.forename && dto.forename !== '?' ? ', ' + dto.forename : '')
      : dto.key;
    return new RegistryEntryPerson(
      dto.key,
      preferredName,
      dto.surname,
      dto.forename,
      ArrayHelper.trimStringArray(dto.addNames),
      ArrayHelper.trimStringArray(dto.roleNames),
      dto.birthDate,
      dto.birthPlace,
      ArrayHelper.trimStringArray(dto.residences),
      dto.deathDate,
      dto.deathPlace,
      dto.sex,
      dto.nationality,
      dto.note,
      ArrayHelper.trimStringArray(dto.controlledVocabularies),
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
    public controlledVocabularies: string[],
    public generatedReadableName: string,
    public generatedOfficialName: string
  ) {}
  get addNamesString(): string {
    return this.addNames.join(', ');
  }
}

export class IndexEntryPerson {
  static fromDTO(dto: IndexEntryPerson): IndexEntryPerson {
    if (!dto) {
      return null;
    }
    return new IndexEntryPerson(
      RegistryEntryPerson.fromDTO(dto.entry),
      ResourceFWDTO.fromDTOs(dto.resources)
    );
  }
  constructor(
    public entry: RegistryEntryPerson,
    public resources: ResourceFWDTO[]
  ) {}
}

export class GeoLocation {
  static fromDTO(dto: GeoLocation): GeoLocation {
    if (!dto) {
      return null;
    }
    return new GeoLocation(dto.latitude, dto.longitude);
  }
  constructor(public latitude: number, public longitude: number) {}
}

export class RegistryEntryPlace {
  static fromDTOs(dtos: RegistryEntryPlace[]): RegistryEntryPlace[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => RegistryEntryPlace.fromDTO(dto));
  }
  static fromDTO(dto: RegistryEntryPlace): RegistryEntryPlace {
    if (!dto) {
      return null;
    }
    const preferredName = dto.preferredName
      ? dto.preferredName
      : dto.locationName
      ? dto.locationName
      : dto.key;
    return new RegistryEntryPlace(
      dto.locationName,
      preferredName,
      dto.region,
      dto.key,
      dto.note,
      ArrayHelper.trimStringArray(dto.controlledVocabularies),
      GeoLocation.fromDTO(dto.geoLocation),
      dto.generatedName
    );
  }
  constructor(
    public locationName: string,
    public preferredName: string,
    public region: string,
    public key: string,
    public note: string,
    public controlledVocabularies: string[],
    public geoLocation: GeoLocation,
    public generatedName: string
  ) {}
}

export class IndexEntryPlace {
  static fromDTO(dto: IndexEntryPlace): IndexEntryPlace {
    if (!dto) {
      return null;
    }
    return new IndexEntryPlace(
      RegistryEntryPlace.fromDTO(dto.entry),
      ResourceFWDTO.fromDTOs(dto.resources)
    );
  }
  constructor(
    public entry: RegistryEntryPlace,
    public resources: ResourceFWDTO[]
  ) {}
}

export class RegistryEntrySaint {
  static fromDTOs(dtos: RegistryEntrySaint[]): RegistryEntrySaint[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => RegistryEntrySaint.fromDTO(dto));
  }
  static fromDTO(dto: RegistryEntrySaint): RegistryEntrySaint {
    if (!dto) {
      return null;
    }
    const preferredName = dto.preferredName
      ? dto.preferredName
      : dto.title
      ? dto.title
      : dto.key;
    return new RegistryEntrySaint(
      dto.key,
      dto.title,
      preferredName,
      dto.encyclopediaLink,
      ArrayHelper.trimStringArray(dto.controlledVocabularies),
      dto.surname,
      dto.forename,
      ArrayHelper.trimStringArray(dto.addNames),
      ArrayHelper.trimStringArray(dto.rolenames),
      dto.birthDate,
      dto.birthPlace,
      ArrayHelper.trimStringArray(dto.residences),
      dto.deathDate,
      dto.deathPlace,
      dto.sex,
      dto.note,
      dto.generatedName
    );
  }
  constructor(
    public key: string,
    public title: string,
    public preferredName: string,
    public encyclopediaLink: string,
    public controlledVocabularies: string[],
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
    public generatedName: string
  ) {}
}

export class IndexEntrySaint {
  static fromDTO(dto: IndexEntrySaint): IndexEntrySaint {
    if (!dto) {
      return null;
    }
    return new IndexEntrySaint(
      RegistryEntrySaint.fromDTO(dto.entry),
      ResourceFWDTO.fromDTOs(dto.resources)
    );
  }
  constructor(
    public entry: RegistryEntrySaint,
    public resources: ResourceFWDTO[]
  ) {}
}

export class RegistryEntryBibleBook {
  static fromDTOs(dtos: RegistryEntryBibleBook[]): RegistryEntryBibleBook[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => RegistryEntryBibleBook.fromDTO(dto));
  }
  static fromDTO(dto: RegistryEntryBibleBook): RegistryEntryBibleBook {
    if (!dto) {
      return null;
    }
    return new RegistryEntryBibleBook(
      dto.book,
      dto.position,
      dto.preferredName,
      dto.counter
    );
  }
  constructor(
    public book: string,
    public position: string,
    public preferredName: string,
    public counter: number
  ) {}
}

export class RegistryEntryBiblePosition {
  static fromDTOs(
    dtos: RegistryEntryBiblePosition[]
  ): RegistryEntryBiblePosition[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => RegistryEntryBiblePosition.fromDTO(dto));
  }
  static fromDTO(dto: RegistryEntryBiblePosition): RegistryEntryBiblePosition {
    if (!dto) {
      return null;
    }
    return new RegistryEntryBiblePosition(
      dto.book,
      dto.position,
      dto.orderPosition,
      ResourceFWDTO.fromDTOs(dto.resources)
    );
  }
  constructor(
    public book: string,
    public position: string,
    public orderPosition: string,
    public resources: ResourceFWDTO[]
  ) {}
  get preferredName(): string {
    return this.book + ' ' + this.position;
  }
}

export class IndexEntryBiblePassages {
  static fromDTO(dto: IndexEntryBiblePassages): IndexEntryBiblePassages {
    if (!dto) {
      return null;
    }
    return new IndexEntryBiblePassages(
      RegistryEntryBibleBook.fromDTO(dto.book),
      RegistryEntryBiblePosition.fromDTOs(dto.entries)
    );
  }
  constructor(
    public book: RegistryEntryBibleBook,
    public entries: RegistryEntryBiblePosition[]
  ) {}
}

export class BiographyFW {
  static fromDTOs(dtos: BiographyFW[]): BiographyFW[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => BiographyFW.fromDTO(dto));
  }
  static fromDTO(dto: BiographyFW): BiographyFW {
    if (!dto) {
      return null;
    }
    return new BiographyFW(
      RegistryEntryPerson.fromDTOs(dto.persons),
      dto.filename,
      dto.title
    );
  }
  constructor(
    public persons: RegistryEntryPerson[],
    public filename: string,
    public title: string
  ) {}
}

export class Biography extends BiographyFW {
  static fromDTO(dto: Biography): Biography {
    if (!dto) {
      return null;
    }
    return new Biography(
      RegistryEntryPerson.fromDTOs(dto.persons),
      dto.filename,
      dto.title,
      IndexEntryPerson.fromDTO(dto.index),
      dto.content,
      dto.toc,
      dto.author
    );
  }
  constructor(
    persons: RegistryEntryPerson[],
    filename: string,
    title: string,
    public index: IndexEntryPerson,
    public content: string,
    public toc: string,
    public author: string
  ) {
    super(persons, filename, title);
  }
}

export enum PhotoType {
  REPRODUCTION = 'REPRODUCTION',
  POSITIVE = 'POSITIVE',
  NEGATIVE = 'NEGATIVE',
}

export enum PhotoColor {
  COLOR = 'COLOR',
  BLACK_WHITE = 'BLACK_WHITE',
}

export class PhotoDocumentItem {
  static fromDTOs(dtos: PhotoDocumentItem[]): PhotoDocumentItem[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => PhotoDocumentItem.fromDTO(dto));
  }
  static fromDTO(dto: PhotoDocumentItem): PhotoDocumentItem {
    if (!dto) {
      return null;
    }
    return new PhotoDocumentItem(
      dto.id,
      dto.signature,
      dto.title,
      dto.dating,
      dto.datingNotBefore,
      dto.datingNotAfter,
      dto.place,
      dto.provenience,
      dto.content,
      dto.copyright,
      dto.pageCount,
      ArrayHelper.trimStringArray(dto.jpegs)
    );
  }
  constructor(
    public id: string,
    public signature: string,
    public title: string,
    public dating: string,
    public datingNotBefore: string,
    public datingNotAfter: string,
    public place: string,
    public provenience: string,
    public content: string,
    public copyright: string,
    public pageCount: string,
    public jpegs: string[]
  ) {}
}

export enum PhotoDocumentGroupType {
  PHOTO = 'PHOTO',
  DOCUMENT = 'DOCUMENT',
}

export class PhotoDocumentGroup {
  static readonly DUMMY: PhotoDocumentGroup = new PhotoDocumentGroup(
    PhotoDocumentGroupType.DOCUMENT,
    'dummy',
    '',
    []
  );

  static fromDTOs(dtos: PhotoDocumentGroup[]): PhotoDocumentGroup[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => PhotoDocumentGroup.fromDTO(dto));
  }
  static fromDTO(dto: PhotoDocumentGroup): PhotoDocumentGroup {
    if (!dto) {
      return null;
    }
    return new PhotoDocumentGroup(
      dto.type,
      dto.groupKey,
      dto.groupTitle,
      PhotoDocumentItem.fromDTOs(dto.items)
    );
  }
  constructor(
    public type: PhotoDocumentGroupType,
    public groupKey: string,
    public groupTitle: string,
    public items: PhotoDocumentItem[]
  ) {}
}

export enum RegistryType {
  CORPORATION = 'CORPORATION',
  PERSON = 'PERSON',
  PLACE = 'PLACE',
  SAINT = 'SAINT',
  BIBLE = 'BIBLE',
}

export class MarkRsRequest {
  constructor(public key: string, public type: RegistryType) {}
}

export class NoteResourceDTO {
  static fromDTO(dto: NoteResourceDTO): NoteResourceDTO {
    if (!dto) {
      return null;
    }
    return new NoteResourceDTO(dto.resourceId, dto.noteId, dto.noteContent);
  }

  constructor(
    public resourceId: string,
    public noteId: string,
    public noteContent: string
  ) {}
}

export class DocumentInfo {
  static fromDTOs(dtos: (DocumentInfo | undefined)[]): DocumentInfo[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => DocumentInfo.fromDTO(dto)).filter((x) => !!x);
  }
  static fromDTO(dto: DocumentInfo | undefined): DocumentInfo | undefined {
    if (!dto) {
      return undefined;
    }
    return new DocumentInfo(dto.filename, dto.anchorName);
  }
  constructor(public filename: string, public anchorName: string) {}
}

export class CorrespInfo {
  static fromDTO(dto: CorrespInfo): CorrespInfo {
    if (!dto) {
      return new CorrespInfo('', []);
    }
    return new CorrespInfo(dto.correspId, DocumentInfo.fromDTOs(dto.places));
  }
  constructor(public correspId: string, public places: DocumentInfo[]) {}
}

export class StatAttrValueOcc {
  static fromDTO(dto: StatAttrValueOcc): StatAttrValueOcc {
    if (!dto) {
      return null;
    }
    return new StatAttrValueOcc(dto.value, dto.occurrences);
  }
  static fromDTOs(dtos: StatAttrValueOcc[]): StatAttrValueOcc[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => StatAttrValueOcc.fromDTO(dto));
  }
  constructor(public value: string, public occurrences: string[]) {}
}

export class StatAttrFullDesc {
  static fromDTO(dto: StatAttrFullDesc): StatAttrFullDesc {
    if (!dto) {
      return null;
    }
    return new StatAttrFullDesc(
      dto.attributeName,
      StatAttrValueOcc.fromDTOs(dto.occurrences)
    );
  }

  static fromDTOs(dtos: StatAttrFullDesc[]): StatAttrFullDesc[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => StatAttrFullDesc.fromDTO(dto));
  }

  constructor(
    public attributeName: string,
    public occurrences: StatAttrValueOcc[]
  ) {}
}

export class StatElementFullDesc {
  static fromDTO(dto: StatElementFullDesc): StatElementFullDesc {
    if (!dto) {
      return null;
    }
    return new StatElementFullDesc(
      dto.elementName,
      StatAttrFullDesc.fromDTOs(dto.attributes)
    );
  }

  static fromDTOs(dtos: StatElementFullDesc[]): StatElementFullDesc[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => StatElementFullDesc.fromDTO(dto));
  }

  constructor(
    public elementName: string,
    public attributes: StatAttrFullDesc[]
  ) {}
}

export class StatRefDesc {
  static fromDTO(dto: StatRefDesc): StatRefDesc {
    if (!dto) {
      return null;
    }
    return new StatRefDesc(dto.destination, dto.occurrences);
  }

  static fromDTOs(dtos: StatRefDesc[]): StatRefDesc[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => StatRefDesc.fromDTO(dto));
  }

  constructor(public destination: string, public occurrences: string[]) {}
}

export class StatRsSingleStat {
  static fromDTO(dto: StatRsSingleStat): StatRsSingleStat {
    if (!dto) {
      return null;
    }
    return new StatRsSingleStat(dto.count, dto.filename);
  }
  static fromDTOs(dtos: StatRsSingleStat[]): StatRsSingleStat[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => StatRsSingleStat.fromDTO(dto));
  }
  constructor(public count: number, public filename: string) {}
}

export class StatRsFullStat {
  static fromDTO(dto: StatRsFullStat): StatRsFullStat {
    if (!dto) {
      return null;
    }
    return new StatRsFullStat(
      dto.total,
      StatRsSingleStat.fromDTOs(dto.singleStats)
    );
  }
  static fromDTOs(dtos: StatRsFullStat[]): StatRsFullStat[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => StatRsFullStat.fromDTO(dto));
  }
  constructor(public total: number, public singleStats: StatRsSingleStat[]) {}
}

export class StatReport {
  static fromDTO(dto: StatReport): StatReport {
    if (!dto) {
      return null;
    }
    return new StatReport(
      StatElementFullDesc.fromDTOs(dto.allElementDescs),
      StatElementFullDesc.fromDTOs(dto.diplElementDescs),
      StatElementFullDesc.fromDTOs(dto.normElementDescs),
      StatRefDesc.fromDTOs(dto.refDescs),
      StatRsFullStat.fromDTO(dto.rsPersonCount),
      StatRsFullStat.fromDTO(dto.rsPlaceCount),
      StatRsFullStat.fromDTO(dto.rsCorpCount),
      StatRsFullStat.fromDTO(dto.footnoteCount)
    );
  }

  constructor(
    public allElementDescs: StatElementFullDesc[],
    public diplElementDescs: StatElementFullDesc[],
    public normElementDescs: StatElementFullDesc[],
    public refDescs: StatRefDesc[],
    public rsPersonCount: StatRsFullStat,
    public rsPlaceCount: StatRsFullStat,
    public rsCorpCount: StatRsFullStat,
    public footnoteCount: StatRsFullStat
  ) {}
}
