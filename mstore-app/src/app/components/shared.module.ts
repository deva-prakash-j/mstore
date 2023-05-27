import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OwlModule } from 'angular-owl-carousel';
import { TranslateModule } from '@ngx-translate/core';
import { LazyLoadImageModule } from 'ng-lazyload-image';
import { HeaderComponent } from './header/header.component';
import { HeaderSearchComponent } from './header/plugins/header-search/header-search.component';
import { CompareMenuComponent } from './header/plugins/compare-menu/compare-menu.component';
import { WishlistMenuComponent } from './header/plugins/wishlist-menu/wishlist-menu.component';
import { CartMenuComponent } from './header/plugins/cart-menu/cart-menu.component';
import { SafeContentPipe } from '../shared/pipe/safe-content.pipe';
import { CategoryMenuComponent } from './header/plugins/category-menu/category-menu.component';
import { MainMenuComponent } from './header/plugins/main-menu/main-menu.component';
import { MobileButtonComponent } from './header/plugins/mobile-button/mobile-button.component';
import { MobileMenuComponent } from './header/plugins/mobile-menu/mobile-menu.component';
import { FooterComponent } from './footer/footer.component';
import { HomeComponent } from '../pages/home/home.component';
import { MainBannerCarouselComponent } from './main-banner-carousel/main-banner-carousel.component';
import { PopularCategoriesComponent } from './popular-categories/popular-categories.component';
import { BrandsComponent } from './brands/brands.component';
import { FeatureComponent } from './feature/feature.component';
import { ImageComponent } from './image/image.component';
import { RecommendCollectionComponent } from './recommend-collection/recommend-collection.component';
import { ProductCardComponent } from './product-card/product-card.component';
import { BgParallaxDirective } from '../shared/directives/bg-parallax.directive';
import { ContentAnimDirective } from '../shared/directives/content-anim.directive';
import { TabClickDirective } from '../shared/directives/custom-tab-click.directive';
import { ProductHoverDirective } from '../shared/directives/product-hover.directive';
import { TrendingComponent } from './trending/trending.component';
import { NewCollectionComponent } from './new-collection/new-collection.component';
import { AccountMenuComponent } from './header/plugins/account-menu/account-menu.component';


@NgModule({
  declarations: [
    HeaderComponent,
    HeaderSearchComponent,
    CompareMenuComponent,
    WishlistMenuComponent,
    CartMenuComponent,
    SafeContentPipe,
    CategoryMenuComponent,
    MainMenuComponent,
    MobileButtonComponent,
    MobileMenuComponent,
    FooterComponent,
    HomeComponent,
    MainBannerCarouselComponent,
    PopularCategoriesComponent,
    BrandsComponent,
    FeatureComponent,
    ImageComponent,
    RecommendCollectionComponent,
    ProductCardComponent,
    BgParallaxDirective,
    ContentAnimDirective,
    TabClickDirective,
    ProductHoverDirective,
    TrendingComponent,
    NewCollectionComponent,
    AccountMenuComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
		NgbModule,
		TranslateModule,
		OwlModule,
		LazyLoadImageModule,
  ],
  exports: [
    HeaderComponent,
    HeaderSearchComponent,
    CompareMenuComponent,
    WishlistMenuComponent,
    CartMenuComponent,
    SafeContentPipe,
    MobileMenuComponent,
    FooterComponent,
    HomeComponent,
    ImageComponent,
    BgParallaxDirective,
    ContentAnimDirective,
    TabClickDirective,
    ProductHoverDirective,
    AccountMenuComponent,
  ]
})
export class SharedModule { }
