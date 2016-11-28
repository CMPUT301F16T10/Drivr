package ca.ualberta.cs.drivr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Ignore this, it's just for testing stuff.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class TestClassTest {

    @Test
    public void TestingShit() {
        assertEquals(2+2, 4);
    }
}
