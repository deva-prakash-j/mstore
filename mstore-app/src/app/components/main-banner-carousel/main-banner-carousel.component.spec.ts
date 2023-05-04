import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainBannerCarouselComponent } from './main-banner-carousel.component';

describe('MainBannerCarouselComponent', () => {
  let component: MainBannerCarouselComponent;
  let fixture: ComponentFixture<MainBannerCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MainBannerCarouselComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MainBannerCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
