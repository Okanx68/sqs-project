import { test, expect } from '@playwright/test';


test('wirdSeiteRichtigAufgebaut', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await expect(page.getByRole('heading', { name: 'Brewery Detail List' })).toBeVisible();
    await expect(page.getByPlaceholder('Enter City Name')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Search' })).toBeVisible();
    await expect(page.locator('#noDataContent')).toBeVisible();
    await expect(page.getByText('No data fetched, please')).toBeVisible();
    await page.getByText('External API used for this').click();
    await expect(page.getByRole('img', { name: 'Open Brewery DB' })).toBeVisible();
});

test('sucheWirdOhneEingabeEinerStadtAusgeführt', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await page.getByPlaceholder('Enter City Name').click();
    await expect(page.getByPlaceholder('Enter City Name')).toBeEmpty();
    await page.getByRole('button', { name: 'Search' }).click();
    await page.getByText('No data fetched, please').click();
});

test('sucheMitRichtigEingegeberStadtWirdErfolgreichAusgeführt', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('San Diego');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByRole('cell', { name: 'Name' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Brewery Type' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Address' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'City', exact: true })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'State Province' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Postal Code' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Country' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Phone' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Website URL' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'State', exact: true })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Street' })).toBeVisible();

});

test('sucheMitFalchEingegebenerStadtWirdNichtErfolgreichAusgeführt', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Sann Diego');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByText('City not found. Try again.')).toBeVisible();
    await expect(page.locator('#noDataContent')).toBeVisible();
});

test('verlinkungDerExternenAPIVerbindetErfolgreichZurBreweryAPI', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await page.getByRole('link', { name: 'Open Brewery DB' }).click();
    await expect(page.locator('#svelte div').filter({ hasText: 'Open Brewery DB Open main menu Breweries Docs FAQ Projects About Newsletter' }).nth(2)).toBeVisible();
});

test('erstFalscheDannRichtigEingegebeneStadtsuche', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Sann Diego');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByText('City not found. Try again.')).toBeVisible();
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('San Diego');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByRole('cell', { name: 'Name' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Brewery Type' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Address' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'City', exact: true })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'State Province' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Postal Code' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Country' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Phone' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Website URL' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'State', exact: true })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Street' })).toBeVisible();
});

test('erstRichtigeDannFalschEingegebeneStadtsuche', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Graz');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByRole('cell', { name: 'Name' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Brewery Type' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Address' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'City', exact: true })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'State Province' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Postal Code' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Country' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Phone' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Website URL' })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'State', exact: true })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'Street' })).toBeVisible();
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Grazz');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByText('City not found. Try again.')).toBeVisible();
});

test('verlinkungenInDerTabelleVerbindenAufDieJeweiligeSeite', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Austin');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByRole('cell', { name: 'http://www.512brewing.com' })).toBeVisible();
    await page.getByRole('link', { name: 'http://www.512brewing.com' }).click();
    await expect(page.locator('.wvc-clearfix').first()).toBeVisible();
    await page.goto('http://localhost:4200/');
    await expect(page.getByText('Brewery Detail ListSearchNo')).toBeVisible();
    
});

test('kompletterAblaufMitDreiFalschUndRichtigEingegebenStadtnamen', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await expect(page.getByText('No data fetched, please')).toBeVisible();
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('San Diego');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByRole('cell', { name: 'Name' })).toBeVisible();
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Sann Diego');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByText('City not found. Try again.')).toBeVisible();
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Graz');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByRole('cell', { name: 'Name' })).toBeVisible();
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Grazz');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByText('City not found. Try again.')).toBeVisible();
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Austin');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByRole('cell', { name: 'Name' })).toBeVisible();
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Austinnn');
    await page.getByRole('button', { name: 'Search' }).click();
    await expect(page.getByText('City not found. Try again.')).toBeVisible();
});


