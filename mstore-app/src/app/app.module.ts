import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { OwlModule } from 'angular-owl-carousel';
import { ToastrModule } from 'ngx-toastr';
import { NgxProgressiveImageLoaderModule, IImageLoaderOptions } from 'ngx-progressive-image-loader';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './components/shared.module';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    NgbModule,
    RouterModule,
    HttpClientModule,
    OwlModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 2000,
      progressBar: false,
      enableHtml: true,
    }),
    SharedModule,
    NgxProgressiveImageLoaderModule.forRoot(<IImageLoaderOptions>{
      rootMargin: '30px',
      threshold: 0.1,
      filter: 'blur(3px) drop-shadow(0 0 0.75rem crimson)',
      imageRatio: 16 / 9,
      placeholderImageSrc:
        'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNTAgMjUwIj4KICA8cGF0aCBmaWxsPSIjZGQwMDMxIiBkPSJNMTI1IDMwTDMxLjkgNjMuMmwxNC4yIDEyMy4xTDEyNSAyMzBsNzguOS00My43IDE0LjItMTIzLjF6Ii8+CiAgPHBhdGggZmlsbD0iI2MzMDAyZiIgZD0iTTEyNSAzMHYyMi4yLS4xVjIzMGw3OC45LTQzLjcgMTQuMi0xMjMuMUwxMjUgMzB6Ii8+CiAgPHBhdGggZD0iTTEyNSA1Mi4xTDY2LjggMTgyLjZoMjEuN2wxMS43LTI5LjJoNDkuNGwxMS43IDI5LjJIMTgzTDEyNSA1Mi4xem0xNyA4My4zaC0zNGwxNy00MC45IDE3IDQwLjl6IiBmaWxsPSIjZmZmIi8+Cjwvc3ZnPgo='
    })
  ],
  exports: [SharedModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
