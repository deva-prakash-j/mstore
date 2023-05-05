import { Component, OnInit, Input } from '@angular/core';
import { trendySlider } from 'src/app/pages/home/data';
import { Product } from 'src/app/shared/model/product.model';

@Component({
  selector: 'app-trending',
  templateUrl: './trending.component.html',
  styleUrls: ['./trending.component.scss']
})
export class TrendingComponent implements OnInit {

  @Input() products:Product[] = [];
  @Input() loaded = false;

  categories = [['all'], ['accessories'], ['cameras', 'camcorders'], ['computers', 'tablets'], ['entertainment']];
  	titles = ['All', 'Accessories', 'Cameras & Camcorders', 'Computers & Tablets', 'Entertainment']
	sliderOption = trendySlider;

  constructor() { }

  ngOnInit(): void {
  }

}
