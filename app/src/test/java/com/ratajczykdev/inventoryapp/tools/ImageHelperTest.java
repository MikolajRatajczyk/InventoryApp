package com.ratajczykdev.inventoryapp.tools;

import org.junit.Test;

import static com.ratajczykdev.inventoryapp.tools.ImageHelper.getRandomUuid;
import static org.junit.Assert.assertTrue;

public class ImageHelperTest {

    @Test
    public void getRandomUuid_noArguments_createRandomUuid() {
        String uuid = getRandomUuid();
        assertTrue(uuid != null && !uuid.isEmpty());
    }


}