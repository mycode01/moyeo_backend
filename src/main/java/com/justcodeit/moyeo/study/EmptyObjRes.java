package com.justcodeit.moyeo.study;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmptyObjRes extends SuccessRes<Map<String, Object>> {
    public EmptyObjRes() {
        super(new HashMap<>());
    }
}
