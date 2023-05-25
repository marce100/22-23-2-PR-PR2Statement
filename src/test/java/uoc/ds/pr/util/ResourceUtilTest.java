package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Test;

import static uoc.ds.pr.UniversityEvents.*;

public class ResourceUtilTest {


    @Test
    public void hasFlagTest1() {
        byte resource = ResourceUtil.getFlag(FLAG_COMPUTER, FLAG_AUXILIARY_MIC, FLAG_TOUCH_SCREEN);

        Assert.assertTrue(ResourceUtil.hasComputer(resource));
        Assert.assertTrue(ResourceUtil.hasAuxiliaryMic(resource));
        Assert.assertTrue(ResourceUtil.hasTouchScreen(resource));
        Assert.assertFalse(ResourceUtil.hasVideoProjector(resource));
        Assert.assertFalse(ResourceUtil.hasAllOpts(resource));
    }

    @Test
    public void hasFlagTest2() {
        byte resource = ResourceUtil.getFlag(FLAG_TOUCH_SCREEN, FLAG_COMPUTER);

        Assert.assertTrue(ResourceUtil.hasTouchScreen(resource));
        Assert.assertTrue(ResourceUtil.hasComputer(resource));
        Assert.assertFalse(ResourceUtil.hasAuxiliaryMic(resource));
        Assert.assertFalse(ResourceUtil.hasVideoProjector(resource));
        Assert.assertFalse(ResourceUtil.hasAllOpts(resource));
    }

    @Test
    public void hasFlagTest3() {
        byte resource = ResourceUtil.getFlag(FLAG_ALL_OPTS);

        Assert.assertTrue(ResourceUtil.hasComputer(resource));
        Assert.assertTrue(ResourceUtil.hasAuxiliaryMic(resource));
        Assert.assertTrue(ResourceUtil.hasTouchScreen(resource));
        Assert.assertTrue(ResourceUtil.hasVideoProjector(resource));
        Assert.assertTrue(ResourceUtil.hasAllOpts(resource));
    }

    @Test
    public void hasFlagTest4() {
        byte resource = ResourceUtil.getFlag(FLAG_AUXILIARY_MIC, FLAG_VIDEO_PROJECTOR, FLAG_COMPUTER, FLAG_TOUCH_SCREEN);

        Assert.assertTrue(ResourceUtil.hasComputer(resource));
        Assert.assertTrue(ResourceUtil.hasAuxiliaryMic(resource));
        Assert.assertTrue(ResourceUtil.hasTouchScreen(resource));
        Assert.assertTrue(ResourceUtil.hasVideoProjector(resource));
        Assert.assertTrue(ResourceUtil.hasAllOpts(resource));
    }

}
