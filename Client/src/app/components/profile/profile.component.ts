import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { minCharacters, noAsterisk } from 'src/app/Constants';
import { AuthService } from 'src/app/Services/AuthService';
import { LocalStorageService } from 'src/app/_helper/LocalStorage.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  username !: string
  form !: FormGroup
  disableToggle: boolean = true

  constructor(private fb: FormBuilder, private authSvc: AuthService, private router: Router, 
              private lsSvc: LocalStorageService){}

  ngOnInit(): void {
    // @ts-ignore
    this.username = localStorage.getItem("username")
    if(this.username != null){
      this.authSvc.getUser(this.username)
      .then((data:any) => 
              this.form = this.createForm(data['name'], data['email'],data['username'], data['id'])
      )
    }
  }

  createForm(nameIn ?:string, emailIn?: string, usernameIn?: string, idIn?:number){
    let name="", email="", username = "", id=0
    if(nameIn && emailIn && usernameIn && idIn){
      name = nameIn;
      email = emailIn;
      username = usernameIn;
      id = idIn;
    }
    return this.fb.group({
      id: this.fb.control<number>(id),
      name: this.fb.control<string>(name,[Validators.required, minCharacters(3)]),
      email: this.fb.control<string>(email,[Validators.required, Validators.email]),
      username: this.fb.control<string>(username,[Validators.required, minCharacters(2)]),
      password: this.fb.control<string>('********',[Validators.required, minCharacters(8), noAsterisk])
    })
  }

  submit(){
    document.getElementById("popup")!.style.display = "block"
  }

  setToggle(event: boolean){
    this.disableToggle = !event
  }

  updateUser(password: string){
    const appUser = {
      id: this.form.value["id"],
      name: this.form.value["name"],
      username: this.form.value["username"],
      email: this.form.value["email"],
      password: this.form.value["password"]
    }

    document.getElementById("popup")!.style.display = "none";
    (document.getElementById("passwordInput") as HTMLInputElement).value = "";
    
    this.authSvc.verifyUser(this.username, password)
                .then((data:any) => {
                  this.authSvc.updateUser(appUser)
                      .then((data:any) => {
                                alert(data['message'])
                                alert("Please log in again.")
                                this.lsSvc.removeTokenInfo()
                                this.router.navigate(['/login'])
                                            .then(() => window.location.reload())
                                        })
                      .catch((err:any) => alert("Error: "+ err.error["message"]))
                })
                .catch((err:any) => alert("Error: "+ err.error["message"]))
  }

  cancelUpdate(){
    document.getElementById("popup")!.style.display = "none";
    (document.getElementById("passwordInput") as HTMLInputElement).value = "";
  }
}
