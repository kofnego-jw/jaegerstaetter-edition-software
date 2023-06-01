import { ReplaySubject } from 'rxjs';
import { EditionCorpus, EditionTimePeriod, ResourceFWDTO } from './dto';

export enum ResourceListType {
  TOC,
  SEARCH,
  REGISTRY,
}

export class ResourceListCurrentState {
  static readonly EMPTY: ResourceListCurrentState =
    new ResourceListCurrentState(0, 0, false, false, '');
  constructor(
    public totalCount: number,
    public currNumber: number,
    public hasPrev: boolean,
    public hasNext: boolean,
    public backTitle: string
  ) {}
}

export interface ResourceListState {
  setCurrent(res: ResourceFWDTO): void;
  getCurr(): ResourceFWDTO;
  next(): void;
  prev(): void;
  getCurrentState$(): ReplaySubject<ResourceListCurrentState>;
}

export enum BriefPanelView {
  FACSIMILE = 'FACSIMILE',
  DIPLO = 'DIPLO',
  NORM = 'NORM',
  XML = 'XML',
}

export enum PanelPosition {
  ONE = 'ONE',
  TWO = 'TWO',
}

export class FacsimileDescDTO {
  static fromDTO(dto: FacsimileDescDTO): FacsimileDescDTO {
    if (!dto) {
      return new FacsimileDescDTO([]);
    }
    const urls = dto.facsimileUrls ? dto.facsimileUrls : [];
    return new FacsimileDescDTO(urls);
  }
  constructor(public facsimileUrls: string[]) {}
}

export class MsIdentifier {
  static readonly EMPTY: MsIdentifier = new MsIdentifier('', '', '', '', '');
  static fromDTO(dto: MsIdentifier): MsIdentifier {
    if (!dto) {
      return MsIdentifier.EMPTY;
    }
    return new MsIdentifier(
      dto.institution,
      dto.repository,
      dto.collection,
      dto.idno,
      dto.altIdno
    );
  }
  constructor(
    public institution: string,
    public repository: string,
    public collection: string,
    public idno: string,
    public altIdno: string
  ) {}
}

export class SourceDesc {
  static readonly EMPTY: SourceDesc = new SourceDesc(
    MsIdentifier.EMPTY,
    '',
    '',
    '',
    '',
    ''
  );
  static fromDTO(dto: SourceDesc): SourceDesc {
    if (!dto) {
      return SourceDesc.EMPTY;
    }
    return new SourceDesc(
      MsIdentifier.fromDTO(dto.msIdentifier),
      dto.msContents,
      dto.physDesc,
      dto.objectType,
      dto.origin,
      dto.provenance
    );
  }
  constructor(
    public msIdentifier: MsIdentifier,
    public msContents: string,
    public physDesc: string,
    public objectType: string,
    public origin: string,
    public provenance: string
  ) {}
  getIdno(): string {
    if (this.msIdentifier) {
      return this.msIdentifier.idno;
    }
    return '';
  }
}

export class TitleStmt {
  static readonly EMPTY: TitleStmt = new TitleStmt('', '');
  static fromDTO(dto: TitleStmt): TitleStmt {
    if (!dto) {
      return TitleStmt.EMPTY;
    }
    return new TitleStmt(dto.title, dto.author);
  }
  constructor(public title: string, public author: string) {}
}

export class PersonInfo {
  static fromDTOs(dtos: PersonInfo[]): PersonInfo[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => PersonInfo.fromDTO(dto));
  }
  static fromDTO(dto: PersonInfo): PersonInfo {
    if (!dto) {
      return null;
    }
    return new PersonInfo(dto.name, dto.ref, dto.key);
  }
  constructor(public name: string, public ref: string, public key: string) {}
  getAsString(): string {
    let resultString = '';
    if (this.name) {
      resultString += this.name;
      resultString += ' ';
    }
    if (this.key) {
      resultString += this.key;
      resultString += ' ';
    }
    if (this.ref) {
      resultString += this.ref;
    }
    return resultString;
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
    return new PlaceInfo(dto.name, dto.key);
  }
  constructor(public name: string, public key: string) {}
  getAsString(): string {
    let resultString = '';
    if (this.name) {
      resultString += this.name;
      resultString += ' ';
    }
    if (this.key) {
      resultString += this.key;
    }
    return resultString;
  }
}

export class CorrespAction {
  static readonly EMPTY: CorrespAction = new CorrespAction([], [], '');
  static fromDTO(dto: CorrespAction): CorrespAction {
    if (!dto) {
      return CorrespAction.EMPTY;
    }
    return new CorrespAction(
      PersonInfo.fromDTOs(dto.personList),
      PlaceInfo.fromDTOs(dto.placeList),
      dto.date
    );
  }
  constructor(
    public personList: PersonInfo[],
    public placeList: PlaceInfo[],
    public date: string
  ) {}

  getAsString(): string {
    let resultString = '';
    if (this.personList.length) {
      this.personList.forEach(
        (person) => (resultString += person.getAsString())
      );
      resultString += ' ';
    }
    if (this.placeList.length) {
      this.placeList.forEach((place) => (resultString += place.getAsString()));
      resultString += ' ';
    }
    if (this.date) {
      resultString += this.date;
    }
    return resultString;
  }

  getFirstPerson(): string {
    if (this.personList && this.personList.length) {
      return this.personList[0].name;
    }
    return '';
  }

  getDate(): string {
    if (this.date) {
      return this.date;
    }
    return '';
  }
}

export class CorrespContext {
  static readonly EMPTY: CorrespContext = new CorrespContext([], []);
  static fromDTO(dto: CorrespContext): CorrespContext {
    if (!dto) {
      return CorrespContext.EMPTY;
    }
    return new CorrespContext(dto.prevLetterFiles, dto.nextLetterFiles);
  }
  constructor(
    public prevLetterFiles: string[],
    public nextLetterFiles: string[]
  ) {}
}

export class CorrespDesc {
  static readonly EMPTY: CorrespDesc = new CorrespDesc(
    CorrespAction.EMPTY,
    CorrespAction.EMPTY,
    CorrespContext.EMPTY
  );
  static fromDTO(dto: CorrespDesc): CorrespDesc {
    if (!dto) {
      return CorrespDesc.EMPTY;
    }
    return new CorrespDesc(
      CorrespAction.fromDTO(dto.sentAction),
      CorrespAction.fromDTO(dto.receivedAction),
      CorrespContext.fromDTO(dto.correspContext)
    );
  }
  constructor(
    public sentAction: CorrespAction,
    public receivedAction: CorrespAction,
    public correspContext: CorrespContext
  ) {}
}

export class LetterDTO {
  static fromDTOs(dtos: LetterDTO[]): LetterDTO[] {
    if (!dtos) {
      return [];
    }
    return dtos.map((dto) => LetterDTO.fromDTO(dto));
  }
  static fromDTO(dto: LetterDTO): LetterDTO {
    if (!dto) {
      return null;
    }
    return new LetterDTO(
      dto.id,
      dto.title,
      dto.dating,
      SourceDesc.fromDTO(dto.sourceDesc),
      CorrespDesc.fromDTO(dto.correspDesc),
      TitleStmt.fromDTO(dto.titleStmt),
      FacsimileDescDTO.fromDTO(dto.facsimiles),
      dto.diploUrl,
      dto.normUrl,
      dto.xmlUrl
    );
  }
  constructor(
    public id: string,
    public title: string,
    public dating: string,
    public sourceDesc: SourceDesc,
    public correspDesc: CorrespDesc,
    public titleStmt: TitleStmt,
    public facsimiles: FacsimileDescDTO,
    public diploUrl: string,
    public normUrl: string,
    public xmlUrl: string
  ) {}

  getSentActionAsString(): string {
    if (!this.correspDesc || !this.correspDesc.sentAction) {
      return '';
    }
    return this.correspDesc.sentAction.getAsString();
  }
  getReceivedActionAsString(): string {
    if (!this.correspDesc || !this.correspDesc.receivedAction) {
      return '';
    }
    return this.correspDesc.receivedAction.getAsString();
  }
  getFirstSender(): string {
    if (this.correspDesc && this.correspDesc.sentAction) {
      return this.correspDesc.sentAction.getFirstPerson();
    }
    return '';
  }
  getFirstRecipient(): string {
    if (this.correspDesc && this.correspDesc.receivedAction) {
      return this.correspDesc.receivedAction.getFirstPerson();
    }
    return '';
  }
  getSentDate(): string {
    if (this.correspDesc && this.correspDesc.sentAction) {
      return this.correspDesc.sentAction.getDate();
    }
    return '';
  }
  getReceivedDate(): string {
    if (this.correspDesc && this.correspDesc.receivedAction) {
      return this.correspDesc.receivedAction.getDate();
    }
    return '';
  }
  getIdno(): string {
    if (this.sourceDesc) {
      return this.sourceDesc.getIdno();
    }
    return '';
  }
  getPlaceKeys(): string {
    if (this.correspDesc) {
      const keys = [];
      if (this.correspDesc.sentAction) {
        keys.push(...this.correspDesc.sentAction.placeList.map((p) => p.key));
      }
      if (this.correspDesc.receivedAction) {
        keys.push(
          ...this.correspDesc.receivedAction.placeList.map((p) => p.key)
        );
      }
      return keys.join('; ');
    }
    return '';
  }
}

export interface ViewPageBreakEvent {
  pageNumber: number;
  facs: string;
  byClick: boolean;
  source: string;
}

export interface LetterFilter {
  filterLetters(letters: LetterDTO[]): LetterDTO[];
  getId(): string;
}

export class NoRestrictionFilter implements LetterFilter {
  constructor() {}
  filterLetters(letters: LetterDTO[]): LetterDTO[] {
    if (!letters) {
      return [];
    }
    return letters;
  }
  getId(): string {
    return 'NoRestriction';
  }
}

export class SentActionLetterFilter implements LetterFilter {
  constructor(public queryString: string) {}
  private filter(letter: LetterDTO): boolean {
    return letter.getSentActionAsString().indexOf(this.queryString) >= 0;
  }
  filterLetters(letters: LetterDTO[]): LetterDTO[] {
    if (!letters) {
      return [];
    }
    return letters.filter((letter) => this.filter(letter));
  }
  getId(): string {
    return 'sentAction_' + this.queryString;
  }
}

export class ReceivedActionLetterFilter implements LetterFilter {
  constructor(public queryString: string) {}
  private filter(letter: LetterDTO): boolean {
    return letter.getReceivedActionAsString().indexOf(this.queryString) >= 0;
  }
  filterLetters(letters: LetterDTO[]): LetterDTO[] {
    if (!letters) {
      return [];
    }
    return letters.filter((letter) => this.filter(letter));
  }
  getId(): string {
    return 'receivedAction_' + this.queryString;
  }
}

export class PlaceLetterFilter implements LetterFilter {
  constructor(private queryString: string) {}
  private filter(letter: LetterDTO): boolean {
    return letter.getPlaceKeys().indexOf(this.queryString) >= 0;
  }
  filterLetters(letters: LetterDTO[]): LetterDTO[] {
    if (!letters) {
      return [];
    }
    return letters.filter((letter) => this.filter(letter));
  }
  getId(): string {
    return 'place_' + this.queryString;
  }
}

export class AndLetterFilter implements LetterFilter {
  constructor(private filters: LetterFilter[]) {}
  filterLetters(letters: LetterDTO[]): LetterDTO[] {
    if (!letters) {
      return [];
    }
    if (!this.filters || !this.filters.length) {
      return letters;
    }
    let restLetters = letters.slice();
    for (let filter of this.filters) {
      restLetters = filter.filterLetters(restLetters);
    }
    return restLetters;
  }
  getId(): string {
    return 'And';
  }
}

export class OrLetterFilter implements LetterFilter {
  constructor(private filters: LetterFilter[]) {}
  filterLetters(letters: LetterDTO[]): LetterDTO[] {
    if (!letters) {
      return [];
    }
    if (!this.filters || !this.filters.length) {
      return letters;
    }
    let result: LetterDTO[] = [];
    for (let filter of this.filters) {
      const currentLetters = filter.filterLetters(letters);
      currentLetters.forEach((letter) => {
        if (result.indexOf(letter) < 0) {
          result.push(letter);
        }
      });
    }
    return result;
  }
  getId(): string {
    return 'Or';
  }
}

export enum LetterSorterField {
  NONE = 'Keine Sortierung',
  SENDER = 'Absender*in',
  SENT_DATE = 'Schreibzeit',
  RECEIVER = 'Empfänger*in',
  RECEIVED_DATE = 'Empfangszeit',
  IDNO = 'Signatur',
}

export class LetterSorter {
  constructor(public field: LetterSorterField, public asc: boolean) {}

  setSortField(field: LetterSorterField): void {
    if (!field) {
      return;
    }
    this.field = field;
  }

  setAsc(asc: boolean): void {
    this.asc = asc;
  }

  private compare(): (x1: LetterDTO, x2: LetterDTO) => number {
    return (x1: LetterDTO, x2: LetterDTO) => {
      let s1: string, s2: string;
      switch (this.field) {
        case LetterSorterField.SENDER:
          s1 = x1.getFirstSender();
          s2 = x2.getFirstSender();
          break;
        case LetterSorterField.SENT_DATE:
          s1 = x1.getSentDate();
          s2 = x2.getSentDate();
          break;
        case LetterSorterField.RECEIVER:
          s1 = x1.getFirstRecipient();
          s2 = x2.getFirstRecipient();
          break;
        case LetterSorterField.RECEIVED_DATE:
          s1 = x1.getReceivedDate();
          s2 = x2.getReceivedDate();
          break;
        case LetterSorterField.IDNO:
        default:
          s1 = x1.getIdno();
          s2 = x2.getIdno();
      }
      const result: number = s1 === s2 ? 0 : s1 < s2 ? -1 : 1;
      return this.asc ? result : -result;
    };
  }

  sort(letters: LetterDTO[]): LetterDTO[] {
    if (this.field === LetterSorterField.NONE) {
      if (this.asc) {
        return letters;
      } else {
        letters.slice().reverse();
      }
    }
    return letters.slice().sort(this.compare());
  }
}

export class PaginationInfo<Type> {
  /**
   * Page number, starting from 0
   */
  pageNumber: number;
  hitFrom: number;
  hitTo: number;

  constructor(private elements: Array<Type>, public pageSize: number) {
    this.pageNumber = 0;
  }

  totalHits(): number {
    return this.elements.length;
  }

  getPageCount(): number {
    return Math.ceil(this.elements.length / this.pageSize);
  }

  getPage(pn: number): Array<Type> {
    if (pn < 0) {
      return [];
    }
    if (pn >= this.getPageCount()) {
      return [];
    }
    this.pageNumber = pn;
    this.hitFrom = this.pageSize * pn;
    this.hitTo = this.pageSize * (pn + 1);
    if (this.hitTo > this.elements.length) {
      this.hitTo = this.elements.length;
    }
    return this.elements.slice(this.hitFrom, this.hitTo);
  }

  getHitFrom(): number {
    return this.hitFrom + 1;
  }

  getHitTo(): number {
    return this.hitTo;
  }
}

export class BiographyDTO {
  static fromDTO(dto: BiographyDTO): BiographyDTO {
    if (!dto) {
      return null;
    }
    return new BiographyDTO(
      dto.id,
      dto.key,
      dto.surname,
      dto.forename,
      dto.addname,
      dto.rolename,
      dto.biography,
      dto.birth,
      dto.birthPlace,
      dto.death,
      dto.deathPlace,
      dto.sex,
      dto.nationality,
      dto.note,
      dto.gnd
    );
  }
  constructor(
    public id: string,
    public key: string,
    public surname: string,
    public forename: string,
    public addname: string,
    public rolename: string,
    public biography: string,
    public birth: string,
    public birthPlace: string,
    public death: string,
    public deathPlace: string,
    public sex: string,
    public nationality: string,
    public note: string,
    public gnd: string
  ) {}
}

export class PlaceDTO {
  static fromDTO(dto: PlaceDTO): PlaceDTO {
    if (!dto) {
      return null;
    }
    return new PlaceDTO(dto.id, dto.key, dto.name, dto.lat, dto.lng);
  }
  constructor(
    public id: string,
    public key: string,
    public name: string,
    public lat: number,
    public lng: number
  ) {}
}

export enum ResourceFilterCategory {
  CORPUS = 'CORPUS',
  TIMEPERIOD = 'TIMEPERIOD',
  AUTHOR = 'AUTHOR',
  RECIPIENT = 'RECIPIENT',
  PLACE = 'PLACE',
  OBJECTTYPE = 'OBJECTTYPE',
  SPECIAL = 'SPECIAL',
}

export class ResourceFilter {
  static readonly TAKE_ALL = new ResourceFilter(
    ResourceFilterCategory.CORPUS,
    'Gesamtverzeichnis',
    (fw) => true
  );
  static readonly TAKE_FROM_FRANZ = new ResourceFilter(
    ResourceFilterCategory.CORPUS,
    'Schriften von Jägerstätter',
    (fw) => fw.corpora.indexOf(EditionCorpus.FROM_FRANZ) >= 0
  );
  static readonly TAKE_TO_FRANZ = new ResourceFilter(
    ResourceFilterCategory.CORPUS,
    'Briefe an Jägerstätter',
    (fw) => fw.corpora.indexOf(EditionCorpus.TO_FRANZ) >= 0
  );
  static readonly TAKE_ABOUT_FRANZ = new ResourceFilter(
    ResourceFilterCategory.CORPUS,
    'Briefe über Jägerstätter',
    (fw) => fw.corpora.indexOf(EditionCorpus.ABOUT_FRANZ) >= 0
  );
  static readonly TAKE_CORRESPONDENCE = new ResourceFilter(
    ResourceFilterCategory.CORPUS,
    'Korrespondenzen',
    (fw) => fw.corpora.indexOf(EditionCorpus.CORRESPONDENCE) >= 0
  );

  static readonly TAKE_EARLY = new ResourceFilter(
    ResourceFilterCategory.TIMEPERIOD,
    'Frühzeit',
    (fw) => fw.periods.indexOf(EditionTimePeriod.EARLY) >= 0
  );
  static readonly TAKE_MILITARY = new ResourceFilter(
    ResourceFilterCategory.TIMEPERIOD,
    'Militärzeit',
    (fw) => fw.periods.indexOf(EditionTimePeriod.MILITARY) >= 0
  );
  static readonly TAKE_TO_REFUSAL = new ResourceFilter(
    ResourceFilterCategory.TIMEPERIOD,
    'Bis zur Verweigerung',
    (fw) => fw.periods.indexOf(EditionTimePeriod.TO_REFUSAL) >= 0
  );
  static readonly TAKE_PRISON = new ResourceFilter(
    ResourceFilterCategory.TIMEPERIOD,
    'Gefängnis',
    (fw) => fw.periods.indexOf(EditionTimePeriod.PRISON) >= 0
  );
  static readonly TAKE_AFTERLIFE = new ResourceFilter(
    ResourceFilterCategory.TIMEPERIOD,
    'Nachleben',
    (fw) => fw.periods.indexOf(EditionTimePeriod.AFTERLIFE) >= 0
  );

  static createMatchAuthor(author: string): ResourceFilter {
    return new ResourceFilter(
      ResourceFilterCategory.AUTHOR,
      author,
      (fw) => fw.authors.indexOf(author) >= 0
    );
  }
  static createMatchRecipient(key: string): ResourceFilter {
    return new ResourceFilter(
      ResourceFilterCategory.RECIPIENT,
      key,
      (fw) => fw.recipients.indexOf(key) >= 0
    );
  }
  static createMatchPlace(key: string): ResourceFilter {
    return new ResourceFilter(
      ResourceFilterCategory.PLACE,
      key,
      (fw) => fw.places.indexOf(key) >= 0
    );
  }
  static createMatchObjectType(key: string): ResourceFilter {
    return new ResourceFilter(
      ResourceFilterCategory.OBJECTTYPE,
      key,
      (fw) => fw.objectTypes.indexOf(key) >= 0
    );
  }
  static createPartnerFilter(
    party1: string[],
    party2: string[]
  ): ResourceFilter {
    return new ResourceFilter(
      ResourceFilterCategory.SPECIAL,
      'correspParty',
      (fw) => {
        if (!party1.length || !party2.length) {
          return false;
        }
        const party1AuthorFilters = party1.map((x) =>
          ResourceFilter.createMatchAuthor(x.normalize('NFC'))
        );
        const party1RecipientFilters = party1.map((x) =>
          ResourceFilter.createMatchRecipient(x.normalize('NFC'))
        );
        const party2AuthorFilters = party2.map((x) =>
          ResourceFilter.createMatchAuthor(x.normalize('NFC'))
        );
        const party2RecipientFilters = party2.map((x) =>
          ResourceFilter.createMatchRecipient(x.normalize('NFC'))
        );
        const party1Author = party1AuthorFilters
          .map((filter) => filter.include(fw))
          .reduce((boolPrev, boolCurr) => boolPrev || boolCurr);
        const party2Recipient = party2RecipientFilters
          .map((filter) => filter.include(fw))
          .reduce((boolPrev, boolCurr) => boolPrev || boolCurr);
        const party2Author = party2AuthorFilters
          .map((filter) => filter.include(fw))
          .reduce((boolPrev, boolCurr) => boolPrev || boolCurr);
        const party1Recipient = party1RecipientFilters
          .map((filter) => filter.include(fw))
          .reduce((boolPrev, boolCurr) => boolPrev || boolCurr);
        return (
          (party1Author && party2Recipient) || (party2Author && party1Recipient)
        );
      }
    );
  }

  static readonly CORPUS_FILTERS: ResourceFilter[] = [
    ResourceFilter.TAKE_ALL,
    ResourceFilter.TAKE_FROM_FRANZ,
    ResourceFilter.TAKE_TO_FRANZ,
    ResourceFilter.TAKE_ABOUT_FRANZ,
    ResourceFilter.TAKE_CORRESPONDENCE,
  ];

  static readonly PERIOD_FILTERS: ResourceFilter[] = [
    ResourceFilter.TAKE_EARLY,
    ResourceFilter.TAKE_MILITARY,
    ResourceFilter.TAKE_TO_REFUSAL,
    ResourceFilter.TAKE_PRISON,
    ResourceFilter.TAKE_AFTERLIFE,
  ];

  constructor(
    public category: ResourceFilterCategory,
    public key: string,
    private test: (fw: ResourceFWDTO) => boolean
  ) {}
  include(resource: ResourceFWDTO): boolean {
    return this.test(resource);
  }
}

export class PredefinedCorpus {
  static readonly COMPLETE = new PredefinedCorpus(
    'gesamt',
    'Gesamtverzeichnis',
    ResourceFilter.TAKE_ALL
  );
  static readonly BY_FRANZ = new PredefinedCorpus(
    'von',
    'Schriften von Jägerstätter',
    ResourceFilter.TAKE_FROM_FRANZ
  );
  static readonly TO_FRANZ = new PredefinedCorpus(
    'an',
    'Briefe an Jägerstätter',
    ResourceFilter.TAKE_TO_FRANZ
  );
  static readonly ABOUT_FRANZ = new PredefinedCorpus(
    'ueber',
    'Briefe über Jägerstätter',
    ResourceFilter.TAKE_ABOUT_FRANZ
  );
  static readonly CORRESP = new PredefinedCorpus(
    'korrespondenz',
    'Korrespondenz',
    ResourceFilter.TAKE_CORRESPONDENCE
  );

  static readonly PREDEFINED = [
    PredefinedCorpus.COMPLETE,
    PredefinedCorpus.BY_FRANZ,
    PredefinedCorpus.TO_FRANZ,
    PredefinedCorpus.ABOUT_FRANZ,
    PredefinedCorpus.CORRESP,
  ];

  constructor(
    public pathComponent: string,
    public title: string,
    public filter: ResourceFilter
  ) {}
}

export class ResourceFilterInfo {
  constructor(public counter: number, public filter: ResourceFilter) {}
}

export class SingleColoringInfo {
  constructor(public hand: string, public color: string) {}
}

export class ColoringInfo {
  constructor(
    public defaultColor: string,
    public coloringInfos: SingleColoringInfo[]
  ) {}
}
