import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContentfulService {

  constructor(private http: HttpClient) { }

  /**
   * Get Recommended Products
   */
  public fetchContentfulData(contentType): Observable<any> {
    let params = {
      'contentType': contentType
    };
    return this.http.get(`${environment.mstoreApiUrl}${environment.contentfulDataEndpoint}`, {params});
  }

  public formatContentfulData(list) {
    let data = {};
    if(list && list.length > 0) {
      list.forEach(element => {
        data[element?.sys?.id] = element?.fields
      });
    }
    return data;
  }
}
