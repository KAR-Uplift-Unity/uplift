const client = filestack.init(FILESTACK_API_KEY);

const profileOptions = {
    maxFiles: 1,
    onUploadDone: (response) => {
        const filestackUrl = response.filesUploaded[0].url;
        updateProfileImageUrl(filestackUrl);
    }
};

const postOptions = {
    maxFiles: 10,
    onUploadDone: (response) => {
        const filestackUrls = response.filesUploaded.map(file => file.url);
        document.getElementById('hiddenImageUrls').value = filestackUrls.join(',');
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