package com.collector.forest;

import java.util.List;
import java.util.Map;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;

public interface MyClient {

    @Post("http://localhost:8080/api/metric/upload")
    String dataUpload(@JSONBody List mreq);

}
