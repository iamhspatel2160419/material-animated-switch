package com.github.glomadrian.materialanimatedswitch.painter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.github.glomadrian.materialanimatedswitch.SwitchInboxPinedState;
import com.github.glomadrian.materialanimatedswitch.observer.BallFinishObservable;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Adrián García Lomas
 */
public class IconPressPainter extends IconPainter {

  private ValueAnimator enterAnimator;
  private ValueAnimator exitAnimator;
  private BallFinishObservable ballFinishObservable;

  public IconPressPainter(Context context, Bitmap bitmap, BallFinishObservable ballFinishObservable,
      int margin) {
    super(context, bitmap, margin);
    initValueAnimator();
    this.ballFinishObservable = ballFinishObservable;
    initObserver();
  }

  private void initValueAnimator() {
    enterAnimator = ValueAnimator.ofInt();
    enterAnimator.setDuration(400);
    enterAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    enterAnimator.addUpdateListener(new EnterValueAnimatorListener());

    exitAnimator = ValueAnimator.ofInt();
    exitAnimator.setDuration(200);
    exitAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    exitAnimator.addUpdateListener(new ExitValueAnimatorListener());
  }

  @Override public void onSizeChanged(int height, int width) {
    super.onSizeChanged(height, width);
    int iconCenterY = height / 2 - (imageHeight / 2);
    enterAnimator.setIntValues(-80, iconCenterY);
    exitAnimator.setIntValues(iconCenterY, iconCenterY + 80);
    iconXPosition = (width - margin) - (imageWidth / 2);
  }

  @Override public void setColor(int color) {

  }

  @Override public int getColor() {
    return 0;
  }

  private void initObserver() {
    ballFinishObservable.addObserver(new BallFinishListener());
  }

  @Override public void setState(SwitchInboxPinedState state) {
    switch (state) {
      case PRESS:
        isVisible = true;
        enterAnimator.start();
        break;
      case RELEASE:
        exitAnimator.start();
    }
  }

  private class BallFinishListener implements Observer {

    @Override public void update(Observable observable, Object data) {
      BallFinishObservable.BallState ballState = ((BallFinishObservable) observable).getState();
      switch (ballState) {
        case RELEASE:
          isVisible = false;
          break;
      }
    }
  }

  private class EnterValueAnimatorListener implements ValueAnimator.AnimatorUpdateListener {
    @Override public void onAnimationUpdate(ValueAnimator animation) {
      iconYPosition = (int) animation.getAnimatedValue();
    }
  }

  private class ExitValueAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

    @Override public void onAnimationUpdate(ValueAnimator animation) {
      iconYPosition = (int) animation.getAnimatedValue();
    }
  }
}
