import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms"

 export const orderDate= (control: AbstractControl) => {
    // Need to convert the data string to Date data type, if not the validators can't validate it.
    if(new Date(control.value) > new Date()){
        return {orderDate: true} as ValidationErrors
    } else {
      return null
    }
  }
  
export const nonWhiteSpace = (control: AbstractControl) => {
    if (control.value.trim().length > 0){
      return null} else {
        return {nonWhiteSpace: true} as ValidationErrors
      }
  }

export function minCharacters (num: number): ValidatorFn {
  
    return (control: AbstractControl) => {
      if (control.value.trim().length >= num){
        return null
      } else {
          return {'minCharacters': true} as ValidationErrors
        }
    }
  }

  export const noAsterisk = (control: AbstractControl) => {
    if ( !control.value.includes("*") ){
      return null
    } else {
        return {nonWhiteSpace: true} as ValidationErrors
      }
  }
