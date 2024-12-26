const fullNameInput = document.getElementById('fullNameInput');
const phoneNumberInput = document.getElementById('phoneNumberInput');
const emailInput = document.getElementById('emailInput');
const birthDateInput = document.getElementById('birthDateInput');
const passwordInput = document.getElementById('passwordInput');

const shopNameInput = document.getElementById('shopNameInput');
const shopDescriptionInput = document.getElementById('shopDescriptionInput');

const sellerShopDataDiv = document.getElementById('sellerShopDataDiv');

const userTypeChoices = document.getElementById('user-type-choices');

function userTypesSelectOnChange(selectElement) {
    const selectedValue = selectElement.value;

    if (selectedValue === 'seller') {
        sellerShopDataDiv.classList.remove('d-none');
        sellerShopDataDiv.classList.add('d-flex');
    } else if (selectedValue === 'client') {
        sellerShopDataDiv.classList.remove('d-flex');
        sellerShopDataDiv.classList.add('d-none');
    }
};

function registerButtonOnClick() {

}