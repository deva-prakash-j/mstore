import { Component, OnInit, Input } from '@angular/core';
import { Banner, Home } from 'src/app/shared/model/home.model';
import { Product } from 'src/app/shared/model/product.model';
import { ProductService } from 'src/app/shared/services/product.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  @Input() loaded = false;
  @Input() products:Product[] = [];
  bannerObj: Banner;

  constructor(public productService: ProductService) { }

  ngOnInit(): void {
    this.fetchModelDataForHome()
    this.getProducts()
  }

  fetchModelDataForHome() {
    this.productService.fetchModelData(environment.homeContentType).subscribe(result => {
      if(result) {
        let home: Home = result[0];
        this.bannerObj = home.banner;
        console.log(this.bannerObj);
      }
    })
  }

  getProducts(): void {
    this.productService.fetchRecommendedProducts().subscribe(result => {
      if(result) {
        this.products = result
        this.loaded = true;
      }
    })
  }



}
