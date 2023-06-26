import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/Services/AuthService';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  resetCode !: string

  constructor(private actRoute: ActivatedRoute, private authSvc: AuthService,
              private router: Router){}
  
  ngOnInit(): void {
    this.resetCode = this.actRoute.snapshot.params['resetCode']
  }

  validEmail(pattern: string){
    let mailFormat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
    if(pattern.match(mailFormat)){
      return true
    } 
    return false
  }

  resetPassword(email: string, password: string){
    this.authSvc.resetPassword(email, password, this.resetCode)
                .then((res:any) => { alert("Password has been successfully reset!!!") 
                                     alert(res['message'])
                                     this.router.navigate(['/login']) })
                .catch((err: any) => {alert(err['error']['message'])
                                      this.router.navigate(['/forgetPassword']) })
  }


}
