package com.qa.base;

import com.qa.utils.HttpHelper;
import com.qa.utils.JsonParser;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.reporters.HtmlHelper;

import java.net.URL;

public class DriverManager {
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static AppiumDriver getDriver(){
        return driver.get();
    }

    public static void setDriver(AppiumDriver driver1){
        driver.set(driver1);
    }

    public static void initializeDriver(String deviceID) throws Exception {
        AppiumDriver driver;
        //String userName = "gkhanit_2Z7js1";
        //String accessKey = "6jBRuJ8Zusz173z9yEny";
        String userName = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
    //    String browserstackLocal = System.getenv("BROWSERSTACK_LOCAL");
        String buildName = System.getenv("BROWSERSTACK_BUILD_NAME");
    //    String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");
        String app = System.getenv("BROWSERSTACK_APP_ID");

        JSONObject appUrlObj = new JSONObject(HttpHelper.uploadApp("/Users/gokhansit/Downloads/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk"));

        JSONObject deviceObj = new JSONObject(JsonParser.parse("Devices.json").getJSONObject(deviceID).toString());
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("device", deviceObj.getString("device"));
        caps.setCapability("os_version", deviceObj.getString("os_version"));
        caps.setCapability("project", "My First Project");
        caps.setCapability("build", buildName);
        caps.setCapability("name", "Bstack-[Java] Sample Test");
        //caps.setCapability("app", appUrlObj.getString("app_url"));
        //caps.setCapability("app", app);
        caps.setCapability("app", deviceObj.getString(app));

        URL url = new URL("https://"+userName+":"+accessKey+"@hub-cloud.browserstack.com/wd/hub");

        switch (deviceID){
            case "1":
                driver = new AndroidDriver(url, caps);
                break;
            case "2":
                driver = new IOSDriver(url, caps);
                break;
            default:
                throw new IllegalStateException("invalid device id" + deviceID);
        }
        setDriver(driver);
    }
}
