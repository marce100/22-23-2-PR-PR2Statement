package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Test;
import uoc.ds.pr.UniversityEventsPR2;

public class LevelHelperTest {

    @Test
    public void levelHelperTest() {
        Assert.assertEquals(UniversityEventsPR2.Level.DIAMOND, LevelHelper.getLevel(999));
        Assert.assertEquals(UniversityEventsPR2.Level.DIAMOND, LevelHelper.getLevel(35));
        Assert.assertEquals(UniversityEventsPR2.Level.PLATINUM, LevelHelper.getLevel(18));
        Assert.assertEquals(UniversityEventsPR2.Level.GOLD, LevelHelper.getLevel(15));
        Assert.assertEquals(UniversityEventsPR2.Level.GOLD, LevelHelper.getLevel(12));
        Assert.assertEquals(UniversityEventsPR2.Level.GOLD, LevelHelper.getLevel(11));
        Assert.assertEquals(UniversityEventsPR2.Level.SILVER, LevelHelper.getLevel(10));
        Assert.assertEquals(UniversityEventsPR2.Level.SILVER, LevelHelper.getLevel(6));
        Assert.assertEquals(UniversityEventsPR2.Level.BRONZE, LevelHelper.getLevel(5));
        Assert.assertEquals(UniversityEventsPR2.Level.BRONZE, LevelHelper.getLevel(3));
        Assert.assertEquals(UniversityEventsPR2.Level.BRONZE, LevelHelper.getLevel(0));
    }

}
