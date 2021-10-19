package pages;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;
public class MainPageObject {
    protected AppiumDriver<?> driver;
    public MainPageObject(AppiumDriver<?> driver) {
        this.driver = driver;
    }

  
    public WebElement waitForElementPresent(String locator, String error_message, long timeout_in_seconds) {

        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeout_in_seconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    
    public WebElement waitForElementPresent(String locator, String error_message) {

        
        return waitForElementPresent(locator, error_message, 15);
    }

    
    public WebElement waitForElementAndClick(String locator, String error_message, long timeout_in_seconds) {

       
        WebElement element = waitForElementPresent(locator, error_message, timeout_in_seconds);
        element.click();
        return element;
    }

    
    public WebElement waitForElementAndSendKeys(String locator, String value, String error_message, long timeout_in_seconds) {

        
        WebElement element = waitForElementPresent(locator, error_message, timeout_in_seconds);
        element.sendKeys(value);
        return element;
    }

    
    public boolean waitForElementNotPresent(String locator, String error_message, long timeout_in_seconds) {

        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeout_in_seconds);
        wait.withMessage(error_message);
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    
    public WebElement waitForElementAndClear(String locator, String error_message, long timeout_in_seconds) {

        
        WebElement element = waitForElementPresent(locator, error_message, timeout_in_seconds);
        element.clear();
        return element;
    }

    
    public void assertElementHasText(String locator, String expected_text, String error_message) {

        
        WebElement element = waitForElementPresent(locator, error_message);
        Assertions.assertTrue(
                element.getAttribute("text").contains(expected_text),
                error_message
        );
    }
    public void swipeUp(Duration time_of_swipe){
        TouchAction<?> action = new TouchAction<>(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int)(size.height * 0.8);
        int end_y = (int)(size.height * 0.2);
        action
                .press(point(x, start_y))
                .waitAction(waitOptions(time_of_swipe))
                .moveTo(point(x, end_y))
                .release()
                .perform();
    }
    public void swipeUpQuick() {
        swipeUp(ofMillis(200));
    }

    
    public void swipeUpToFindElement(String locator, String error_message, int max_swipes) {

        By by = getLocatorByString(locator);
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0) {

            if (already_swiped > max_swipes) {
                
                waitForElementPresent(locator, "cannot find element by swipe UP. \n" + error_message, 0);
                return;
            }
            swipeUpQuick();
            already_swiped++;
        }
    }

    
    public void swipeElementToLeft(String locator, String error_message) {

      
        WebElement element = waitForElementPresent(locator, error_message);

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        
        TouchAction<?> action = new TouchAction<>(driver);
        action
                .press(point(right_x, middle_y))
                .waitAction(waitOptions(ofMillis(300)))
                .moveTo(point(left_x, middle_y))
                .release()
                .perform();
    }

    
    public int getAmountOfElements(String locator) {

        By by = getLocatorByString(locator);
        List<?> elements = driver.findElements(by);
        return elements.size();
    }

    
    public void assertElementNotPresent(String locator, String error_message) {

        
        int amount_of_elements = getAmountOfElements(locator);

        if (amount_of_elements > 0) {
            
            String default_message = "An element '" + locator + "' supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    
    public String waitForElementAndGetAttribute(String locator, String attribute, String error_message, long timeout_in_seconds) {

       
        WebElement element = waitForElementPresent(locator, error_message, timeout_in_seconds);
        return element.getAttribute(attribute);
    }

    
    public void assertElementPresent(String locator, String error_message) {

        By by = getLocatorByString(locator);
        if (driver.findElements(by).size() < 1) {
            
            String default_message = "An element '" + locator + "' supposed to be present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    private By getLocatorByString(String locator_with_type) {

        String[] exploded_locator = locator_with_type.split(Pattern.quote(":"),2);
        String by_type = exploded_locator[0];
        String locator = exploded_locator[1];

        if (by_type.equals("xpath")) {
            return By.xpath(locator);
        } else if (by_type.equals("id")) {
            return By.id(locator);
        } else {
            throw new IllegalArgumentException("Cannot get typ of locator. Locator: " + locator_with_type);
        }
    }
}