package org.eclipselabs.bobthebuilder;

@SuppressWarnings("unused")
public class BarFu {

	private long id;
	private String description;

	public static void main(String[] args) {/* do nothing */
	}

	private BarFu(Builder builder) {
		this.id = builder.id;
		this.description = builder.description;
	}
	public static class Builder {
		private long id;
		private String description;

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}
	}

}
