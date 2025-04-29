package com.hibob.anylink.chat.typeHandler;

import com.alibaba.fastjson.TypeReference;
import com.hibob.anylink.chat.typeHandler.base.ListTypeHandler;

import java.util.List;

public class LongListTypeHandler extends ListTypeHandler<Long> {
    @Override
    protected TypeReference<List<Long>> specificType() {
        return new TypeReference<List<Long>>() {};
    }
}
