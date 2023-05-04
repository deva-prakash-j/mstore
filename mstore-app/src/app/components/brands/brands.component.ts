import { Component, OnInit } from '@angular/core';
import { brandSlider } from '../../pages/home/data';

@Component({
  selector: 'app-brands',
  templateUrl: './brands.component.html',
  styleUrls: ['./brands.component.scss']
})
export class BrandsComponent implements OnInit {

  brandSlider = brandSlider
  
  constructor() { }

  ngOnInit(): void {
  }

}
