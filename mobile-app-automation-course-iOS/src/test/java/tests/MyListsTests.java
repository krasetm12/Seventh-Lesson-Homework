package tests;
import factories.ArticlePageObjectFactory;
import factories.MyListsPageObjectFactory;
import factories.NavigationUIFactory;
import factories.SearchPageObjectFactory;
import lib.Platform;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import pages.ArticlePageObject;
import pages.MyListsPageObject;
import pages.NavigationUI;
import pages.SearchPageObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class MyListsTests extends CoreTestCase {
    public static final String folder_name = "Learning programming";
    @Test
    public void testSaveFirstArticleToMyList() throws InterruptedException {
        String article_title = "Appium";
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine(article_title);
        searchPageObject.clickByArticleWithSubstring(article_title);
        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        articlePageObject.waitForTitleElement();
        if (Platform.getInstance().isAndroid()) {
            articlePageObject.addArticleToMyListForTheFirstTime(folder_name);
        } else {
            articlePageObject.addArticlesToMySavedForTheFirstTime();
        }
        articlePageObject.closeArticle();
        NavigationUI navigationUI = NavigationUIFactory.get(driver);
        Thread.sleep(2000);
        navigationUI.clickMyLists();
        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid()) {
            myListsPageObject.openFolderByName(folder_name);
        }
        myListsPageObject.swipeByArticleToDelete(article_title);
    }

    @Test
    public void testSaveTwoArticlesToMyList() {

        String folder_name = "Learning programming";
        String first_title = "Java (programming language)";
        String second_title = "Python (programming language)";

        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring(first_title);

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid()) {
            articlePageObject.addArticleToMyListForTheFirstTime(folder_name);
        } else {
            articlePageObject.addArticlesToMySavedForTheFirstTime();
        }
        articlePageObject.closeArticle();

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Python");
        searchPageObject.clickByArticleWithSubstring(second_title);

        if (Platform.getInstance().isAndroid()) {
            articlePageObject.addArticleIntoExistingMyList(folder_name);
        } else {
            articlePageObject.addArticlesToMySaved();
        }
        articlePageObject.closeArticle();

        NavigationUI navigationUI = NavigationUIFactory.get(driver);
        navigationUI.clickMyLists();

        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid()) {
            myListsPageObject.openFolderByName(folder_name);
        }
        myListsPageObject.swipeByArticleToDelete(first_title);
        myListsPageObject.waitForArticleToAppearByTitle(second_title);
    }
}