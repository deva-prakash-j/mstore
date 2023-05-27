import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, NavigationStart, Router, RouterOutlet } from '@angular/router';
import { Subscription } from 'rxjs';
import { UtilsService } from './shared/services/utils.service';
import { ProductService } from './shared/services/product.service';
import { Header } from './shared/model/header.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  containerClass = 'container';
	isBottomSticky = false;
	current = "/";
	headerData: Header;

  private subscr: Subscription;

  constructor(private router: Router, public utilsService: UtilsService, public productService: ProductService) {
		this.utilsService.toggleLoader()
		this.subscr = this.router.events.subscribe(event => {
			if (event instanceof NavigationStart) {
				this.current = event.url;
				if (this.current.includes('fullwidth')) {
					this.containerClass = 'container-fluid';
				} else {
					this.containerClass = 'container';
				}
			} else if (event instanceof NavigationEnd) {
				this.current = event.url;
				if (this.current.includes('fullwidth')) {
					this.containerClass = 'container-fluid';
				} else {
					this.containerClass = 'container';
				}
			}
		});
	}

	ngOnInit(): void {
		this.fetchHeaderData()
	}

	ngOnDestroy(): void {
		this.subscr.unsubscribe();
	}

  @HostListener('window:resize', ['$event'])
	handleKeyDown(event: Event) {
		this.resizeHandle()
	}
	
	@HostListener('window: scroll', ['$event'])
	onWindowScroll(e: Event) {
		this.utilsService.setStickyHeader();
	}

	prepareRoute (outlet: RouterOutlet) {
		return outlet && outlet.isActivated && outlet.activatedRoute && outlet.activatedRoute.url;
	}

	resizeHandle() {
		if (this.current.includes('product/default') && window.innerWidth > 992)
			this.isBottomSticky = true;
		else
			this.isBottomSticky = false;
	}

	hideMobileMenu() {
		document.querySelector('body').classList.remove('mmenu-active');
		document.querySelector('html').style.overflowX = 'unset';
	}

	fetchHeaderData() {
		this.productService.fetchHeaderData().subscribe(result=> {
			if(result && result.data) {
				result = result.data[0];
				let brands = result?.brands;
				let categories = result?.categories;
				this.headerData = {brands, categories, brandsWithCategory: result.brandsWithCategory};
				this.utilsService.toggleLoader('hide')
			}
		})
	}
}
