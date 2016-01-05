package com.pawcare.source.util;

import java.util.List;

/**
 * Created by anmumukh on 12/26/15.
 */
public interface DataFetchedCallback {
    void dataFetched(List dataList, Exception e);
}
