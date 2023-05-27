import { Component, OnInit, Input } from '@angular/core';
import { introSlider } from '../../pages/home/data';
import { Banner } from 'src/app/shared/model/home.model';


@Component({
  selector: 'app-main-banner-carousel',
  templateUrl: './main-banner-carousel.component.html',
  styleUrls: ['./main-banner-carousel.component.scss']
})
export class MainBannerCarouselComponent implements OnInit {

  @Input() bannerObj: Banner;
  introSlider = introSlider;
  
  constructor() { 
  }

  ngOnInit(): void {
    
  }

}
