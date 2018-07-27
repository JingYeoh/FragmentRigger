package com.jkb.fragment.rigger.helper;

import android.support.v4.view.ViewCompat;
import android.view.View;

import java.lang.ref.WeakReference;

public class SharedElement {
    public WeakReference<View> referenceView;
    public String transactionName;
    public static SharedElement create(View refView, String transName){
        SharedElement sharedElement = new SharedElement();
        sharedElement.referenceView = new WeakReference<>(refView);
        sharedElement.transactionName = transName;
        return sharedElement;
    }
    public static SharedElement create(View refView){
        return create(refView, ViewCompat.getTransitionName(refView));
    }
}
