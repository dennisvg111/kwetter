package domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class KweetTests {
    @Test
    public void ComparableKweets()
    {
        List<Kweet> kweets = new ArrayList<Kweet>();
        Kweet kweet1 = new Kweet();
        kweet1.setDate(new Date(2018, 3, 20));
        Kweet kweet2 = new Kweet();
        kweet2.setDate(new Date(2018, 3, 15));
        Kweet kweet3 = new Kweet();
        kweet3.setDate(new Date(2018, 3, 11));
        kweets.add(kweet2);
        kweets.add(kweet1);
        kweets.add(kweet3);
        Collections.sort(kweets);

        Assert.assertEquals("Newest kweet should be first", kweet1, kweets.get(0));
        Assert.assertEquals("Newest kweet should be first", kweet2, kweets.get(1));
        Assert.assertEquals("Oldest kweet should be last", kweet3, kweets.get(2));
    }
}
