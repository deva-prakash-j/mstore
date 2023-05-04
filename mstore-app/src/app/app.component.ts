import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, NavigationStart, Router, RouterOutlet } from '@angular/router';
import { Subscription } from 'rxjs';
import { UtilsService } from './shared/services/utils.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  containerClass = 'container';
	isBottomSticky = false;
	current = "/";

  private subscr: Subscription;

  constructor(private router: Router, public utilsService: UtilsService) {
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
}
