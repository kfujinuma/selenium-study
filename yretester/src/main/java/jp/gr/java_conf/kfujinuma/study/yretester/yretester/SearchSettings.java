package jp.gr.java_conf.kfujinuma.study.yretester.yretester;

import java.util.List;

import com.github.takezoe.xlsbeans.annotation.HorizontalRecords;
import com.github.takezoe.xlsbeans.annotation.LabelledCell;
import com.github.takezoe.xlsbeans.annotation.LabelledCellType;
import com.github.takezoe.xlsbeans.annotation.Sheet;

@Sheet(name = "検索設定")
public class SearchSettings {

	private String outputDir;
	private String browserKind;
	private String driverPath;

	public String getOutputDir() {
		return outputDir;
	}

	@LabelledCell(label = "結果出力フォルダ", type = LabelledCellType.Right)
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getBrowserKind() {
		return browserKind;
	}

	@LabelledCell(label = "ブラウザ種類", type = LabelledCellType.Right)
	public void setBrowserKind(String browserKind) {
		this.browserKind = browserKind;
	}

	public String getDriverPath() {
		return driverPath;
	}

	@LabelledCell(label = "WebDriverパス", type = LabelledCellType.Right)
	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	private List<SearchCondition> conditions;

	public List<SearchCondition> getConditions() {
		return conditions;
	}

	@HorizontalRecords(tableLabel = "検索条件")
	public void setConditions(List<SearchCondition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return "SearchSettings [outputDir=" + outputDir + ", browserKind=" + browserKind + ", driverPath=" + driverPath
				+ ", conditions=" + conditions + "]";
	}

}
