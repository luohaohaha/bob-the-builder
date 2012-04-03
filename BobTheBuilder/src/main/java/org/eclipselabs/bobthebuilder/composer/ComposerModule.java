package org.eclipselabs.bobthebuilder.composer;

import org.eclipselabs.bobthebuilder.FieldTextBuilder;

import com.google.inject.AbstractModule;

public class ComposerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ConstructorComposer.class);
    bind(BuilderComposer.class);
    bind(FieldTextBuilder.FieldAssignmentBuilder.class);
    bind(FieldTextBuilder.FieldDeclarationBuilder.class);
    bind(FieldTextBuilder.WithMethodBuilder.class);
  }

}
