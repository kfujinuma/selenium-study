package jp.gr.java_conf.kfujinuma.study.yretester.yretester;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class SearchResult {

	private SearchCondition condition;
	private String count;
	private String uri;
	private String capturePath;
	private List<Candidate> candidates = new ArrayList<Candidate>();
	private boolean success = false;
	private Exception exception;
	private String errorStackTrace;

	public boolean isSuccess() {
		return success;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		pw.flush();
		this.errorStackTrace = sw.toString();
	}

	public SearchCondition getCondition() {
		return condition;
	}

	public void setCondition(SearchCondition condition) {
		this.condition = condition;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getCapturePath() {
		return capturePath;
	}

	public void setCapturePath(String capturePath) {
		this.capturePath = capturePath;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}

	@Override
	public String toString() {
		return "SearchResult [condition=" + condition + ", count=" + count + ", uri=" + uri + ", capturePath="
				+ capturePath + ", candidates=" + candidates + ", success=" + success + ", exception=" + exception
				+ ", errorStackTrace=" + errorStackTrace + "]";
	}

}
