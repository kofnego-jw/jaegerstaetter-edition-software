import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeiDocumentModalComponent } from './tei-document-modal.component';

describe('TeiDocumentModalComponent', () => {
  let component: TeiDocumentModalComponent;
  let fixture: ComponentFixture<TeiDocumentModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeiDocumentModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeiDocumentModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
