// var fileInputs = document.querySelectorAll(".file-input");
// var uploadButtons = document.querySelectorAll(".upload-button");
//
// for (var i = 0; i < fileInputs.length; i++) {
//     uploadButtons[i].addEventListener("click", function() {
//         var fileInput = document.querySelector(".file-input");
//         fileInput.click();
//
//     });
// }

const images = document.querySelectorAll('img');
for (let i = 0; i < images.length; i++){
    images[i].addEventListener('error', function (ev) {
        ev.target.src = "/uploads/image_placeholder.svg";
        ev.onerror = null;
    })
}