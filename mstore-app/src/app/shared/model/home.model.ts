export interface Home {
    id: string;
    name: string;
    items: Banner;
}

export interface Banner {
    main: string[];
    mobile: string[];
}