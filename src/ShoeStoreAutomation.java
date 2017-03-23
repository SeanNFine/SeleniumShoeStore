import org.junit.Assert;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.ArrayList;
import java.util.List;

public class ShoeStoreAutomation {
    //Declared as public static to use same webdriver instance publicly
    public static WebDriver driver = new FirefoxDriver();

    @Test
    public static void main(String[] args){

        driver.get("http://shoestore-manheim.rhcloud.com/");

        monthly_shoe_lists_test();
        submit_email_test();

        //driver.quit();
    }

    @Test
    public static void monthly_shoe_lists_test(){

        //Story 1: Monthly Display of new releases
        //Should display a 'small blurb' of each shoe
        //Should display an image each shoe being release
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
                try {
                    Assert.assertNotEquals("Shoe has a 'blurb'.", "", shoeBlurb);
                    Assert.assertNotEquals("Shoe has an image", "", shoeImage.getAttribute("src"));
                    Assert.assertNotEquals("Shoe has a price.", "", shoePrice);
                } catch (Exception e){
                    System.out.println("Assert Error: "+e.getLocalizedMessage());
                }
            }
        }
        //Return to main page
        driver.navigate().to("http://shoestore-manheim.rhcloud.com/");
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

        WebElement notification = driver.findElement(By.xpath("//div[id='flash']/div"));

        Assert.assertEquals("Notification message is 'Thanks! We will notify you of our new shoes at this email: users email address'",
                "Thanks! We will notify you of our new shoes at this email: users email address: test@test.com",notification.getText());


        //Return to main page
        driver.navigate().to("http://shoestore-manheim.rhcloud.com/");
    }
}
