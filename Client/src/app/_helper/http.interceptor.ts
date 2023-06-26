import { HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable, catchError, throwError } from "rxjs";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private router: Router){}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        const idToken = localStorage.getItem("id_token");

        if(idToken) {
            req = req.clone({
                headers: req.headers.set("Authorization", ("Bearer " + idToken) )
            })
        }
        return next.handle(req).pipe(
            catchError( err => {console.info("Error from interceptor: ",err)
                    if(err.status == 403){
                        alert("Please login.")
                        this.router.navigate(['login', {path: this.router.url}]).then(
                            () =>{ window.location.reload}
                        )
                    }
                    // return throwError(() => err.error)
                    // It is better to throw back the err to the targeted ts. err.error will only pass the error message to the ts but not the err.status, hence the catch in the ts won't function well.
                    return throwError(() => err)
            })
        )
    }
}

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ];