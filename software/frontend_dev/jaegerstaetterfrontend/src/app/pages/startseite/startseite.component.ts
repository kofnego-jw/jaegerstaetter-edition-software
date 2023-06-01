import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import { Router } from '@angular/router';
import {
  faChevronRight,
  faArrowRight,
  faArrowLeft,
  faRedo,
  faArrowsAltH,
} from '@fortawesome/free-solid-svg-icons';
import { CommentDoc } from 'src/app/models/dto';
import { MenuControllerService } from 'src/app/services/http/menu-controller.service';
import { ResourceService } from 'src/app/services/resource.service';

@Component({
  selector: 'app-startseite',
  templateUrl: './startseite.component.html',
  styleUrls: ['./startseite.component.scss'],
})
export class StartseiteComponent implements OnInit {
  faChevronRight = faChevronRight;
  faArrowRight = faArrowRight;
  faArrowLeft = faArrowLeft;
  faRedo = faRedo;
  faArrowsAltH = faArrowsAltH;

  startDoc: CommentDoc = null;

  constructor(
    private router: Router,
    private menuController: MenuControllerService,
    private changeDetector: ChangeDetectorRef,
    private resourceService: ResourceService
  ) {}

  ngOnInit(): void {
    this.menuController
      .startCommentDoc()
      .subscribe((msg) => this.setStartDocument(msg.commentDoc));
  }

  navigateTo(nav: string): void {
    this.router.navigate([nav]);
  }

  navigateToGesamtverzeichnis(): void {
    this.resourceService.clearAllFilters();
    this.router.navigate(['/edition/verzeichnis'], {
      queryParams: { corpus: 'gesamt' },
    });
  }

  setStartDocument(doc: CommentDoc): void {
    this.startDoc = doc;
    this.changeDetector.detectChanges();
  }
}
