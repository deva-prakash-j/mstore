import { Component, Input, OnInit } from '@angular/core';
import { InnerObject } from 'src/app/shared/model/header.model';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit {

  current: "/"
  @Input() brandsWithCategory: InnerObject[]
  
  constructor() { }

  ngOnInit(): void {
  }

}
