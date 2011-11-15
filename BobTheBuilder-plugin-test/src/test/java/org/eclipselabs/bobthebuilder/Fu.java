package org.eclipselabs.bobthebuilder;

@SuppressWarnings("unused")
public class Fu {

	private long id;
	private String description;
	private static final String HOLA = "HOLA";

	public static void main(String[] args) {/* do nothing */
	}

	public static class Builder {
		private long id;
		private String description;
		private static final String HOLA = "HOLA";

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
