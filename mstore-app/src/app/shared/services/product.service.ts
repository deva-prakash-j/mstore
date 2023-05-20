import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  /**
   * Get Recommended Products
   */
  public fetchRecommendedProducts(): Observable<any> {
    return this.http.get(`${environment.mstoreApiUrl}${environment.recommendedProductEndpoint}`);
  }

  public fetchModelData(name): Observable<any> {
    let params = {
      'name': name
    };
    return this.http.get(`${environment.mstoreApiUrl}${environment.modelDataEndpoint}`, {params});
  }
}
