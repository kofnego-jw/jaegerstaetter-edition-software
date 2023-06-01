import { Component, Injector } from '@angular/core';
import { createCustomElement } from '@angular/elements';
import { AbComponent } from './elements/ab/ab.component';
import { AbbrExpandComponent } from './elements/abbr-expand/abbr-expand.component';
import { AbbrComponent } from './elements/abbr/abbr.component';
import { AddComponent } from './elements/add/add.component';
import { AnchorComponent } from './elements/anchor/anchor.component';
import { BioFigureComponent } from './elements/bio-figure/bio-figure.component';
import { BioNoteComponent } from './elements/bio-note/bio-note.component';
import { BioRefComponent } from './elements/bio-ref/bio-ref.component';
import { CbComponent } from './elements/cb/cb.component';
import { ChoiceComponent } from './elements/choice/choice.component';
import { CommentComponent } from './elements/comment/comment.component';
import { CorrComponent } from './elements/corr/corr.component';
import { DamageComponent } from './elements/damage/damage.component';
import { DelComponent } from './elements/del/del.component';
import { DivComponent } from './elements/div/div.component';
import { ExpanComponent } from './elements/expan/expan.component';
import { FwComponent } from './elements/fw/fw.component';
import { GapComponent } from './elements/gap/gap.component';
import { HandsInfoComponent } from './elements/hands-info/hands-info.component';
import { HiComponent } from './elements/hi/hi.component';
import { LbComponent } from './elements/lb/lb.component';
import { MetamarkComponent } from './elements/metamark/metamark.component';
import { NoteRefComponent } from './elements/note-ref/note-ref.component';
import { NoteComponent } from './elements/note/note.component';
import { NotehandComponent } from './elements/notehand/notehand.component';
import { OrgNameComponent } from './elements/org-name/org-name.component';
import { PbComponent } from './elements/pb/pb.component';
import { PersNameComponent } from './elements/pers-name/pers-name.component';
import { PlaceNameComponent } from './elements/place-name/place-name.component';
import { QuoteComponent } from './elements/quote/quote.component';
import { RefComponent } from './elements/ref/ref.component';
import { SicCorrComponent } from './elements/sic-corr/sic-corr.component';
import { SicComponent } from './elements/sic/sic.component';
import { StampComponent } from './elements/stamp/stamp.component';
import { SubstComponent } from './elements/subst/subst.component';
import { SuppliedComponent } from './elements/supplied/supplied.component';
import { SurplusComponent } from './elements/surplus/surplus.component';
import { UnclearComponent } from './elements/unclear/unclear.component';
import { ViewAnchorComponent } from './elements/view-anchor/view-anchor.component';
import { CommentdocRefComponent } from './elements/commentdoc-ref/commentdoc-ref.component';
import { ViewPlaceRegistryComponent } from './elements/view-place-registry/view-place-registry.component';
import { SpecialCorrespComponent } from './elements/special-corresp/special-corresp.component';
import { BioindexFigureComponent } from './elements/bioindex-figure/bioindex-figure.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  constructor(injector: Injector) {
    const MyAnchorComponent = createCustomElement(AnchorComponent, {
      injector,
    });
    customElements.define('ffji-anchor', MyAnchorComponent);

    const MyViewAnchorComponent = createCustomElement(ViewAnchorComponent, {
      injector,
    });
    customElements.define('ffji-viewanchor', MyViewAnchorComponent);

    // TEI custom elements

    const MyAbComponent = createCustomElement(AbComponent, {
      injector,
    });
    customElements.define('ffji-ab', MyAbComponent);

    const MyAddComponent = createCustomElement(AddComponent, {
      injector,
    });
    customElements.define('ffji-add', MyAddComponent);

    const MyCbComponent = createCustomElement(CbComponent, {
      injector,
    });
    customElements.define('ffji-cb', MyCbComponent);

    const MyDamageComponent = createCustomElement(DamageComponent, {
      injector,
    });
    customElements.define('ffji-damage', MyDamageComponent);

    const MyDelComponent = createCustomElement(DelComponent, {
      injector,
    });
    customElements.define('ffji-del', MyDelComponent);

    const MyFwComponent = createCustomElement(FwComponent, {
      injector,
    });
    customElements.define('ffji-fw', MyFwComponent);

    const MyGapComponent = createCustomElement(GapComponent, {
      injector,
    });
    customElements.define('ffji-gap', MyGapComponent);

    const MyHiComponent = createCustomElement(HiComponent, {
      injector,
    });
    customElements.define('ffji-hi', MyHiComponent);

    const MyLineBreakComponent = createCustomElement(LbComponent, {
      injector,
    });
    customElements.define('ffji-lb', MyLineBreakComponent);

    const MyMetamarkComponent = createCustomElement(MetamarkComponent, {
      injector,
    });
    customElements.define('ffji-metamark', MyMetamarkComponent);

    const MyNoteComponent = createCustomElement(NoteComponent, {
      injector,
    });
    customElements.define('ffji-note', MyNoteComponent);

    const MyNotehandComponent = createCustomElement(NotehandComponent, {
      injector,
    });
    customElements.define('ffji-notehand', MyNotehandComponent);

    const MyPbComponent = createCustomElement(PbComponent, {
      injector,
    });
    customElements.define('ffji-pb', MyPbComponent);

    const MyQuoteComponent = createCustomElement(QuoteComponent, {
      injector,
    });
    customElements.define('ffji-quote', MyQuoteComponent);

    const MyStampComponent = createCustomElement(StampComponent, {
      injector,
    });
    customElements.define('ffji-stamp', MyStampComponent);

    const MySubstComponent = createCustomElement(SubstComponent, {
      injector,
    });
    customElements.define('ffji-subst', MySubstComponent);

    const MySuppliedComponent = createCustomElement(SuppliedComponent, {
      injector,
    });
    customElements.define('ffji-supplied', MySuppliedComponent);

    const MySurplusComponent = createCustomElement(SurplusComponent, {
      injector,
    });
    customElements.define('ffji-surplus', MySurplusComponent);

    const MyUnclearComponent = createCustomElement(UnclearComponent, {
      injector,
    });
    customElements.define('ffji-unclear', MyUnclearComponent);

    // Normalized

    const MyAbbrComponent = createCustomElement(AbbrComponent, {
      injector,
    });
    customElements.define('ffji-abbr', MyAbbrComponent);

    const MyChoiceComponent = createCustomElement(ChoiceComponent, {
      injector,
    });
    customElements.define('ffji-choice', MyChoiceComponent);

    const MyCorrComponent = createCustomElement(CorrComponent, {
      injector,
    });
    customElements.define('ffji-corr', MyCorrComponent);

    const MyExpanComponent = createCustomElement(ExpanComponent, {
      injector,
    });
    customElements.define('ffji-expan', MyExpanComponent);

    const MyRefComponent = createCustomElement(RefComponent, {
      injector,
    });
    customElements.define('ffji-ref', MyRefComponent);

    const MySicComponent = createCustomElement(SicComponent, {
      injector,
    });
    customElements.define('ffji-sic', MySicComponent);

    const MyPersNameComponent = createCustomElement(PersNameComponent, {
      injector,
    });
    customElements.define('ffji-persname', MyPersNameComponent);

    const MyPlaceNameComponent = createCustomElement(PlaceNameComponent, {
      injector,
    });
    customElements.define('ffji-placename', MyPlaceNameComponent);

    const MyOrgNameComponent = createCustomElement(OrgNameComponent, {
      injector,
    });
    customElements.define('ffji-orgname', MyOrgNameComponent);

    const MyAbbrExpandComponent = createCustomElement(AbbrExpandComponent, {
      injector,
    });
    customElements.define('ffji-abbrexpan', MyAbbrExpandComponent);

    const MySicCorrComponent = createCustomElement(SicCorrComponent, {
      injector,
    });
    customElements.define('ffji-siccorr', MySicCorrComponent);

    const MyDivComponent = createCustomElement(DivComponent, {
      injector,
    });
    customElements.define('ffji-div', MyDivComponent);

    const MyCommentComponent = createCustomElement(CommentComponent, {
      injector,
    });
    customElements.define('ffji-comment', MyCommentComponent);

    const MyHandsInfoComponent = createCustomElement(HandsInfoComponent, {
      injector,
    });
    customElements.define('ffji-handsinfo', MyHandsInfoComponent);

    const MyViewPlaceRegistryComponent = createCustomElement(
      ViewPlaceRegistryComponent,
      {
        injector,
      }
    );
    customElements.define('ffji-viewplace', MyViewPlaceRegistryComponent);

    const MyNoteRefComponent = createCustomElement(NoteRefComponent, {
      injector,
    });
    customElements.define('ffji-note-ref', MyNoteRefComponent);

    const MyBioNoteComponent = createCustomElement(BioNoteComponent, {
      injector,
    });
    customElements.define('ffji-bio-note', MyBioNoteComponent);

    const MyBioFigureComponent = createCustomElement(BioFigureComponent, {
      injector,
    });
    customElements.define('ffji-bio-figure', MyBioFigureComponent);

    const MyBioIndexFigureComponent = createCustomElement(
      BioindexFigureComponent,
      {
        injector,
      }
    );
    customElements.define('ffji-bioindex-figure', MyBioIndexFigureComponent);

    const MyBioRefComponent = createCustomElement(BioRefComponent, {
      injector,
    });
    customElements.define('ffji-bio-ref', MyBioRefComponent);

    const MyCommentdocRefComponent = createCustomElement(
      CommentdocRefComponent,
      {
        injector,
      }
    );
    customElements.define('ffji-commentdoc-ref', MyCommentdocRefComponent);

    const MySpecialCorrespComponent = createCustomElement(
      SpecialCorrespComponent,
      {
        injector,
      }
    );
    customElements.define('ffji-special-corresp', MySpecialCorrespComponent);
  }
}
