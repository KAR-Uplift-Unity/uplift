const client = filestack.init(FILESTACK_API_KEY);

const profileOptions = {
    maxFiles: 1,
    onUploadDone: (response) => {
        const filestackUrl = response.filesUploaded[0].url;
        updateProfileImageUrl(filestackUrl);
    }
};

const postOptions = {
    maxFiles: 5,
    onUploadDone: (response) => {
        const filestackUrls = response.filesUploaded.map(file => file.url);

        document.getElementById('hiddenImageUrls').value = filestackUrls.join(',');

        const previewContainer = document.getElementById('imagePreviewContainer');
        previewContainer.innerHTML = "";

        filestackUrls.forEach(url => {
            const imgDiv = document.createElement('div');
            imgDiv.classList.add("image-preview-wrapper");

            const img = document.createElement('img');
            img.src = url;
            img.alt = "Uploaded image preview";
            img.width = 80;
            img.height = 80;

            const closeButton = document.createElement('span');
            closeButton.innerText = 'X';
            closeButton.classList.add("remove-image-button");
            closeButton.onclick = function() {

                previewContainer.removeChild(imgDiv);

                const currentUrls = document.getElementById('hiddenImageUrls').value.split(',');
                const newUrls = currentUrls.filter(storedUrl => storedUrl !== url);
                document.getElementById('hiddenImageUrls').value = newUrls.join(',');
            };

            imgDiv.appendChild(img);
            imgDiv.appendChild(closeButton);
            previewContainer.appendChild(imgDiv);
        });
    }
};

function openPostImagePicker() {
    client.picker(postOptions).open();
}

function openProfileImagePicker() {
    client.picker(profileOptions).open();
}

function updateProfileImageUrl(url) {
    let token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    let header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/update-profile-image', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
            , [header]: token
        },
        body: JSON.stringify({ imageUrl: url })
    })
        .then(response => response.json())
        .then(data => {
            const successMessage = document.getElementById('success-message');
            const errorMessage = document.getElementById('error-message');

            if (data.message === 'Profile image updated successfully') {
                successMessage.classList.remove('d-none');
                errorMessage.classList.add('d-none');

                const cacheBuster = "?t=" + new Date().getTime();
                document.querySelector('img[alt="Profile Image"]').src = url + cacheBuster;
            } else {
                successMessage.classList.add('d-none');
                errorMessage.classList.remove('d-none');
            }
        })
        .catch(error => {
            console.error("Error updating profile image:", error);
        });
}

function removeExistingImage(element) {
    const imageId = element.getAttribute('data-image-id');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`/posts/${imageId}/delete-image`, {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        }
    }).then(response => {
        if(response.ok) {
            const imageWrapper = element.parentElement;
            imageWrapper.parentElement.removeChild(imageWrapper);
        } else {
            console.error('Failed to delete image with ID:', imageId);
        }
    });
}


