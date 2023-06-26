import { Component, OnInit } from '@angular/core';
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
  pathList !: string[]
  param: Record<string, string> = {}

  constructor(private authSvc:AuthService, private httpSvc: HttpService, 
              private actRoute: ActivatedRoute, private router: Router, 
              private localStorSvc: LocalStorageService){}

  ngOnInit(): void {
    this.divertPath = this.actRoute.snapshot.params['path']
    this.divertPath = decodeURI(this.divertPath)
    this.pathList = this.divertPath.split(';')

    if(this.pathList.length>1){
      for(let i=1; i<this.pathList.length; i++){
        const pathParam = this.pathList[i].split("=")
        this.param[pathParam[0]] = pathParam[1]
      }
    }
  }

  submit(username:string, password: string){
    this.authSvc.login(username, password)
          .then((data:any) => {
            // Set JWT into local Storage in browser
            this.localStorSvc.setToken(data)
            alert("Login successfully!!!")
            if(this.pathList.length == 1){
              this.router.navigate([this.pathList[0]]).then(() => {
                window.location.reload();
            })
            } else if(this.pathList.length>1){
              this.router.navigate([this.pathList[0], this.param]).then(() => {
                    window.location.reload();
                })
            } else {
              this.router.navigate(['home']).then(() => {
                    window.location.reload();
                })
            }
          })
            .catch(err => {
              console.error("Error: ",err)
              console.error("Http code:", err.status)
              if(err.status == 0){
                alert("Server down...please contacts administrator.")
              } else {
                alert("Login fail...please try again.")
              }
                }
              )
  }
}
