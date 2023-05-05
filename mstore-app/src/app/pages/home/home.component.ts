import { Component, OnInit, Input } from '@angular/core';
import { Product } from 'src/app/shared/model/product.model';
import { ProductService } from 'src/app/shared/services/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  @Input() loaded = false;
  @Input() products:Product[] = [];

  constructor(public productService: ProductService) { }

  ngOnInit(): void {
    this.productService.fetchRecommendedProducts().subscribe(result => {
      if(result) {
        this.products = result
        this.loaded = true;
      }
    })
  }

}
