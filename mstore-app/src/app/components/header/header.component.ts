import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Header } from 'src/app/shared/model/header.model';
import { UtilsService } from 'src/app/shared/services/utils.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Input() containerClass = "container";
  @Input() headerData: Header;

  wishCount = 0;

  constructor(public activeRoute: ActivatedRoute, public utilsService: UtilsService) { }

  ngOnInit(): void {
  }

  showLoginModal(event: Event): void {
		event.preventDefault();
		//this.modalService.showLoginModal();
	}

}
