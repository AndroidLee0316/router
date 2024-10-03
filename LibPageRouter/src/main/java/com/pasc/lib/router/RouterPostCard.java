package com.pasc.lib.router;

import android.app.Activity;

public class RouterPostCard {
  private final RouterPath routerPath;
  private final Object[] args;
  private PostCard postCard;

  RouterPostCard(RouterPath routerPath, Object[] args) {
    this.routerPath = routerPath;
    this.args = args;
  }

  public Object navigation(Activity activity) throws Exception {
    try {
      postCard = routerPath.toPostCard(args);
      return postCard.navigation(activity);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
