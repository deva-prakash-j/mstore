import { sliderOpt } from 'src/app/shared/data';

export const introSlider = {
    ...sliderOpt,
    nav: false,
    autoplayTimeout: 5000,
    autoplay: true,
    dots: false,
    loop: true
}

export const brandSlider = {
    ...sliderOpt,
    nav: false,
    dots: false,
    autoplayTimeout: 2000,
    autoplay: true,
    margin: 0,
    responsive: {
        0: {
            items: 2
        },
        420: {
            items: 3
        },
        600: {
            items: 4
        },
        900: {
            items: 5
        },
        1024: {
            items: 6
        },
        1360: {
            items: 6,
            margin: 30
        }
    }
}

export const newSlider = {
    ...sliderOpt,
    nav: false, 
    dots: false,
    margin: 20,
    loop: false,
    responsive: {
        0: {
            items:2
        },
        480: {
            items:2
        },
        768: {
            items:3
        },
        992: {
            items:4
        },
        1200: {
            items:5,
            dots: true
        }
    }
}

export const trendySlider = {
    ...sliderOpt,
    nav: true, 
    dots: false,
    margin: 20,
    loop: false,
    responsive: {
        0: {
            items:2,
            nav: false
        },
        480: {
            items:2,
            nav: false
        },
        768: {
            items:3,
            nav: false
        },
        992: {
            items:4
        }
    }
}

export const blogSlider = {
    ...sliderOpt,
    nav: false,
    dots: true,
    items: 3,
    margin: 20,
    loop: false,
    responsive: {
        0: {
            items: 1
        },
        576: {
            items: 2
        },
        992: {
            items: 3
        }
    }
}