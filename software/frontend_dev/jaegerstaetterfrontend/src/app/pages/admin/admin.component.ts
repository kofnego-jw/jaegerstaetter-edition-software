import { Component, OnDestroy, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AdminPasswordModalComponent } from 'src/app/modals/admin-password-modal/admin-password-modal.component';
import { InfoModalComponent } from 'src/app/modals/info-modal/info-modal.component';
import { PasswordDTO } from 'src/app/models/dto';
import { ApplicationService } from 'src/app/services/application.service';
import { AdminControllerService } from 'src/app/services/http/admin-controller.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss'],
})
export class AdminComponent implements OnInit {
  constructor(
    private adminController: AdminControllerService,
    private modalService: BsModalService
  ) {}

  ngOnInit(): void {}

  showResult(msg: string): void {
    const modalRef = this.modalService.show(InfoModalComponent);
    modalRef.content.message = msg;
  }

  showPasswordFormForClone(): void {
    const modalRef = this.modalService.show(AdminPasswordModalComponent);
    modalRef.content.onClose$.subscribe({
      next: (passwd) => this.cloneEditionToPreview(passwd),
    });
  }

  showPasswordFormForCloneAndIngest(): void {
    const modalRef = this.modalService.show(AdminPasswordModalComponent);
    modalRef.content.onClose$.subscribe({
      next: (passwd) => this.cloneEditionAndIngestToPreview(passwd),
    });
  }

  showPasswordFormForIngest(): void {
    const modalRef = this.modalService.show(AdminPasswordModalComponent);
    modalRef.content.onClose$.subscribe({
      next: (passwd) => this.ingestToEdition(passwd),
    });
  }

  cloneEditionToPreview(passwd: string): void {
    const passwdDTO = new PasswordDTO(passwd);
    this.adminController.cloneEditionToPreview(passwdDTO).subscribe((msg) => {
      if (msg.hasError) {
        this.showResult(
          'Daten der Edition konnte nicht kopiert werden: ' + msg.message
        );
      } else {
        this.showResult(
          'Daten der Edition wurden auf die Preview-Datenbank kopiert.'
        );
      }
    });
  }

  ingestToEdition(passwd: string): void {
    const passwdDTO = new PasswordDTO(passwd);
    this.adminController.ingestToEdition(passwdDTO).subscribe((msg) => {
      if (msg.hasError) {
        this.showResult(
          'Neue Daten konnte nicht der Edition-Datenbank eingespielt werden: ' +
            msg.message
        );
      } else {
        this.showResult('Neue Daten wurde der Edition-Datenbank eingespielt.');
      }
    });
  }

  cloneEditionAndIngestToPreview(passwd: string): void {
    const passwdDTO = new PasswordDTO(passwd);
    this.adminController
      .cloneEditionAndIngestToPreview(passwdDTO)
      .subscribe((msg) => {
        if (msg.hasError) {
          this.showResult(
            'Daten der Edition konnte nicht kopiert werden, und neue Daten konnte nicht eingespielt werden: ' +
              msg.message
          );
        } else {
          this.showResult(
            'Daten der Edition wurden auf die Preview-Datenbank kopiert. Und neue Daten wurde zum Preview eingespielt.'
          );
        }
      });
  }
}
