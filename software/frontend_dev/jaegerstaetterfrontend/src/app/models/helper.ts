import {
  RegistryEntryCorporation,
  RegistryEntryPerson,
  RegistryEntryPlace,
  RegistryEntrySaint,
} from './dto';

export class ArrayHelper {
  static trimStringArray(arr: string[]): string[] {
    if (!arr) {
      return [];
    }
    return arr.map((x) => x.normalize('NFC'));
  }
}

export class RegistryEntryView {
  constructor(public key: string, public entryTitle: string) {
    this.compString = entryTitle
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .match(/[A-Z].*$/)[0];
  }
  readonly compString: string;
}

export class RegistryEntryViewGroup {
  static fromRegistryEntryViews(
    entries: RegistryEntryView[]
  ): RegistryEntryViewGroup[] {
    const myGroups: object = entries.reduce((group, current) => {
      const category: string = current.compString
        ? current.compString.charAt(0)
        : '#';
      if (group[category]?.length) {
        group[category].push(current);
      } else {
        group[category] = [current];
      }
      return group;
    }, {});
    return Object.keys(myGroups)
      .map((key) => new RegistryEntryViewGroup(key, myGroups[key]))
      .sort((e1, e2) => (e1.groupName < e2.groupName ? -1 : 1));
  }

  entries: RegistryEntryView[];

  constructor(public groupName: string, entries: RegistryEntryView[]) {
    this.entries = entries.sort((e1, e2) =>
      e1.compString < e2.compString
        ? -1
        : e1.compString === e2.compString
        ? 0
        : 1
    );
  }
}

export class RegistryHelper {
  static personsToRegistryEntryView(
    list: RegistryEntryPerson[]
  ): RegistryEntryView[] {
    return list.map(
      (entry) => new RegistryEntryView(entry.key, entry.generatedOfficialName)
    );
  }

  static groupPersonEntryTogether(
    list: RegistryEntryPerson[]
  ): RegistryEntryViewGroup[] {
    const views = RegistryHelper.personsToRegistryEntryView(list);
    return RegistryEntryViewGroup.fromRegistryEntryViews(views);
  }

  static saintsToRegistryEntryView(
    list: RegistryEntrySaint[]
  ): RegistryEntryView[] {
    return list.map(
      (entry) => new RegistryEntryView(entry.key, entry.generatedName)
    );
  }

  static groupSaintEntryTogether(
    list: RegistryEntrySaint[]
  ): RegistryEntryViewGroup[] {
    const views = RegistryHelper.saintsToRegistryEntryView(list);
    return RegistryEntryViewGroup.fromRegistryEntryViews(views);
  }
  static corporationToRegistryEntryView(
    list: RegistryEntryCorporation[]
  ): RegistryEntryView[] {
    return list.map(
      (entry) => new RegistryEntryView(entry.key, entry.generatedName)
    );
  }

  static groupCorporationEntryTogether(
    list: RegistryEntryCorporation[]
  ): RegistryEntryViewGroup[] {
    const views = RegistryHelper.corporationToRegistryEntryView(list);
    return RegistryEntryViewGroup.fromRegistryEntryViews(views);
  }
  static placesToRegistryEntryView(
    list: RegistryEntryPlace[]
  ): RegistryEntryView[] {
    return list.map(
      (entry) => new RegistryEntryView(entry.key, entry.generatedName)
    );
  }

  static groupPlaceEntryTogether(
    list: RegistryEntryPlace[]
  ): RegistryEntryViewGroup[] {
    const views = RegistryHelper.placesToRegistryEntryView(list);
    return RegistryEntryViewGroup.fromRegistryEntryViews(views);
  }
}
