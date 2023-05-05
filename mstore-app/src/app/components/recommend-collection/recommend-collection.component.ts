import { Component, OnInit, Input } from '@angular/core';
import { Product } from 'src/app/shared/model/product.model';

@Component({
  selector: 'app-recommend-collection',
  templateUrl: './recommend-collection.component.html',
  styleUrls: ['./recommend-collection.component.scss']
})
export class RecommendCollectionComponent implements OnInit {

  @Input() loaded = false;
  @Input() products:Product[] = [];

  constructor() { }

  ngOnInit(): void {
  }

}
