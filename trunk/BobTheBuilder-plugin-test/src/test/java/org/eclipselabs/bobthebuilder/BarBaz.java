package org.eclipselabs.bobthebuilder;

@SuppressWarnings("unused")
public class BarBaz {

	private long id;
	private String description;

	public static void main(String[] args) {/* do nothing */
	}

	public static class Builder {
		private long id;
		private String description;
		private int index;

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}
		
		public Builder withIndex(int index) {
			this.index = index;
			return this;
		}
	}

}
