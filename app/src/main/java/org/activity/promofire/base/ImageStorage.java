package org.activity.promofire.base;

import java.io.File;

/**
 * Created by DESARROLLO on 07/01/17.
 */
public interface ImageStorage {
    String getImageUrl(String id);
    void upload(File file, String id, ImageStorageFinishedListener listener);
}