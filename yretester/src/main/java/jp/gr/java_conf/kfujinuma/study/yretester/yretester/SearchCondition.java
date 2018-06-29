package jp.gr.java_conf.kfujinuma.study.yretester.yretester;

import com.github.takezoe.xlsbeans.annotation.Column;

public class SearchCondition {

	private String id;
	private String keyword;
	private String category;
	private String area;
	private String order;
	private int captureCount;

	@Override
	public String toString() {
		return "SearchCondition [id=" + id + ", keyword=" + keyword + ", category=" + category + ", area=" + area
				+ ", order=" + order + ", captureCount=" + captureCount + "]";
	}

	public String getId() {
		return id;
	}

	@Column(columnName = "ID")
	public void setId(String id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	@Column(columnName = "物件名、路線名、駅名など")
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCategory() {
		return category;
	}

	@Column(columnName = "カテゴリー")
	public void setCategory(String category) {
		this.category = category;
	}

	public String getArea() {
		return area;
	}

	@Column(columnName = "地域")
	public void setArea(String area) {
		this.area = area;
	}

	public String getOrder() {
		return order;
	}

	@Column(columnName = "順序")
	public void setOrder(String order) {
		this.order = order;
	}

	public int getCaptureCount() {
		return captureCount;
	}

	@Column(columnName = "キャプチャ取得件数")
	public void setCaptureCount(int captureCount) {
		this.captureCount = captureCount;
	}

}
