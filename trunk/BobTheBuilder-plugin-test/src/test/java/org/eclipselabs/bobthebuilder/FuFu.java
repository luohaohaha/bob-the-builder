package org.eclipselabs.bobthebuilder;

@SuppressWarnings("unused")
public class FuFu {

	private long id;
	private String description;
	private boolean whatsUp;

	public static class Builder {
		private long id;

		public Builder withId(long id) {
			this.id = id;
			return this;
		}
		
		public FuFu build() {
			validate();
			return new FuFu();
		}

		private void validate() {
			
		}

	}

}
