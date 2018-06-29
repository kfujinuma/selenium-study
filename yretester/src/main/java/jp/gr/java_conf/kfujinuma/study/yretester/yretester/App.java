package jp.gr.java_conf.kfujinuma.study.yretester.yretester;

import java.io.FileInputStream;

import com.github.takezoe.xlsbeans.XLSBeans;
import com.github.takezoe.xlsbeans.xssfconverter.WorkbookFinder;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		try {
			SearchSettings settings = (SearchSettings) new XLSBeans().load(
					new FileInputStream(args[0]),
					SearchSettings.class, WorkbookFinder.TYPE_XSSF);
			System.out.println(settings);

			YreTester tester = new YreTester(settings);
				tester.test();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
