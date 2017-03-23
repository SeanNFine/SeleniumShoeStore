import org.junit.Assert;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShoeStoreAutomation {
    //Declared as public static to use same webdriver instance publicly
    public static WebDriver driver = new FirefoxDriver();

    @Test
    public static void main(String[] args){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://shoestore-manheim.rhcloud.com/");

        try {
//            monthly_shoe_lists_test();
        } catch (AssertionError e){
            System.out.println("Assert Error: "+e.getLocalizedMessage());
        } finally {
            //return to main page
            driver.navigate().to("http://shoestore-manheim.rhcloud.com/");
        }

        try{
            submit_email_test();
        } catch (AssertionError e){
            System.out.println("Assert Error: "+e.getLocalizedMessage());
        } finally {
            //return to main page
            driver.navigate().to("http://shoestore-manheim.rhcloud.com/");
        }

        driver.quit();
    }

    @Test
    public static void monthly_shoe_lists_test(){

        //Story 1: Monthly Display of new releases
        //Should display a 'small blurb' of each shoe
        //Should display an image each shoe being released
        //Each should should have a suggested price pricing
        List<WebElement> monthLinks = driver.findElements(By.xpath("//a[contains(@href,'/months/')]"));
        List<String> hrefs = new ArrayList<String>();
        String hrefString = null;

        for(int i = 0; i < monthLinks.size(); i++){
            hrefString = monthLinks.get(i).getAttribute("href");
            hrefs.add(hrefString);
        }

        String monthText = null;

        System.out.println("Month amount (should be 12): "+monthLinks.size());
        for(int i = 0; i < hrefs.size(); i++){
            hrefString = hrefs.get(i);
            driver.navigate().to(hrefString);

            List<WebElement> shoeResults = driver.findElements(By.className("shoe_result"));
            WebElement shoeResult = null;
            String shoeBlurb = null;
            WebElement shoeImage = null;
            String shoePrice = null;
            for(int j = 0; j < shoeResults.size(); j++){
                shoeResult = shoeResults.get(j);
                shoeBlurb = shoeResult.findElement(By.className("shoe_description")).getText();
                shoeImage = shoeResult.findElement(By.xpath("//td[@class='shoe_image']/img"));
                shoePrice = shoeResult.findElement(By.className("shoe_price")).getText();

                Assert.assertNotEquals("Shoe has a 'blurb'.", "", shoeBlurb);
                //Do we just have to confirm the section exists or do there need to be images? Assuming the latter
                Assert.assertNotEquals("Shoe has an image", "", shoeImage.getAttribute("src"));
                Assert.assertNotEquals("Shoe has a price.", "", shoePrice);
            }
        }
    }

    @Test
    public static void submit_email_test(){
        //1) There should be an area to submit email address
        //2) on successful submission of a valid email address user should receive a message:
        // "Thanks! We will notify you of our new shoes at this email: users email address"

        //Find the Email, confirm it's displayed and enabled, then input an email address
        WebElement emailInput = driver.findElement(By.id("remind_email_input"));
        Assert.assertTrue(emailInput.isDisplayed());
        Assert.assertTrue(emailInput.isEnabled());
        emailInput.sendKeys("test@test.com");

        WebElement submitEmail = driver.findElement(By.cssSelector("div.left:nth-child(1) > input:nth-child(3)"));
        submitEmail.click();

        WebElement notification = driver.findElement(By.cssSelector(".flash"));

        Assert.assertEquals("Notification message is 'Thanks! We will notify you of our new shoes at this email: users email address'",
                "Thanks! We will notify you of our new shoes at this email: test@test.com",notification.getText());
    }
}
