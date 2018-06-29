package jp.gr.java_conf.kfujinuma.study.yretester.yretester;

import java.util.List;

public class TestResult {
	public TestResult(SearchSettings settings, List<SearchResult> results) {
		super();
		this.settings = settings;
		this.results = results;
	}
	public SearchSettings getSettings() {
		return settings;
	}
	public void setSettings(SearchSettings settings) {
		this.settings = settings;
	}
	public List<SearchResult> getResults() {
		return results;
	}
	public void setResults(List<SearchResult> results) {
		this.results = results;
	}
	SearchSettings settings;
	List<SearchResult> results;
	@Override
	public String toString() {
		return "TestResult [settings=" + settings + ", results=" + results + "]";
	}
}
