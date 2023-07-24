function validatePassword () {
    let pass1 = document.querySelector('#password'),
    pass2 = document.querySelector('#password-check')
    pass1.addEventListener('input', function () {
        this.value !== pass2.value ? pass2.setCustomValidity('Password incorrect') : pass2.setCustomValidity('')
    })
    pass2.addEventListener('input', function (e) {
        this.value !== pass1.value ? this.setCustomValidity('Password incorrect') : this.setCustomValidity('')
    })
}
