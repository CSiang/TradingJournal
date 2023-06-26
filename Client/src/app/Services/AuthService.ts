import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";

// const baseUrl = "http://localhost:8080";
const baseUrl = "https://trading-journal-production.up.railway.app";


@Injectable()
export class AuthService{

    constructor(private http: HttpClient){}

    login(username:string, password: string) {
        const url = baseUrl + "/login";
        const body = new HttpParams()
            .set('username', username)
            .set('password', password);
      
        return lastValueFrom(this.http.post(url,
          body.toString(),
          {
            headers: new HttpHeaders()
              .set('Content-Type', 'application/x-www-form-urlencoded')
          }
        ));
      }

    register(appUser: any){
      const url = baseUrl + "/auth/register";

      return lastValueFrom(this.http.post(url, appUser))
    }

    getUser(username: string){
      const url =  baseUrl + `/auth/user/${username}`
      return lastValueFrom(this.http.get(url))
    }

    verifyUser(username: string, password: string){
      const url =  baseUrl + "/auth/verify"
      const data = {username: username, password: password}
      
      return lastValueFrom(this.http.post(url, data))
    }

    updateUser(appUser: any){
      const url = baseUrl + "/auth/update";

      return lastValueFrom(this.http.post(url, appUser))
    }

    forgetPassword(email: string, domain: string){
      const url = baseUrl + "/auth/forgetPassword";
      const param = new HttpParams().set("email", email)
                                    .set("domain", domain)

      return lastValueFrom(this.http.get(url, {params: param}))
    }

    resetPassword(email: string, password: string, resetCode: string){
      const url = baseUrl + "/auth/resetPassword";
      const param = new HttpParams().set("email", email)
                                    .set("password", password)
                                    .set("resetCode", resetCode)

      return lastValueFrom(this.http.get(url, {params: param}))
    }

}