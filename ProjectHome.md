The [builder pattern](http://books.google.com/books?id=ka2VUBqHiWkC&lpg=PA11&ots=yYEgLhq5P3&dq=effective%20java%20builder%20pattern&pg=PA11#v=onepage&q&f=false) in Java requires a fair amount of boiler plate code.

The bob-the-builder plugin for eclipse may help you effectively create and maintain the builder pattern as your pojo evolves.

With bob-the-builder you can go **from** here
```
public class FuBaz {

	private long id;
	private String description;
	private Set<Long> longs;


}
```

**to** here with the following combinations in Eclipse:

  1. Activate bob-the-builder when working on your pojo with: `Ctrl + 6`
  1. Click the `Bob, build` button on the plugin dialog
  1. Format your pojo with:  `Ctrl + Shift + f`

```
public class FuBaz {

  private long id;

  private String description;

  private Set<Long> longs;

  private FuBaz(Builder builder) {
    this.longs = builder.longs;
    this.id = builder.id;
    this.description = builder.description;
  }

  public static class Builder {

    private Set<Long> longs;

    private long id;

    private String description;

    public Builder withLongs(Set<Long> longs) {
      this.longs = longs;
      return this;
    }

    public Builder withId(long id) {
      this.id = id;
      return this;
    }

    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public FuBaz build() {
      validate();
      return new FuBaz(this);
    }

    private void validate() {
      Validate.notNull(longs, "longs may not be null");
      Validate.isTrue(!longs.isEmpty(), "longs may not be empty");
      Validate.isTrue(id > 0L, "id should be set");
      Validate.isTrue(!StringUtils.isBlank(description), "description may not be blank");
    }
  }

}
```

### Features ###
  * Compatible with Eclipse Helios, Indigo and [MyEclipse 10](http://www.myeclipseide.com/)
  * bob-the-builder will analyze your pojo, find its fields and generate:
    * a private constructor in your pojo that takes a Builder class
    * a builder class with
      * convenience 'with' methods than can be chained
      * a validate method with simple `not null` and `empty collection` validations
      * a build method that invokes a private constructor in the enclosing class
  * Any time you invoke it, it will scan the active Java file in your editor.
    * If the  builder class is missing, it will create it for you.
    * If it already exists it will notify about the differences and prompt you for the possible options.
    * It will also detect fields and 'with' methods that are present in the builder class but not in your pojo.

### Install ###
The instructions to install the plugin are outlined [here](http://code.google.com/a/eclipselabs.org/p/bob-the-builder/wiki/HowToInstallBobTheBuilder).

### Similar projects ###
  * [bpep](http://code.google.com/p/bpep/)
  * http://code.google.com/p/fluent-builders-generator-eclipse-plugin/
  * http://code.google.com/p/make-builder/