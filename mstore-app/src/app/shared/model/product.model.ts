export interface Product {
    id: string;
    asin: string;
    category: string;
    title: string;
    brand: string;
    featureBullets: string[];
    techSpecs: object;
    price: Price;
    rating: Rating;
    imagesObj: ImageModel;
}

export interface Rating {
    totalReview: string;
    reviewRating: string;
}

export interface Price {
    discounted: boolean;
    currency: string;
    currentPrice: string;
    beforePrice: string;
    savingsAmount: string;
    savingsPercent:	string;
}

export interface ImageModel {
    id: string;
    asin: string;
    isCurrentProduct: boolean;
    title: string;
    images: Image[];
}

export interface Image {
  main: boolean;
  url: string;
}