package com.justcodeit.moyeo.study;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class SuccessRes<T> extends BaseResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public SuccessRes() {
        super(200);
        data = null;
    }

    public SuccessRes(T data) {
        super(200);
        this.data = data;
    }
}
