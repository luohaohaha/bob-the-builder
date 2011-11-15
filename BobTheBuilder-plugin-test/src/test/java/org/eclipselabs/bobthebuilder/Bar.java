package org.eclipselabs.bobthebuilder;

@SuppressWarnings("unused")
public class Bar {

	private long id;
	private String description;

	public static class Builder {
		private long id;

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

	}

}
