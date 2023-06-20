import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { minCharacters } from 'src/app/Constants';
import { AuthService } from 'src/app/Services/AuthService';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form !: FormGroup

  constructor(private fb: FormBuilder, private authSvc: AuthService, private router: Router){}

  ngOnInit(): void {
    this.form = this.createForm()
  }

  createForm(){
    return this.fb.group({
      name: this.fb.control<string>('',[Validators.required, minCharacters(3)]),
      email: this.fb.control<string>('',[Validators.required, Validators.email]),
      username: this.fb.control<string>('',[Validators.required, minCharacters(2)]),
      password: this.fb.control<string>('',[Validators.required, minCharacters(8)])
    })
  }

  submit(){
    console.info(this.form.value)

    const appUser = {
      name: this.form.value["name"],
      username: this.form.value["username"],
      email: this.form.value["email"],
      password: this.form.value["password"]
    }

    this.authSvc.register(appUser)
        .then( (data:any) => {alert(data['message'])
                              alert("Please login.")
                              this.router.navigate(['/login'])
          })
        .catch( err => {console.info("Registration error: ", err)
                        alert(err['message'])
                        })
  }


}
