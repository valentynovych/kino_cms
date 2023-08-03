function uploadImage(event) {
    const button = event.target;
    const input = button.nextElementSibling;
    input.click();

    function handleFileSelect() {
        if (input.files && input.files[0]) {
            const imageBlock = button.closest('.image-block');
            const imagePreview = imageBlock.querySelector('img');
            const reader = new FileReader();
            reader.onload = function (e) {
                imagePreview.src = e.target.result;
                input.removeEventListener('change', handleFileSelect);
            };
            reader.readAsDataURL(input.files[0]);
        }
    }
    input.addEventListener('change', handleFileSelect);
}

function clearImage(event) {
    const button = event.target;
    const imageBlock = button.closest('.image-block');
    const img = imageBlock.querySelector('img');
    const input = imageBlock.querySelector('input[type="file"]');

    img.src = '/uploads/image_placeholder.svg';
    input.value = null;

    const file = new File(['ff'], 'empty.png', {
        type: 'image/png',
        lastModified: new Date()
    });
    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(file);
    input.files = dataTransfer.files;
}

document.querySelectorAll('.upload-button').forEach(button => {
    button.addEventListener('click', uploadImage);
});

document.querySelectorAll('.clear-image').forEach(button => {
    button.addEventListener('click', clearImage);
});

document.querySelectorAll('img').forEach(img => {
    img.addEventListener('error', function () {
        img.src = "/uploads/image_placeholder.svg";
        img.onerror = null;
    })
});

