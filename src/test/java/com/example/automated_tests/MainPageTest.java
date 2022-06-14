package com.example.automated_tests;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPageTest {
    WebDriver driver;    //Creating WebDriver

    @BeforeEach
    public void setUp() {
        //you must specify the driver and the path to the driver
        System.setProperty("webdriver.gecko.driver", "./src/test/resources/geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }

    //vgroll Tests
    @Test
    public void verifyVgrollDevLogin() {

        /** This test verifies that the login page is functioning **/

        try {
            driver.get("https://dev.vgroll.com/login"); //Open vgroll
            assertEquals(driver.getTitle(), "Please Sign In");

            Thread.sleep(1500);     //Small wait to allow user to see what's happening

            //Find username box, select it, enter username
            WebElement username = driver.findElement(By.id("username"));
            username.sendKeys("Vincent");

            Thread.sleep(1500);

            //Find password box, select it, enter password
            WebElement password = driver.findElement(By.id("password"));
            password.sendKeys("123");

            Thread.sleep(1500);

            //click sign in
            WebElement signInButton = driver.findElement(By.xpath("/html/body/div/form/button"));
            signInButton.click();

            Thread.sleep(1500);

            //For some reason I get an extra page that pops up I have to click out of on Selenium, this handles that
            if(!driver.getTitle().equals("StaffWizard Payroll Application")) {
                WebElement returnToMain = driver.findElement(By.xpath("/html/body/div/section/div/div/p/a"));
                Thread.sleep(1500);
                returnToMain.click();
            }

            //Verify home page title
            //Assert.assertEquals(driver.getTitle(), "StaffWizard Payroll Application");
            assertEquals(driver.getTitle(), "StaffWizard Payroll Application");

            //Close the browser
            driver.quit();
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Log In Failed");
        }
    }


    @Test
    public void testVgrollBasicPayrollFunctions() throws InterruptedException {
        /**         This tests the payroll functions         **/

        try {
            vrollLogin();    //I run this method before the tests to ensure I am logged in
            driver.manage().window().maximize();    //This maximizes the browser window
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Log In Failed");
        }

        Thread.sleep(2000);
        WebElement currentElement;

        //Testing start new payroll
        try {
            //Go to Payroll menu
            currentElement = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/aside/div[3]/nav/ul/li[1]/a"));    //Xpath to the "Payroll" button
            currentElement.click();

            Thread.sleep(1000);


            //Start New Payroll
            currentElement = driver.findElement(By.xpath("/html/body/div[2]/div[3]/section/div[2]/div[2]/div/div/div[4]/div[2]/div[1]/div[2]/div"));    //Xpath to "Start New Payroll"
            currentElement.click();

            Thread.sleep(1000);

            currentElement = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div/div[1]/div/div/input"));     //Xpath to "Period Begin Date"
            currentElement.sendKeys("04/01/2022");

            Thread.sleep(500);

            currentElement = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div/div[2]/div/div/input"));     //Xpath to "Period End Date"
            currentElement.sendKeys("04/07/2022");

            Thread.sleep(500);

            currentElement = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div/div[3]/div/div/input"));     //Xpath to "Check Date"
            currentElement.sendKeys("04/08/2022");

            Thread.sleep(500);

            currentElement = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div/div[4]/div/div/input"));     //Xpath to "Description"
            currentElement.sendKeys("Regression Test");

            Thread.sleep(1000);

            currentElement = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[2]/div/div[1]/div"));       //Xpath to "Save" button
            currentElement.click();

            Thread.sleep(7000);     //Give plenty of time for vgroll to create the payroll

            driver.findElement(By.xpath("/html/body/div[2]/div[3]/section/div[2]/div[2]/div/div/div[4]/div[4]/div/table/tbody/tr[1]/td[10]/div[2]/div[1]/i")).click();  //Click payrolls 'Cog Wheel'

            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div[3]/section/div[2]/div[2]/div/div/div[4]/div[4]/div/table/tbody/tr[1]/td[10]/div[2]/div[2]/div[1]")).click(); //Calculate all checks

            Thread.sleep(55000);

            driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div/button")).click();    //exits progress pop up

            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            //driver.quit();
            fail("start new payroll failed");
        }

        //Testing delete payroll by deleting the Payroll that was created
        try {

            if (driver.getPageSource().contains("Regression Test")) {
                //Delete Payroll
                driver.findElement(By.xpath("/html/body/div[2]/div[3]/section/div[2]/div[2]/div/div/div[4]/div[4]/div/table/tbody/tr[1]/td[10]/div[5]/i")).click();
                Thread.sleep(1000);

                //Alert
                Alert alert = driver.switchTo().alert();
                alert.accept();
            }

            Thread.sleep(30000);    //Takes a long time for the payroll to show up as deleted
            driver.navigate().refresh();    //refresh page source
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/aside/div[3]/nav/ul/li[1]/a")).click();    //Xpath to the "Payroll" button
            Thread.sleep(1000);

            if (driver.getPageSource().contains("Regression Test")) {   //If regression test payroll is still on the page, fail the test
                driver.quit();
                fail("Deletion of payroll failed");
            }

        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Deletion of payroll failed");
        }

        //TODO: Add a 'process payroll' test case
        driver.quit();
    }

    @Test
    public void testVgrollBasicEmployeeFunctions() {
        /**         This tests the basic Employee functions         **/

        try {

            vrollLogin();    //I run this method before the tests to ensure I am logged in
            driver.manage().window().maximize();    //This maximizes the browser window

        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Log In Failed");
        }
        //vrollLogin();    //I run this method before the tests to ensure I am logged in

        //driver.manage().window().maximize();    //This maximizes the browser window


        /**         Creating Employee           **/
        try {
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/aside/div[3]/nav/ul/li[3]/a")).click();    //Clicking on "Employees"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div[3]/section/div[2]/div[2]/div/div/div[4]/div[2]/div[1]/div[2]/div")).click();    //Clicking on "+ New"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[1]/div[1]/div/div/input")).sendKeys("TEID0001");    //Entering "Employee ID"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[1]/div[2]/div[2]/div[3]/div")).click();  //Unlock SSN
            Thread.sleep(1500);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[1]/div[2]/div[2]/div[1]/div/div/input")).sendKeys("629745855");    //Entering SSN
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[2]/div[1]/div/div/input")).sendKeys("Man");    //Entering "First Name"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[2]/div[3]/div/div/input")).sendKeys("Person"); //Entering "Last name"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[3]/div[1]/div/div/input")).sendKeys("06/27/1995"); //Entering "Birth Date"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[3]/div[2]/div/div/input")).sendKeys("01/01/2022"); //Entering "Hire Date"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[4]/div[1]/div/div/input")).sendKeys("12345 Address");  //Entering "Address"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[4]/div[3]/div/div/input")).sendKeys("City");   //Entering "City"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[5]/div[1]/div/div/div[2]/button")).click();    //Clicking "State"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[5]/div[1]/div/div/div[2]/div/div/div/div/div/div/table/tbody/tr[1]/td")).click();  //Click Alabama
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[5]/div[2]/div/div/div[2]/button/i")).click();    //Click "Home State"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[5]/div[2]/div/div/div[2]/div/div/div/div/div/div/table/tbody/tr[1]/td")).click();  //Click Alabama
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[5]/div[3]/div/div/input")).sendKeys("00000");      //Entering "Zip Code"
            Thread.sleep(1000);


            driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div/div/div/form/div/div[3]/div[14]/div[1]/div[6]/div[1]/div/div/input")).sendKeys("2140000000"); //Entering "Phone Number"
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div/div/div/form/div/div[2]/div/div[1]/div")).click();        //Clicking "Save"
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Creation of employee failed");
        }


        /**     Delete the Employee    **/
        try {
            driver.findElement(By.name("__search__")).sendKeys("Person");    //Searching for created employee
            Thread.sleep(1000);


            driver.findElement(By.xpath("/html/body/div[2]/div[3]/section/div[2]/div[2]/div/div/div[4]/div[4]/div/table/tbody/tr/td[8]/div[4]/i")).click(); //Clicking delete button
            Thread.sleep(1000);

            //Accept Alert
            Alert alert = driver.switchTo().alert();
            alert.accept();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Deletion of employee failed");
        }

        driver.quit(); //exit
    }


    //Staff Wizard Tests
    @Test
    public void testLoginPage() {
        /**         The purpose of this test is to ensure the log in features are working           **/
        try {
            /**         Checking Log in page is working            **/
            driver.get("https://staging.staffwizardstaging.com/admin/dashboard");   //Go to Staging.StaffWizard to test login page

            System.out.println("Page Title: " + driver.getTitle());       //TODO: Apparently the title of the page is blank

            //Assert.assertEquals(driver.getTitle(), "Staffwizard");      //Ensuring that the page visited is the SW page and working
            Thread.sleep(1000);


            /**         Trying to log in with bad credentials**/
            driver.findElement(By.name("username")).sendKeys("123456789");      //Entering text in the username field
            Thread.sleep(1000);

            driver.findElement(By.name("password")).sendKeys("123456789");
            Thread.sleep(1000);

            driver.findElement(By.name("submit")).click();
            Thread.sleep(3000);

            if (!driver.getPageSource().contains("Invalid Username or Password!!!")) {
                System.out.println("The Invalid Log In Error did not appear.");
            } else System.out.println("The Invalid Log In Error showed up.");


            /**         Verify that the Error Message is removed when going to enter new credentials            **/
            driver.findElement(By.name("username")).sendKeys("123");
            Thread.sleep(1000);

            driver.findElement(By.name("username")).clear();
            Thread.sleep(1000);

            if (driver.getPageSource().contains("Invalid Username or Password!!!")) {   //TODO: Even though the error was removed it does not say it is. This may be because the page source needs refreshing after the message is gone
                System.out.println("The Invalid Log In Error was not removed.");
            } else System.out.println("The Invalid Log In Error was removed.");


            /**         Trying to log in with no credentials            **/
            driver.findElement(By.name("username")).sendKeys("");
            Thread.sleep(1000);

            driver.findElement(By.name("password")).sendKeys("");
            Thread.sleep(1000);

            driver.findElement(By.name("submit")).click();
            Thread.sleep(3000);

            if (!driver.getPageSource().contains("The Username field is required")) {
                System.out.println("The No Log In Credentials Error did not appear.");
            } else System.out.println("The No Log In Credentials Error showed up.");


            /**         Verify that the No Credential Error Message is removed when going to enter new credentials            **/
            driver.findElement(By.name("username")).sendKeys("123");
            Thread.sleep(1000);

            if (!driver.getPageSource().contains("Invalid Username or Password")) {
                System.out.println("The No Log In Credentials Error has been removed.");
            } else System.out.println("The No Log In Credentials Error stayed.");

            driver.findElement(By.name("username")).clear();
            Thread.sleep(1000);


            /**         Trying to log in again with good credentials            **/
            driver.findElement(By.name("username")).sendKeys("superadmin");
            Thread.sleep(1000);

            driver.findElement(By.name("password")).sendKeys("1234@direct");
            Thread.sleep(1000);

            driver.findElement(By.name("submit")).click();
            Thread.sleep(3000);

            //driver.close();     //closes out of the browser window
            driver.quit();      //closes every browser window and stops the process
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Log In Test Failed");
        }
    }

    @Test
    public void testForgetPassword() {
        /**         The purpose of this test is to ensure the Forget Password features are working           **/
        try {
            driver.get("https://staging.staffwizardstaging.com/admin/dashboard");   //Go to Staging.StaffWizard to test login page
            Thread.sleep(2000);

            driver.findElement(By.xpath("/html/body/div/div/div/div/form/div/div/div/p/a")).click();    //Click forgot password
            Thread.sleep(1000);

            if(driver.getCurrentUrl().equals("https://staging.staffwizardstaging.com/auth/forgot_password")) {  //Verify correct page pops up
                System.out.println("Forgot Password page loaded.");
            }
            else System.out.println("Forgot Password page not loaded.");



            driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("bjgjgbdjgbjmeejgbjg@gmail.com");   //Enter fake email address
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"ssn\"]")).sendKeys("878548787");      //Enter fake SSN
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click();    //click submit

            if(driver.getPageSource().contains("The Email And Social Security number that you provided are not registered with us.")) {
                System.out.println("'Email & SSN not recognized error' shows up.");
            } else System.out.println("'Email & SSN not recognized error' does NOT show up.");
            Thread.sleep(2000);

            //TODO: Need credentials for abc@gmail.com and SSN to finish this test.
            driver.quit();
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Forget Password Test Failed");
        }
    }

    @Test
    public void testSuperAdmin() {
        /**         The purpose of this test is to ensure the Super Admin account is still working           **/

        try {
            staffWizardLogIn(); //Logs in to Staging.StaffWizard
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Log In Failed");
        }

        try {
            //Verify Super Admin profile works
            driver.findElement(By.xpath("/html/body/div[1]/section/header/header/nav/div/div[3]/div/ul/li[7]/a")).click();  //Click admin profile name/picture
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[1]/section/header/header/nav/div/div[3]/div/ul/li[7]/ul/li[2]/div[2]/a")).click();  //Click profile
            Thread.sleep(1000);

            if (driver.getCurrentUrl().equals("https://staging.staffwizardstaging.com/admin/profile")) {     //Make sure page loaded
                System.out.println("Admin Profile loaded");
            } else System.out.println("Admin Profile not loaded");
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Super Admin Profile Test Failed");
        }


        try {
            //Verify updating profile works
            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click();    //click update profile without changing anything
            Thread.sleep(1000);

            if (driver.getPageSource().contains("Profile has been Updated Successfully!")) { //printing out if there was an error message
                System.out.println("Profile updated with no actual changes made. Website listed 'Successful update message'.");
            } else System.out.println("No 'Successful update' message was displayed");

            driver.navigate().refresh();    //refreshing page to clear page source

            driver.findElement(By.id("firstname")).sendKeys("$%^&"); //entering unacceptable characters in the first name field
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click(); //Clicking update
            Thread.sleep(1000);

            if (driver.getPageSource().contains("Profile has been Updated Successfully!")) { //printing out if there was an error message
                System.out.println("StaffWizard did allow the field with incorrect characters to be updated.");
            } else System.out.println("StaffWizard did not allow the field with incorrect characters to be updated.");

            driver.findElement(By.id("firstname")).clear();
            driver.findElement(By.id("firstname")).sendKeys("Con"); //entering acceptable characters in the first name field
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click(); //Clicking update
            Thread.sleep(1000);

            driver.findElement(By.id("firstname")).sendKeys("or"); //entering acceptable characters in the first name field
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click(); //Clicking update
            Thread.sleep(3000);

            if (driver.getPageSource().contains("Profile has been Updated Successfully!")) { //printing out if there was an error message
                System.out.println("StaffWizard did allow the field with correct characters to be updated.");
            } else System.out.println("StaffWizard did not allow the field with correct characters to be updated.");
            driver.quit();
        } catch(Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Update Profile Test Failed");
        }
    }

    @Test
    public void changePassword() {
        /**         This test verifies that the website's change password feature works         **/
        WebElement element;

        try {
            staffWizardLogIn();     //Logs in to StaffWizard
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Log In Failed");
        }

        try {
            driver.findElement(By.xpath("/html/body/div[1]/section/header/header/nav/div/div[3]/div/ul/li[7]/a/span")).click(); //Clicking top right user
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[1]/section/header/header/nav/div/div[3]/div/ul/li[7]/ul/li[2]/div[2]/a")).click();  //clicking profile
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[1]/section/section/div/section/div[1]/div/div/div[2]/a")).click();      //Click change password
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Navigating to profile failed");
        }


        try {
            //Change password without entering any data
            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click();    //Clicking change password
            Thread.sleep(1000);

            try {   //Ensuring that the password change is not accepted when entering no new password
                element = driver.findElement(By.xpath("//*[@id=\"password-error\"]"));
                if (element.isDisplayed()) {
                    System.out.println("Change password is not accepted when not entering any data");
                }
            } catch (Exception e) {
                System.out.println("Change password accepted when not entering any data: FAILED TEST");
            }

            driver.navigate().refresh();
            Thread.sleep(1000);


            //Change password but skip confirm password
            driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("1234@direct");
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click();    //Clicking change password
            Thread.sleep(1000);

            try {   //Ensuring that the password change is not accepted when entering a new password but not confirming it
                element = driver.findElement(By.xpath("//*[@id=\"confirm_pwd-error\"]"));
                if (element.isDisplayed()) {
                    System.out.println("Change password is not accepted when not confirming new password");
                }
            } catch (Exception e) {
                System.out.println("Change password accepted when not confirming new password: FAILED TEST");
            }

            driver.navigate().refresh();
            Thread.sleep(1000);


            //Change password but have different confirm password
            driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("1234@direct");
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"confirm_pwd\"]")).sendKeys("asdfghjkl");

            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click();    //Clicking change password
            Thread.sleep(1000);

            try {   //Ensuring that the password change is not accepted when entering a new password but a different confirm password
                element = driver.findElement(By.xpath("//*[@id=\"confirm_pwd-error\"]"));
                if (element.isDisplayed()) {
                    System.out.println("Change password is not accepted when entering different confirm password");
                }
            } catch (Exception e) {
                System.out.println("Change password is accepted when entering different confirm password: FAILED TEST");
            }

            driver.navigate().refresh();
            Thread.sleep(1000);


            //Change password but have a new password under 8 characters
            driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("1234@");
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"confirm_pwd\"]")).sendKeys("1234@");

            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click();    //Clicking change password
            Thread.sleep(1000);

            try {   //Ensuring that the password change is not accepted when entering a new password under 8 character
                element = driver.findElement(By.xpath("//*[@id=\"password-error\"]"));
                if (element.isDisplayed()) {
                    System.out.println("Change password is not accepted when entering a new one under 8 characters");
                }
            } catch (Exception e) {
                System.out.println("Change password is accepted when entering a new one under 8 characters");
            }

            driver.navigate().refresh();
            Thread.sleep(1000);


            //Change Password for real now
            driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("1234@direct");
            Thread.sleep(1000);

            driver.findElement(By.xpath("//*[@id=\"confirm_pwd\"]")).sendKeys("1234@direct");

            driver.findElement(By.xpath("//*[@id=\"submit\"]")).click();    //Clicking change password
            Thread.sleep(1000);

            if (driver.getPageSource().contains("Password has been changed successfully!")) {
                System.out.println("Change password working");
            } else System.out.println("Change password not working");

            driver.quit();
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Change Password Test Failed");
        }
    }

    @Test
    public void signOut() {
        /**         This test verifies the sign-out feature works           **/
        try {
            staffWizardLogIn();
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Sign In Failed");
        }

        try {
            driver.findElement(By.xpath("/html/body/div[1]/section/header/header/nav/div/div[3]/div/ul/li[7]/a/span")).click(); //Click profile
            Thread.sleep(1000);

            driver.findElement(By.xpath("/html/body/div[1]/section/header/header/nav/div/div[3]/div/ul/li[7]/ul/li[2]/div[1]/a")).click();  //Click sign out
            Thread.sleep(1000);

            if (driver.getCurrentUrl().equals("https://staging.staffwizardstaging.com/auth/login")) {
                System.out.println("Test Passed");
            } else System.out.println("Test Failed");
            driver.quit();
        } catch (Exception e) {
            System.out.println("\nStack Trace:");
            e.printStackTrace();
            driver.quit();
            fail("Sign out Test Failed");
        }
    }



    //Example Test
    @Test
    public void fakeTest() throws InterruptedException {
        driver.get("https://www.facebook.com/");      //Got to facebook.com

        Thread.sleep(1000);     //Small wait to allow user to see what's happening

        driver.manage().window().maximize();

        WebElement currentElement = driver.findElement(By.xpath("//*[@id=\"email\"]"));     //Assigning "mail or Phone number" to currentElement
        currentElement.sendKeys("FakeEmail12345@ymail.com");                          //Sending a fake email address to the element

        Thread.sleep(1000);     //Small wait to allow user to see what's happening

        driver.findElement(By.xpath("//*[@id=\"pass\"]")).sendKeys("Password"); //Sending an incorrect facebook password to the "Password" Element

        Thread.sleep(1000);     //Small wait to allow user to see what's happening

        currentElement = driver.findElement(By.name("login"));      //Clicking on the "Log in" button
        currentElement.click();

        Thread.sleep(1000);     //Small wait to allow user to see what's happening

        assertEquals(driver.getTitle(), "Log into Facebook");    //Checks that the title of the page matches "Log into Facebook"

        driver.close();     //Closes the driver
    }




    //Methods
    public void vrollLogin() throws InterruptedException {
        /**                 Throw this before a test so you can log in                      **/
        driver.get("https://dev.vgroll.com/login"); //Open vgroll

        assertEquals(driver.getTitle(), "Please Sign In");//Verify title page

        Thread.sleep(1000);     //Small wait to allow user to see what's happening

        //Find username box, select it, enter username
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("Vincent");       //Put your username in the ""

        Thread.sleep(1000);

        //Find password box, select it, enter password
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("123");           //Put your password in the ""

        Thread.sleep(1000);

        //click sign in
        WebElement signInButton = driver.findElement(By.xpath("/html/body/div/form/button"));
        signInButton.click();

        Thread.sleep(1000);

        //For some reason I get an extra page that pops up I have to click out of on Selenium, this handles that
        if(!driver.getTitle().equals("StaffWizard Payroll Application")) {
            WebElement returnToMain = driver.findElement(By.xpath("/html/body/div/section/div/div/p/a"));
            Thread.sleep(1000);
            returnToMain.click();
        }

        //Verify home page title
        assertEquals(driver.getTitle(), "StaffWizard Payroll Application");
    }

    public void staffWizardLogIn() throws InterruptedException {
        /**         This is a method to log the user in to Staff Wizard           **/
        /**         Checking Log in page is working                               **/
        driver.get("https://staging.staffwizardstaging.com/admin/dashboard");   //Go to Staging.StaffWizard to test login page
        Thread.sleep(1000);

        driver.findElement(By.name("username")).sendKeys("superadmin"); //Logging in as super admin
        Thread.sleep(1000);

        driver.findElement(By.name("password")).sendKeys("1234@direct");
        Thread.sleep(1000);

        driver.findElement(By.name("submit")).click();
        Thread.sleep(3000);
    }

}