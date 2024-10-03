package com.pasc.lib.router.aspect;
import org.junit.Assert;
import org.junit.Test;

public class FlagUtilTest {

  @Test
  public void enableFlag() {
    int flags=0;
    int flag = FlagUtil.enableFlag(flags, 4);
    Assert.assertEquals(flag,0x0010);
    Assert.assertEquals(FlagUtil.enableFlag(flags,3),0x0008);
    Assert.assertEquals(FlagUtil.enableFlag(flags,14),0x4000);
    Assert.assertEquals(FlagUtil.enableFlag(flags,7),0x0080);

  }

  @Test
  public void disableFlag() {
    int flags=0xffff;
    Assert.assertEquals(FlagUtil.disableFlag(flags,3),0xfff7);
    Assert.assertEquals(FlagUtil.disableFlag(flags,14),0xbfff);
    Assert.assertEquals(FlagUtil.disableFlag(flags,7),0xff7f);
  }

  @Test
  public void flagIsEnable() {
    int flags=0xf75;
    Assert.assertTrue(FlagUtil.flagIsEnable(flags,0));
    Assert.assertTrue(FlagUtil.flagIsEnable(flags,2));
    Assert.assertTrue(FlagUtil.flagIsEnable(flags,8));
    Assert.assertTrue(FlagUtil.flagIsEnable(flags,9));
  }
}