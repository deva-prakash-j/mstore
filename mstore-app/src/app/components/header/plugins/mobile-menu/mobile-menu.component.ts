import { Component, Input, OnInit } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Header } from 'src/app/shared/model/header.model';

declare var $: any;

@Component({
  selector: 'app-mobile-menu',
  templateUrl: './mobile-menu.component.html',
  styleUrls: ['./mobile-menu.component.scss']
})
export class MobileMenuComponent implements OnInit {

	searchTerm = "";
	@Input() headerData: Header;

	private subscr: Subscription;

	constructor(private router: Router) {
		this.subscr = this.router.events.subscribe(event => {
			if (event instanceof NavigationStart) {
				this.hideMobileMenu();
			}
		});
	}

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
		this.subscr.unsubscribe();
	}

  submenuToggle(e) {
		const parent: HTMLElement = e.target.closest('li');
		const submenu: HTMLElement = parent.querySelector('ul');

		if (parent.classList.contains('open')) {
			$(submenu).slideUp(300, function () {
				parent.classList.remove('open');
			});
		}
		else {
			$(submenu).slideDown(300, function () {
				parent.classList.add('open');
			});
		}

		e.preventDefault();
		e.stopPropagation();
	}

	hideMobileMenu() {
		document.querySelector('body').classList.remove('mmenu-active');
		document.querySelector('html').removeAttribute('style');
	}

}
