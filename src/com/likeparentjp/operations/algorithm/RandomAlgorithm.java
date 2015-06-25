package com.likeparentjp.operations.algorithm;

import java.util.Random;

import android.graphics.Bitmap;

public class RandomAlgorithm implements LikeParentAlgorithm {

    @Override
    public float analyzeDadPercent(Bitmap dad, Bitmap mom, Bitmap child) {
        Random r = new Random();
        return r.nextFloat() * 100;
    }

}
