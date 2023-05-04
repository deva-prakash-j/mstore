import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecommendCollectionComponent } from './recommend-collection.component';

describe('RecommendCollectionComponent', () => {
  let component: RecommendCollectionComponent;
  let fixture: ComponentFixture<RecommendCollectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecommendCollectionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecommendCollectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
