import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import exp from "constants";
import { Observable } from "rxjs";

@Injectable({
    providedIn: "root"
})
export class ApplicationPeriodService{

    private baseUrl = "http://localhost:8080/application-period";

    constructor(private http: HttpClient){}

    public getApplicationPeriodStatus(): Observable<boolean>{
        return this.http.get<boolean>(`${this.baseUrl}/`);
    }

    public reverseApplicationPeriodStatus(): Observable<boolean>{
        return this.http.put<boolean>(`${this.baseUrl}/reverse`, {});
    }

    public setApplicationPeriod(endDate: string): Observable<boolean>{
        let params = new HttpParams()
        params = params.set('endDate', endDate);
        return this.http.put<boolean>(`${this.baseUrl}/`, params);
    }

}