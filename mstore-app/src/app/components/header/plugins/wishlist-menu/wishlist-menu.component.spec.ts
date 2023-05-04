import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistMenuComponent } from './wishlist-menu.component';

describe('WishlistMenuComponent', () => {
  let component: WishlistMenuComponent;
  let fixture: ComponentFixture<WishlistMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WishlistMenuComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
