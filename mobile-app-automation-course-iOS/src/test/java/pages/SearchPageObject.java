package pages;
import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.WebElement;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertTrue;
public abstract class SearchPageObject extends MainPageObject {
    protected static String
            SEARCH_INIT_ELEMENT,
            SEARCH_INPUT,
            SEARCH_CANCEL_BUTTON,
            SEARCH_RESULT_BY_SUBSTRING_TPL,
            SEARCH_RESULT_ELEMENT,
            SEARCH_RESULT_ELEMENT_BY_ORDER_TPL,
            SEARCH_EMPTY_RESULT_ELEMENT,
            SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL;
    public SearchPageObject(AppiumDriver<?> driver) {
        super(driver);
    }
    public void initSearchInput() {
        this.waitForElementPresent(
                SEARCH_INIT_ELEMENT,
                "Cannot find field search init"
        );
        this.waitForElementAndClick(
                SEARCH_INIT_ELEMENT,
                "Cannot find and click search init element",
                10
        );
    }
    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(
                SEARCH_CANCEL_BUTTON,
                "Cannot find search cancel button"
        );
    }
    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(
                SEARCH_CANCEL_BUTTON,
                "Search cancel button is steel present",
                10
        );
    }
    public void clickCancelSearch() {
        this.waitForElementAndClick(
                SEARCH_CANCEL_BUTTON,
                "Cannot find and click search cancel button",
                10
        );
    }

    public void typeSearchLine(String search_line) {

        this.waitForElementAndClear(
                SEARCH_INPUT,
                "Cannot find and type into search init",
                10
        );

        this.waitForElementAndSendKeys(
                SEARCH_INPUT,
                search_line,
                "Cannot find and type into search init",
                10
        );
    }
    public void verifyTextInSearchLine(String search_line) {
        this.waitForElementPresent(
                SEARCH_INPUT,
                "Cannot find entry field",
                10
        );
        if (Platform.getInstance().isAndroid()) {
            this.assertElementHasText(
                    SEARCH_INPUT,
                    search_line,
                    "Cannot find expected text in entry field"
            );
        } else {
            assertElementPresent(
                    SEARCH_INPUT,
                    "Cannot find expected element"
            );
        }
    }
    public void waitForSearchResult(String substring) {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementPresent(
                search_result_xpath,
                "Cannot find search result with substring " + substring
        );
    }
    public void clickByArticleWithSubstring(String substring) {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementAndClick(
                search_result_xpath,
                "Cannot find and click search result with substring " + substring,
                10
        );
    }
    public void waitForElementByTitleAndDescription(String title, String description) {
        String search_result_xpath = getResultSearchElementByTitleAndDescription(title, description);
        this.waitForElementPresent(
                search_result_xpath,
                String.format("Cannot find search result with title '%s' and description '%s'", title, description),
                10
        );
    }
    public int getAmountOfFoundArticles() {
        this.waitForElementPresent(
                SEARCH_RESULT_ELEMENT,
                "Cannot find anything by the request",
                15
        );
        return this.getAmountOfElements(SEARCH_RESULT_ELEMENT);
    }
    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(
                SEARCH_EMPTY_RESULT_ELEMENT,
                "Cannot find empty result element"
        );
    }
    public void assertThereIsNoResultOfSearch() {
        this.assertElementNotPresent(
                SEARCH_RESULT_ELEMENT,
                "We supposed not to find any results"
        );
    }
    public void checkCorrectnessOfSearchResults(String search_text) {
        this.waitForElementPresent(
                SEARCH_RESULT_ELEMENT,
                "Cannot find search results",
                10
        );
        int articles_number = driver.findElements(getLocatorByString(SEARCH_RESULT_ELEMENT)).size();
        int number = 0;
        while (articles_number > number) {
            number++;
            String search_element_xpath = getSearchElementByOrder("" + number);
            WebElement element = this.waitForElementPresent(
                    search_element_xpath,
                    "Search element not found"
            );
            assertTrue(
                    element.getAttribute("name").toLowerCase(Locale.ROOT)
                            .contains(search_text.toLowerCase(Locale.ROOT)),
                    "Search result does not contain the search word"
            );
        }
    }
    public void assertSearchResultsPresent() {
        this.assertElementPresent(
                SEARCH_RESULT_ELEMENT,
                "Found one or zero articles"
        );
    }
    public void assertSearchResultsNotPresent() {
        this.assertElementNotPresent(
                SEARCH_RESULT_ELEMENT,
                "Articles are still on the page"
        );
//        assertEquals(
//                0,
//                driver.findElements(By.id("org.wikipedia:id/page_list_item_container")).size(),
//                "Articles are still on the page "
//        );
    }
    /**
     * TEMPLATES METHODS
     *
     * @param substring
     * @return
     */
    private static String getResultSearchElement(String substring) {
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }
    private static String getSearchElementByOrder(String substring) {
        return SEARCH_RESULT_ELEMENT_BY_ORDER_TPL.replace("{NUMBER}", substring);
    }
    private static String getResultSearchElementByTitleAndDescription(String title, String description) {
        return SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL.replace("{TITLE}", title).replace("{DESCRIPTION}", description);
    }
}