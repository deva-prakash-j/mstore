import { Component, OnInit, Input } from '@angular/core';
import { Product } from 'src/app/shared/model/product.model';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss']
})
export class ProductCardComponent implements OnInit {

  @Input() product: Product;
  rating: number;

  constructor() { }

  ngOnInit(): void {
    if(this.product) {
      this.rating = Number(this.product.rating.reviewRating)
    }
  }
}
