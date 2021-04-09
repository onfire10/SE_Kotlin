import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CarComponentsPage, CarDeleteDialog, CarUpdatePage } from './car.page-object';

const expect = chai.expect;

describe('Car e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let carComponentsPage: CarComponentsPage;
  let carUpdatePage: CarUpdatePage;
  let carDeleteDialog: CarDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cars', async () => {
    await navBarPage.goToEntity('car');
    carComponentsPage = new CarComponentsPage();
    await browser.wait(ec.visibilityOf(carComponentsPage.title), 5000);
    expect(await carComponentsPage.getTitle()).to.eq('Cars');
    await browser.wait(ec.or(ec.visibilityOf(carComponentsPage.entities), ec.visibilityOf(carComponentsPage.noResult)), 1000);
  });

  it('should load create Car page', async () => {
    await carComponentsPage.clickOnCreateButton();
    carUpdatePage = new CarUpdatePage();
    expect(await carUpdatePage.getPageTitle()).to.eq('Create or edit a Car');
    await carUpdatePage.cancel();
  });

  it('should create and save Cars', async () => {
    const nbButtonsBeforeCreate = await carComponentsPage.countDeleteButtons();

    await carComponentsPage.clickOnCreateButton();

    await promise.all([
      carUpdatePage.setCarTypeInput('carType'),
      carUpdatePage.setBrandInput('brand'),
      carUpdatePage.setKwPowerInput('5'),
      carUpdatePage.setUsdPriceInput('5'),
    ]);

    expect(await carUpdatePage.getCarTypeInput()).to.eq('carType', 'Expected CarType value to be equals to carType');
    expect(await carUpdatePage.getBrandInput()).to.eq('brand', 'Expected Brand value to be equals to brand');
    expect(await carUpdatePage.getKwPowerInput()).to.eq('5', 'Expected kwPower value to be equals to 5');
    expect(await carUpdatePage.getUsdPriceInput()).to.eq('5', 'Expected usdPrice value to be equals to 5');
    const selectedIsRented = carUpdatePage.getIsRentedInput();
    if (await selectedIsRented.isSelected()) {
      await carUpdatePage.getIsRentedInput().click();
      expect(await carUpdatePage.getIsRentedInput().isSelected(), 'Expected isRented not to be selected').to.be.false;
    } else {
      await carUpdatePage.getIsRentedInput().click();
      expect(await carUpdatePage.getIsRentedInput().isSelected(), 'Expected isRented to be selected').to.be.true;
    }

    await carUpdatePage.save();
    expect(await carUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await carComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Car', async () => {
    const nbButtonsBeforeDelete = await carComponentsPage.countDeleteButtons();
    await carComponentsPage.clickOnLastDeleteButton();

    carDeleteDialog = new CarDeleteDialog();
    expect(await carDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Car?');
    await carDeleteDialog.clickOnConfirmButton();

    expect(await carComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
