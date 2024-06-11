import { test, expect } from '@playwright/test';


test('sucheMitExistierenderStadt', async ({ page }) => {
    await page.goto('http://localhost:4200/');
    await page.getByPlaceholder('Enter City Name').click();
    await page.getByPlaceholder('Enter City Name').fill('Graz');
    await page.getByRole('button', { name: 'Search' }).click();
    await page.getByRole('cell', { name: 'Name' }).click();
});

