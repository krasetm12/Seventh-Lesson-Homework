package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class WelcomePageObject extends MainPageObject {

    public static final String
            LEARN_MORE_LINK = "id:Learn more about Wikipedia",
            SKIP_LINK = "id:Skip";

    public WelcomePageObject(AppiumDriver<?> driver) {
        super(driver);
    }

    public void waitForLearnMoreLink() {

        this.waitForElementPresent(
                LEARN_MORE_LINK,
                "Cannot find 'Learn more about Wikipedia' link"
        );
    }

    public void clickSkipButton() {

        this.waitForElementAndClick(
                SKIP_LINK,
                "Cannot find and click 'Skip' link",
                10
        );
    }
}