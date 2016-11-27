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
        ShadowLog.stream = System.out;
        ArrayList<TestClass> a = new ArrayList<TestClass>();
        TestClass b = new TestClass(5); a.add(b);
        TestClass c = new TestClass(10); a.add(c);
        TestClass d = new TestClass(15); a.add(d);
        for(TestClass test: a) {
            ShadowLog.v("Here", Integer.toString(test.getA()));
            if(test.getA() > 6) {
                a.remove(test);
            }
        }
        assertEquals(a.size(), 1);
    }
}
