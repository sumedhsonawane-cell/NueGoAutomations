package nuego_automation_project;

import nuego_automation_project.base.BasePage;
import nuego_automation_project.utils.ScreenshotUtil;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    private final Duration TIMEOUT = Duration.ofSeconds(30);

    // ---------------- WebElements ---------------- //
@FindBy(xpath = "//img[@alt='Profile']")
private WebElement clk;

@FindBy(name = "random_mobile_number")
private WebElement txtMobileNumber;


@FindBy(css = ".teal-22BBB0-bg.submit-button.w-100.mb-3")
private WebElement btnSendOtp;


@FindBy (css=".cb-i")
private WebElement clickCaptch;

@FindBy(xpath = "//input[@aria-label='Please enter OTP character 1']")
private WebElement txtOtp;

// Possible CAPTCHA element (Cloudflare checkbox or challenge)
@FindBy(css = "iframe[title*='captcha'], div[aria-label*='Cloudflare']")
private WebElement captchaFrame;

@FindBy(xpath = "//button[@class='teal-22BBB0-bg white-color open-600w-16s-24h p-3 w-100 submit-button mb-3']")
private WebElement btnVerifyOtp;

// ---------------- Constructor ---------------- //
public LoginPage(WebDriver driver) {
    super(driver); // call BasePage constructor
    PageFactory.initElements(driver, this);
}

// ---------------- Actions ---------------- //
public void login(String mobile, String otp) {
    WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

    try {
        // Click profile icon
wait.until(ExpectedConditions.elementToBeClickable(clk)).click();

// Check for CAPTCHA
try {
    if (captchaFrame.isDisplayed()) {
        System.out.println("CAPTCHA detected, cannot bypass automatically.");
takeScreenshot("captcha_block"); // inherited from BasePage
return; // stop execution
    }
} catch (NoSuchElementException | TimeoutException ignored) {
    // CAPTCHA not present, continue
}

// Enter mobile number digit by digit
wait.until(ExpectedConditions.visibilityOf(txtMobileNumber));
txtMobileNumber.clear();
for (char digit : mobile.toCharArray()) {
    txtMobileNumber.sendKeys(Character.toString(digit));
    Thread.sleep(200);
}

// Wait before clicking "Send OTP"
Thread.sleep(1000);


// Click "Send OTP"
wait.until(ExpectedConditions.elementToBeClickable(btnSendOtp)).click();

Thread.sleep(1500);

wait.until(ExpectedConditions.elementToBeClickable(clickCaptch)).click();

// Enter OTP
wait.until(ExpectedConditions.visibilityOf(txtOtp)).sendKeys(otp);
Thread.sleep(1500);

// Click Verify OTP
    wait.until(ExpectedConditions.elementToBeClickable(btnVerifyOtp)).click();

} catch (Exception e) {
    takeScreenshot("LoginPage_Failure"); // inherited from BasePage
            throw new RuntimeException(e);
        }
    }
}



