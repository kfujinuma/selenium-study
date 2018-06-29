package jp.gr.java_conf.kfujinuma.study.yretester.yretester;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YreTester {

	private final SearchSettings settings;
	private final Properties yprops = new Properties();
	private final String outDir;

	public YreTester(SearchSettings settings) {
		this.settings = settings;

		try {
			yprops.load(this.getClass().getResourceAsStream("/yretester.properties"));
			outDir = settings.getOutputDir() + File.separator + getDateTime();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void test() {
		WebDriver driver = getDriver(settings);
		List<SearchResult> results = new ArrayList<SearchResult>();
		for (SearchCondition condition : settings.getConditions()) {
			results.add(search(driver, condition));
		}
		driver.quit();

		System.out.println(results);

		writeResult(settings, results);
	}

	private void writeResult(SearchSettings settings, List<SearchResult> results) {
		// Apache Velocity を初期化する
		Velocity.init(this.getClass().getResource("/velocity.properties").getPath());

		// Velocityコンテキストを生成する
		VelocityContext context = new VelocityContext();

		// コンテキストに値を設定する
		context.put("settings", settings);
		context.put("results", results);

		// ライターを生成する
		Writer writer = null;
		try {
			writer = new FileWriter(new File(outDir + File.separator + "result.html"));
			// テンプレートを取得する
			Template tmpl = Velocity.getTemplate(
					"/tmpl.html", "UTF-8");

			// テンプレートとコンテキストをマージし、結果をライターに出力する
			tmpl.merge(context, writer);
			writer.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		}

		return;
	}

	private SearchResult search(WebDriver driver, SearchCondition condition) {
		SearchResult result = new SearchResult();
		result.setCondition(condition);

		try {

			//yahoo不動産ホームページを開く

			driver.navigate().to(yprops.getProperty("yre.query.uri"));

			//検索条件を入力して検索ボタンを押下

			WebElement keyword = findElement(driver, getLocation(Page.Query, "keyword"));
			WebElement category = findElement(driver, getLocation(Page.Query, "category"));
			WebElement area = findElement(driver, getLocation(Page.Query, "area"));
			WebElement searchbtn = findElement(driver, getLocation(Page.Query, "searchbtn"));

			setText(keyword, condition.getKeyword());
			setText(category, condition.getCategory());
			setText(area, condition.getArea());

			searchbtn.click();
			TimeUnit.SECONDS.sleep(2);

			//並び順を変える

			Page resultPage = Page.getPage(condition.getCategory());
			(new WebDriverWait(driver, 5))
					.until(ExpectedConditions.visibilityOfElementLocated(by(getLocation(resultPage, "sort"))));
			WebElement sort = findElement(driver, getLocation(resultPage, "sort"));
			setText(sort, condition.getOrder());

			TimeUnit.SECONDS.sleep(1);

			//検索結果(結果ページと件数)の取得

			WebElement count = findElement(driver, getLocation(resultPage, "count"));
			result.setCount(count.getText());
			TimeUnit.SECONDS.sleep(1);
			String resultCapPath = takeScreenShot(driver, getCapturePath(condition.getId()));
			result.setUri(driver.getCurrentUrl());
			result.setCapturePath(resultCapPath);

			//検索結果（各物件情報）の取得

			String resultWindow = driver.getWindowHandle();
			List<WebElement> candidates = findElements(driver, getLocation(resultPage, "candidates"));
			int capCount = candidates.size();

			for (int i = 0; i < capCount; i++) {

				String title = null;
				String uri = null;
				String capturePath = null;

				candidates = findElements(driver, getLocation(resultPage, "candidates"));
				WebElement candidate = candidates.get(i);
				title = candidate.getText();
				uri = candidate.getAttribute("href");

				if (StringUtils.equals(candidate.getTagName(), "a") &&
						i < condition.getCaptureCount()) {

					String openingWindow = candidate.getAttribute("target");
					candidate.click();
					if (StringUtils.equals(openingWindow, "_blank")) {
						for (String id : driver.getWindowHandles()) {
							if (!StringUtils.equals(id, resultWindow)) {
								//現在のウインドウIDと違っていたら格納
								//最後に格納されたIDが一番新しく開かれたウインドウと判定
								openingWindow = id;
							}
						}
					}
					driver.switchTo().window(openingWindow);
					TimeUnit.SECONDS.sleep(2);
					capturePath = takeScreenShot(driver, getCapturePath(condition.getId(), i));
					uri = driver.getCurrentUrl();
					driver.switchTo().window(resultWindow);
				}

				result.getCandidates().add(new Candidate(title, uri, capturePath));
			}
			result.setSuccess(true);

		} catch (Exception e) {
			result.setException(e);

		}

		return result;
	}

	private String getCapturePath(String conditionId) {
		String path = String.format("%s%s%s%sresult_%s.png", outDir, File.separator, conditionId,
				File.separator, getDateTime());
		return path;
	}

	private String takeScreenShot(WebDriver driver, String capturePath) throws IOException {
		File sfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(sfile, new File(capturePath));
		return capturePath;
	}

	private String getCapturePath(String conditionId, int candNum) {
		String path = String.format("%s%s%s%s%02d_%s.png", outDir, File.separator, conditionId,
				File.separator, candNum, getDateTime());
		return path;
	}

	private String getLocation(Page p, String elementName) {
		String key = String.format("yre.%s.el.%s", p.getPropertyKey(), elementName);
		return yprops.getProperty(key);
	}

	private WebElement findElement(WebDriver driver, String findby) {
		try {
			return driver.findElement(by(findby));
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<WebElement> findElements(WebDriver driver, String findby) {
		try {
			return driver.findElements(by(findby));
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private By by(String findby) {
		String[] spritted = findby.split("=", 2);
		if (spritted.length != 2) {
			throw new IllegalArgumentException("argument findby \"" + findby + "\" is illegal");
		}
		return by(spritted[0], spritted[1]);
	}

	private By by(String findtype, String findname) {
		if (StringUtils.equals(findtype, "css")) {
			return By.cssSelector(findname);

		} else if (StringUtils.equals(findtype, "id")) {
			return By.id(findname);

		} else if (StringUtils.equals(findtype, "class")) {
			return By.className(findname);

		} else if (StringUtils.equals(findtype, "xpath")) {
			return By.xpath(findname);
		} else {
			throw new IllegalArgumentException("argument findtype \"" + findtype + "\" is not supported");
		}
	}

	private void setText(WebElement element, String text) {
		String elementType = element.getTagName();
		if (StringUtils.equals(elementType, "select")) {
			Select select = new Select(element);
			select.selectByVisibleText(text);
		} else {
			element.clear();
			element.sendKeys(text);
		}
	}

	private WebDriver getDriver(SearchSettings settings) {

		String browserKind = settings.getBrowserKind();
		String driverPath = settings.getDriverPath();

		if (StringUtils.equals(browserKind, "firefox")) {
			System.setProperty("webdriver.gecko.driver", driverPath);
			return new FirefoxDriver();
		} else if (StringUtils.equals(browserKind, "chrome")) {
			System.setProperty("webdriver.chrome.driver", driverPath);
			return new ChromeDriver();
		} else if (StringUtils.equals(browserKind, "ie")) {
			System.setProperty("webdriver.ie.driver", driverPath);
			return new InternetExplorerDriver();
		} else {
			return new ChromeDriver();
		}
	}

	private String getDateTime() {
		LocalDateTime d = LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return df.format(d);
	}
}

enum Page {
	Query("検索ページ", "query"), NewApart("新築マンション", "result.newapart"), NewHouse("新築一戸建て", "result.newhouse"), OldApart(
			"中古マンション", "result.oldapart"), OldHouse("中古一戸建て",
					"result.oldhouse"), Land("土地", "result.land"), Rental("賃貸住宅", "result.rental");

	private String displayName;
	private String propertyKey;

	Page(String displayName, String propertyKey) {
		this.displayName = displayName;
		this.propertyKey = propertyKey;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public static Page getPage(String displayName) {
		for (Page p : Page.values()) {
			if (StringUtils.equals(displayName, p.displayName)) {
				return p;
			}
		}
		return null;
	}
}
