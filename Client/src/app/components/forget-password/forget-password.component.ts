import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/Services/AuthService';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.css']
})
export class ForgetPasswordComponent {

  constructor(private router: Router, private authSvc: AuthService){}

  validEmail(pattern: string){
    let mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
    if(pattern.match(mailformat)){
      return true
    } 
    return false
  }

  submitEmail(email: string){    
    this.authSvc.forgetPassword(email,window.location.origin)
        .then( (res:any) => {alert(res['message'])
                        alert("Please check your mailbox for password reset email!!!")
                        this.router.navigate(['/login']) 
                      })
        .catch((err:any) => alert(err['error']['message']))
  }

}
