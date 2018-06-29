package jp.gr.java_conf.kfujinuma.study.yretester.yretester;

public class Candidate {

	private String title;
	private String uri;
	private String capturePath;
	public String getTitle() {
		return title;
	}
	public String getUri() {
		return uri;
	}
	public String getCapturePath() {
		return capturePath;
	}
	public Candidate(String title, String uri, String capturePath) {
		super();
		this.title = title;
		this.uri = uri;
		this.capturePath = capturePath;
	}
	@Override
	public String toString() {
		return "Candidate [title=" + title + ", uri=" + uri + ", capturePath=" + capturePath + "]";
	}

}
