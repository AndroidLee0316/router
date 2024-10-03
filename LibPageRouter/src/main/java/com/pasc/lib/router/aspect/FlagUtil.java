package com.pasc.lib.router.aspect;

public class FlagUtil {
  public static int enableFlag(int flags, int index) {
    return (1 << index) | flags;
  }

  public static int disableFlag(int flags, int index) {
    int i = 1 << index;
    return ~i & flags;
  }

  public static boolean flagIsEnable(int flags, int index) {
    return ((flags >> index) & 1) == 1;
  }
}
