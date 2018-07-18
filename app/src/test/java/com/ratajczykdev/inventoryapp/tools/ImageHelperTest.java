package com.ratajczykdev.inventoryapp.tools;

import android.database.Cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.ratajczykdev.inventoryapp.tools.ImageHelper.getRandomUuid;
import static com.ratajczykdev.inventoryapp.tools.ImageHelper.getStringFromCursor;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ImageHelperTest {

    final static String TEXT_FROM_CURSOR_WITH_DATA = "something";

    @Mock
    private Cursor cursor;


    @Test
    public void getRandomUuid_noArguments_createRandomUuid() {
        String uuid = getRandomUuid();
        assertTrue(uuid != null && !uuid.isEmpty());
    }

    @Test
    public void getStringFromCursor_cursorWithData_notEmptyString() {
        Mockito.when(cursor.isNull(Mockito.anyInt())).thenReturn(false);
        Mockito.when(cursor.getString(Mockito.anyInt())).thenReturn(TEXT_FROM_CURSOR_WITH_DATA);

        String receivedString = getStringFromCursor(cursor, 12);
        assertTrue(receivedString != null && !receivedString.isEmpty());
    }


}