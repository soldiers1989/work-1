import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Headers, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { CompanyIntro } from './companyintro';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
@Injectable()
export class CompanyIntroService {
   // private indexInfoesUrl = 'api/companyintros';
     private indexInfoesUrl = 'http://localhost:8080/console/api/view/homeList?type=1'
  constructor(private http: Http) { }

  getcompanyintros():  Promise<CompanyIntro[]> {
    // let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' });
    // let options = new RequestOptions({ headers: headers });
    // let headers = new Headers({ 'Content-Type': 'application/json' });
    // let options = new RequestOptions({ headers: headers });
    return this.http.get(this.indexInfoesUrl)
      .toPromise()
      .then(this.extractData)
      // .catch(this.handleError);
  }

  private extractData(res: Response) {
    let body = res.json();
    console.log(body['dataList'] + '2112');

    return body || { };
  }
  private handleError(error: any): Promise<any> {

    // console.error('An error occurred', error);
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Promise.reject(errMsg);
  }
}
