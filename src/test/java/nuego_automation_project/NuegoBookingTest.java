package nuego_automation_project;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class NuegoBookingTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private BusBookingPage bookingPage;
    private Filters filtersPage;
    private SelectSeatPoints seatPointsPage;

    @Parameters("browser")
@BeforeMethod
public void setUp(@Optional("chrome") String browser) {
if (browser.equalsIgnoreCase("chrome")) {
WebDriverManager.chromedriver().setup();
ChromeOptions options = new ChromeOptions();
options.addArguments("--disable-notifications");
options.addArguments("--disable-blink-features=AutomationControlled");
options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
options.setExperimentalOption("useAutomationExtension", false);
    driver = new ChromeDriver(options);

} else if (browser.equalsIgnoreCase("firefox")) {
WebDriverManager.firefoxdriver().setup();
FirefoxOptions options = new FirefoxOptions();
options.addArguments("--disable-notifications");
    driver = new FirefoxDriver(options);
} else {
    throw new IllegalArgumentException("Unsupported browser: " + browser);
}

driver.manage().window().maximize();
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

// Initialize page objects
    loginPage = new LoginPage(driver);
    homePage = new HomePage(driver);
    bookingPage = new BusBookingPage(driver);
    filtersPage = new Filters(driver);
    seatPointsPage = new SelectSeatPoints(driver);
}

@Test(description = "Verify user can search, apply filters, and book a seat")
public void testBookBusFromAgraToBassi() {
    driver.get("https://greencell-nuego-web.web.app/");
System.out.println("Navigated to application URL");

// Step 0: Login
loginPage.login("7428730993", "1234");
System.out.println("Login successful (mobile + OTP entered)");

// Step 1: Handle popup & search bus
homePage.closePopupIfPresent();
homePage.searchBus("Agra", "Bassi");
System.out.println("Searched for buses from Agra to Bassi");

// Step 2: Apply Filters
String timer = filtersPage.selectTimer();
Assert.assertNotNull(timer, "Timer filter not applied!");
System.out.println("Timer Applied: " + timer);

filtersPage.clickBoardingPoint();
String boardingSelected = filtersPage.selectBoardingCheckbox();
Assert.assertNotNull(boardingSelected, "Boarding point not selected!");
System.out.println("Boarding Point selected: " + boardingSelected);

filtersPage.clickDroppingPoint();
String droppingSelected = filtersPage.selectDroppingCheckboxAndReset();
Assert.assertNotNull(droppingSelected, "Dropping point not selected!");
System.out.println("Dropping Point selected: " + droppingSelected);

// Step 3: Select Seat & Book
bookingPage.scrollDownSmall();
bookingPage.clickSeat();
seatPointsPage.selectSeats("1A", "2A", "3A", "4A");
seatPointsPage.selectPickupPointByName("Eidgah Bus Stan...");
seatPointsPage.selectDropPointByName("Bassi Chowk");
seatPointsPage.clickBookAndPay();

System.out.println("Booking flow completed successfully");
}

@AfterMethod(alwaysRun = true)
public void tearDown() {
    if (driver != null) {
        driver.quit();
        System.out.println("Browser closed");
        }
    }
}
