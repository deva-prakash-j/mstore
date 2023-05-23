export interface Header {
    brands: UrlObject[];
    categories: UrlObject[];
    brandsWithCategory: InnerObject[];
}

export interface InnerObject extends UrlObject {
    brands: UrlObject[];
}

export interface UrlObject {
    urlSlug: string;
    displayText: string;
}