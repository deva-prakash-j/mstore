import { Component, OnInit } from '@angular/core';
import { introSlider } from '../../pages/home/data';

@Component({
  selector: 'app-main-banner-carousel',
  templateUrl: './main-banner-carousel.component.html',
  styleUrls: ['./main-banner-carousel.component.scss']
})
export class MainBannerCarouselComponent implements OnInit {

  introSlider = introSlider;
  
  constructor() { }

  ngOnInit(): void {
  }

}
