import { Component, Input, OnInit } from '@angular/core';
import { UrlObject } from 'src/app/shared/model/header.model';

@Component({
  selector: 'app-category-menu',
  templateUrl: './category-menu.component.html',
  styleUrls: ['./category-menu.component.scss']
})
export class CategoryMenuComponent implements OnInit {

  constructor() { }

  @Input() brandsList: UrlObject[];

  ngOnInit(): void {
  }

}
