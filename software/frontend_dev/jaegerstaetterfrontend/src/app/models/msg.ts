import { CommentDoc, MenuItem, ResourceFWDTO, SearchResult } from './dto';
import { ArrayHelper } from './helper';

export class BasicMsg {
  static fromDTO(dto: BasicMsg): BasicMsg {
    if (!dto) {
      return new BasicMsg('Unknown', true);
    }
    return new BasicMsg(dto.message, dto.hasError);
  }
  constructor(public message: string, public hasError: boolean) {}
}

export class CommentDocMsg extends BasicMsg {
  static fromDTO(dto: CommentDocMsg): CommentDocMsg {
    if (!dto) {
      return null;
    }
    return new CommentDocMsg(
      dto.message,
      dto.hasError,
      CommentDoc.fromDTO(dto.commentDoc)
    );
  }
  constructor(
    message: string,
    hasError: boolean,
    public commentDoc: CommentDoc
  ) {
    super(message, hasError);
  }
}

export class MenuItemLisMsg extends BasicMsg {
  static fromDTO(dto: MenuItemLisMsg): MenuItemLisMsg {
    if (!dto) {
      return null;
    }
    return new MenuItemLisMsg(
      dto.message,
      dto.hasError,
      MenuItem.fromDTOs(dto.menuItemList)
    );
  }
  constructor(
    message: string,
    hasError: boolean,
    public menuItemList: MenuItem[]
  ) {
    super(message, hasError);
  }
}

export class ResourceListMsg extends BasicMsg {
  static fromDTO(dto: ResourceListMsg): ResourceListMsg {
    if (!dto) {
      return null;
    }
    return new ResourceListMsg(
      dto.message,
      dto.hasError,
      ResourceFWDTO.fromDTOs(dto.resources)
    );
  }
  constructor(
    message: string,
    hasError: boolean,
    public resources: ResourceFWDTO[]
  ) {
    super(message, hasError);
  }
}

export class SearchResultMsg extends BasicMsg {
  static fromDTO(dto: SearchResultMsg): SearchResultMsg {
    if (!dto) {
      return null;
    }
    return new SearchResultMsg(
      dto.message,
      dto.hasError,
      SearchResult.fromDTO(dto.searchResult)
    );
  }
  constructor(
    message: string,
    hasError: boolean,
    public searchResult: SearchResult
  ) {
    super(message, hasError);
  }
}

export class AppConfigMsg extends BasicMsg {
  static fromDTO(dto: AppConfigMsg): AppConfigMsg {
    if (!dto) {
      return new AppConfigMsg('unknown', false, false);
    }
    return new AppConfigMsg(dto.message, dto.hasError, dto.edition);
  }
  constructor(message: string, hasError: boolean, public edition: boolean) {
    super(message, hasError);
  }
}

export class StringListMsg extends BasicMsg {
  static fromDTO(dto: StringListMsg): StringListMsg {
    if (!dto) {
      return new StringListMsg('unknown', true, []);
    }
    return new StringListMsg(
      dto.message,
      dto.hasError,
      ArrayHelper.trimStringArray(dto.list)
    );
  }
  constructor(message: string, hasError: boolean, public list: string[]) {
    super(message, hasError);
  }
}
