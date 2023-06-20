import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/Services/AuthService';
import { HttpService } from 'src/app/Services/HttpServices';
import { LocalStorageService } from 'src/app/_helper/LocalStorage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  content!: string
  divertPath !: string

  constructor(private authSvc:AuthService, private httpSvc: HttpService, 
              private actRoute: ActivatedRoute, private router: Router, 
              private localStorSvc: LocalStorageService){}

  ngOnInit(): void {
    this.divertPath = this.actRoute.snapshot.params['path']
    if(this.divertPath && localStorage.length>0){
      this.localStorSvc.removeTokenInfo()
      window.location.reload()
    }
  }

  submit(username:string, password: string){
    console.log("Username: ", username)
    console.log("Password: ", password)

    this.authSvc.login(username, password)
          .then((data:any) => {
            console.info("Data: ",data)
            console.log("Access Token ", data["accessToken"])
            console.log("Expiry Date: ", data["accessTokenExpiry"])
            // Set JWT into local Storage in browser
            this.localStorSvc.setToken(data)
            
            alert("Login successfully!!!")
              if(this.divertPath){
                this.router.navigate([this.divertPath]).then(() => {
                      window.location.reload();
                  })
              } else {
                this.router.navigate(['home']).then(() => {
                      window.location.reload();
                  })
              }
          })
            .catch(err => {
              console.info("Error: ",err)
              console.info("Http code:", err.status)
              if(err.status == 0){
                alert("Server down...please contacts administrator.")
              } else {
                alert("Login fail...please try again.")
              }
                }
              )
  }
}
