import { Component, OnInit, OnDestroy } from '@angular/core';
import { UtilsService } from 'src/app/shared/services/utils.service';

@Component({
  selector: 'app-header-search',
  templateUrl: './header-search.component.html',
  styleUrls: ['./header-search.component.scss']
})
export class HeaderSearchComponent implements OnInit, OnDestroy {

  products = [];
	searchTerm = "";
	cat = null;
	suggestions = [];
	timer: any;

  constructor(public utilsService: UtilsService) { }

  ngOnInit(): void {
    document.querySelector('body').addEventListener('click', this.closeSearchForm);
  }

  ngOnDestroy(): void {
		document.querySelector('body').removeEventListener('click', this.closeSearchForm);
	}

  searchToggle(e: Event) {
		document.querySelector('.header-search').classList.toggle('show');
		e.stopPropagation();
	}

	showSearchForm(e: Event) {
		document
			.querySelector('.header .header-search')
			.classList.add('show');
		e.stopPropagation();
	}

	closeSearchForm() {
		document
			.querySelector('.header .header-search')
			.classList.remove('show');
	}

	matchEmphasize(name: string) {
		var regExp = new RegExp(this.searchTerm, 'i');
		return name.replace(
			regExp,
			match => '<strong>' + match + '</strong>'
		);
	}

}
