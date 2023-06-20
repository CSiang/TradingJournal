import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService } from './_helper/LocalStorage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  username : any
  
  constructor(private router: Router, private localStrSvc: LocalStorageService){}
  
  ngOnInit(): void {
    const expiryDateLong = localStorage.getItem('token_expiry_long');
    const date = new Date(Number(expiryDateLong))
    const now = new Date()
    
    // Check if JWT token expired. If expired then delete JWT info in localStorage.
    if(expiryDateLong){
      if(date < now){
        this.localStrSvc.removeTokenInfo()
        alert("Login token is expired!!!")
        this.router.navigate(['login', {path: this.router.url}])
                    .then( () =>{ window.location.reload} )
      } else {
        this.username = localStorage.getItem("username")
      }
    }
  }

  logout(){
    this.localStrSvc.removeTokenInfo()
    alert("You have been logged out successfully.")
    this.router.navigate(['/']).then(() => {
          window.location.reload();
      })
  }
}
