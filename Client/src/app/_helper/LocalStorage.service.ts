import { Injectable } from "@angular/core";

@Injectable()
export class LocalStorageService{

    setToken(data: any){
        localStorage.setItem('id_token', data["accessToken"]);
        localStorage.setItem('username', data["username"]);
        localStorage.setItem('token_expiry', data["accessTokenExpiry"]);
        localStorage.setItem('token_expiry_long', data["accessTokenExpiryLong"]);
    }

    removeTokenInfo(){
        localStorage.removeItem("id_token")
        localStorage.removeItem("username")
        localStorage.removeItem("token_expiry_long")
        localStorage.removeItem("token_expiry")
      }
}