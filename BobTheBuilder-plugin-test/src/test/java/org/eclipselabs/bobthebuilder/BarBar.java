package org.eclipselabs.bobthebuilder;

@SuppressWarnings("unused")
public class BarBar {

	private long id;
	private String description;

	private BarBar(Builder bobThebuilder) {
		this.id = bobThebuilder.id;
	}
	public static class Builder {
		private long id;

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

	}

}
