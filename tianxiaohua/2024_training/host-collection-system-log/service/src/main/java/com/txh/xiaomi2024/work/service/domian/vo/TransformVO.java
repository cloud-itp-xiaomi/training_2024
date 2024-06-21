package com.txh.xiaomi2024.work.service.domian.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class TransformVO<T, V> implements Serializable {
    public abstract V transFormDoc(T doc);

    public List<V> transFormDocs(List<T> docs){
        List<V> list = new ArrayList<>();
        for (T doc : docs) {
            list.add(transFormDoc(doc));
        }
        return list;
    }
}