import { CorporationInfo, PersonInfo, PlaceInfo, SaintInfo } from './model';

export enum SortableField {
  NONE = 'NONE',
  KEY = 'KEY',
  NAME = 'NAME',
}

export class FilterAndSortSetting {
  static EMPTY: FilterAndSortSetting = new FilterAndSortSetting(
    '',
    '',
    SortableField.NONE,
    true
  );
  constructor(
    public keyFilter: string,
    public stringFilter: string,
    public sortField: SortableField,
    public ascending: boolean
  ) {}

  sortPerson(): (pi1: PersonInfo, pi2: PersonInfo) => number {
    switch (this.sortField) {
      case SortableField.KEY:
        return (pi1, pi2) => {
          const sort = pi1.key < pi2.key ? -1 : 1;
          return this.ascending ? sort : -sort;
        };
      case SortableField.NAME:
        return (pi1, pi2) => {
          const sort =
            pi1.surname < pi2.surname
              ? -1
              : pi1.surname > pi2.surname
              ? 1
              : pi1.forename < pi2.forename
              ? -1
              : 1;
          return this.ascending ? sort : -sort;
        };
    }
    return () => 0;
  }

  sortSaint(): (pi1: SaintInfo, pi2: SaintInfo) => number {
    switch (this.sortField) {
      case SortableField.KEY:
        return (pi1, pi2) => {
          const sort = pi1.key < pi2.key ? -1 : 1;
          return this.ascending ? sort : -sort;
        };
      case SortableField.NAME:
        return (pi1, pi2) => {
          const sort =
            pi1.surname < pi2.surname
              ? -1
              : pi1.surname > pi2.surname
              ? 1
              : pi1.forename < pi2.forename
              ? -1
              : 1;
          return this.ascending ? sort : -sort;
        };
    }
    return () => 0;
  }

  sortPlace(): (pi1: PlaceInfo, pi2: PlaceInfo) => number {
    switch (this.sortField) {
      case SortableField.KEY:
        return (pi1, pi2) => {
          const sort = pi1.key < pi2.key ? -1 : 1;
          return this.ascending ? sort : -sort;
        };
      case SortableField.NAME:
        return (pi1, pi2) => {
          const sort =
            pi1.stringRepresentation < pi2.stringRepresentation ? -1 : 1;
          return this.ascending ? sort : -sort;
        };
    }
    return () => 0;
  }

  sortCorporation(): (pi1: CorporationInfo, pi2: CorporationInfo) => number {
    switch (this.sortField) {
      case SortableField.KEY:
        return (pi1, pi2) => {
          const sort = pi1.key < pi2.key ? -1 : 1;
          return this.ascending ? sort : -sort;
        };
      case SortableField.NAME:
        return (pi1, pi2) => {
          const sort = pi1.organisation < pi2.organisation ? -1 : 1;
          return this.ascending ? sort : -sort;
        };
    }
    return () => 0;
  }

  public changeKeyFilter(keyFilter: string): FilterAndSortSetting {
    return new FilterAndSortSetting(
      keyFilter,
      this.stringFilter,
      this.sortField,
      this.ascending
    );
  }
  public changeStringFilter(stringFilter: string): FilterAndSortSetting {
    return new FilterAndSortSetting(
      this.keyFilter,
      stringFilter,
      this.sortField,
      this.ascending
    );
  }

  public changeSortField(
    sortField: SortableField,
    ascending?: boolean
  ): FilterAndSortSetting {
    if (sortField === this.sortField) {
      return this.toggleAscending();
    }
    const asc = ascending ? ascending : true;
    return new FilterAndSortSetting(
      this.keyFilter,
      this.stringFilter,
      sortField,
      asc
    );
  }
  public toggleAscending(): FilterAndSortSetting {
    return new FilterAndSortSetting(
      this.keyFilter,
      this.stringFilter,
      this.sortField,
      !this.ascending
    );
  }
}
