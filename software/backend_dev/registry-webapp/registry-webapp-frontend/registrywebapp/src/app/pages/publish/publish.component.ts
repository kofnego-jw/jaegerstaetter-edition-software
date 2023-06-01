import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, take } from 'rxjs';
import { TeiDocumentDTO } from 'src/app/model';
import { AppService } from 'src/app/services/app.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { TeiDocumentModalComponent } from 'src/app/modals/tei-document-modal/tei-document-modal.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-publish',
  templateUrl: './publish.component.html',
  styleUrls: ['./publish.component.scss'],
})
export class PublishComponent implements OnInit, OnDestroy {
  destroyed$: Subject<any>;
  registryDocs: TeiDocumentDTO[] = [];

  constructor(private app: AppService, private modalService: BsModalService) {
    this.destroyed$ = new Subject();
  }

  ngOnDestroy(): void {
    this.destroyed$.next(true);
  }

  ngOnInit(): void {
    this.loadRegistryDocs();
  }

  loadRegistryDocs(): void {
    this.app
      .publishFindRegistryDocs()
      .subscribe((docs) => (this.registryDocs = docs));
  }

  peekTeiDoc(): void {
    this.app
      .publishPeekTeiDoc()
      .pipe(take(1))
      .subscribe({
        next: (doc) => this.viewDoc(doc, true),
      });
  }

  viewDoc(doc: TeiDocumentDTO, isPreview: boolean): void {
    const modalComponent = this.modalService.show(TeiDocumentModalComponent, {
      class: 'modal-xl',
      initialState: { teiDocument: doc, isPreview: isPreview },
    });
    if (isPreview) {
      modalComponent.content.doPublish.pipe(take(1)).subscribe({
        next: (bool) => {
          if (bool) {
            const yesNoModal = this.modalService.show(
              ConfirmationModalComponent,
              {
                initialState: {
                  title: 'Registereinträge hochladen?',
                  message:
                    'Wollen Sie die Einträge wirklich hochladen? Es wird eine neue Version des Register-Dokuments erstellt.',
                },
              }
            );
            yesNoModal.content.onSubmit.pipe(take(1)).subscribe({
              next: (yesNo) => {
                if (yesNo) {
                  const versionNum = this.registryDocs?.length
                    ? this.registryDocs[0].version
                    : 0;
                  this.app
                    .publishPublishRegistry(versionNum)
                    .subscribe({ next: () => this.loadRegistryDocs() });
                }
              },
            });
          }
        },
      });
    }
  }
}
