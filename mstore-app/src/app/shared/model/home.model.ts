export interface Home {
    id: string;
    name: string;
    banner: Banner;
}

export interface Banner {
    main: string[];
    mobile: string[];
}