const profileInputs = document.getElementsByClassName('profile-input');
const profileDataElements = document.getElementsByClassName('profile-data');

const fullNameH5 = document.getElementById('fullNameH5');
const phoneNumberH5 = document.getElementById('phoneNumberH5');
const emailH5 = document.getElementById('emailH5');
const birthDateH5 = document.getElementById('birthDateH5');

const fullNameInput = document.getElementById('fullNameInput');
const phoneNumberInput = document.getElementById('phoneNumberInput');
const emailInput = document.getElementById('emailInput');
const birthDateInput = document.getElementById('birthDateInput');

const editButton1Div = document.getElementById('editButton1Div');
const editPageButtonsDiv = document.getElementById('editPageButtonsDiv');

document.addEventListener('DOMContentLoaded', () => {
    fullNameInput.value = fullNameH5.textContent;
    phoneNumberInput.value = phoneNumberH5.textContent;
    emailInput.value = emailH5.textContent;
    birthDateInput.value = birthDateH5.textContent;
});

function editButton1OnClick() {
    for (const data of profileDataElements) {
        data.classList.add('d-none');
    }

    for (const input of profileInputs) {
        input.classList.remove('d-none');
    }

    editButton1Div.classList.add('d-none');
    editPageButtonsDiv.classList.remove('d-none');
    editPageButtonsDiv.classList.add('d-flex');
}

function cancelButtonOnClick() {
    for (const input of profileInputs) {
        input.classList.add('d-none');
    }

    for (const data of profileDataElements) {
        data.classList.remove('d-none');
    }

    editPageButtonsDiv.classList.add('d-none');
    editButton1Div.classList.remove('d-none');
}

function editButton2OnClick() {
    
}