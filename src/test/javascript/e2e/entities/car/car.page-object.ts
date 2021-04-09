import { element, by, ElementFinder } from 'protractor';

export class CarComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-car div table .btn-danger'));
  title = element.all(by.css('jhi-car div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class CarUpdatePage {
  pageTitle = element(by.id('jhi-car-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  carTypeInput = element(by.id('field_carType'));
  brandInput = element(by.id('field_brand'));
  kwPowerInput = element(by.id('field_kwPower'));
  usdPriceInput = element(by.id('field_usdPrice'));
  isRentedInput = element(by.id('field_isRented'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setCarTypeInput(carType: string): Promise<void> {
    await this.carTypeInput.sendKeys(carType);
  }

  async getCarTypeInput(): Promise<string> {
    return await this.carTypeInput.getAttribute('value');
  }

  async setBrandInput(brand: string): Promise<void> {
    await this.brandInput.sendKeys(brand);
  }

  async getBrandInput(): Promise<string> {
    return await this.brandInput.getAttribute('value');
  }

  async setKwPowerInput(kwPower: string): Promise<void> {
    await this.kwPowerInput.sendKeys(kwPower);
  }

  async getKwPowerInput(): Promise<string> {
    return await this.kwPowerInput.getAttribute('value');
  }

  async setUsdPriceInput(usdPrice: string): Promise<void> {
    await this.usdPriceInput.sendKeys(usdPrice);
  }

  async getUsdPriceInput(): Promise<string> {
    return await this.usdPriceInput.getAttribute('value');
  }

  getIsRentedInput(): ElementFinder {
    return this.isRentedInput;
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class CarDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-car-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-car'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
