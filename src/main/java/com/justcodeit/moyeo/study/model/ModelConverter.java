package com.justcodeit.moyeo.study.model;

public interface ModelConverter<I, O> {

  O convert(I source);

}
