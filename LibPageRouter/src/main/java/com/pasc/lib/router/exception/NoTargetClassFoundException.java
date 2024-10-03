package com.pasc.lib.router.exception;

public class NoTargetClassFoundException extends RuntimeException {
  public NoTargetClassFoundException(String path) {
    super("can't find any target activity or service for path:"+path+", please check your annotation config!");
  }
}
