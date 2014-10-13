package com.iflytek.iFramework.ui.tagcloud;
/**
 * Komodo Lab: Tagin! Project: 3D Tag Cloud
 * Google Summer of Code 2011
 * @authors Reza Shiftehfar, Sara Khosravinasr and Jorge Silva
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagCloudView extends RelativeLayout {

    float oldX, oldY;

    public TagCloudView(Context mContext, int width, int height, List<Tag> tagList, final OnTagClickListener onTagClickListener) {
        this(mContext, width, height, tagList, 6, 34, 1, onTagClickListener); //default for min/max text size
    }

    public TagCloudView(Context mContext, int width, int height, List<Tag> tagList) {
        this(mContext, width, height, tagList, 6, 34, 1, null); //default for min/max text size
    }

    public TagCloudView(Context mContext, int width, int height, List<Tag> tagList,
                        int textSizeMin, int textSizeMax, int scrollSpeed, final OnTagClickListener onTagClickListener) {

        super(mContext);
        this.mContext = mContext;
        this.textSizeMin = textSizeMin;
        this.textSizeMax = textSizeMax;

        tspeed = scrollSpeed;

        //set the center of the sphere on center of our screen:
        centerX = width / 2;
        centerY = height / 2;
        radius = Math.min(centerX * 0.95f, centerY * 0.95f); //use 95% of screen
        //since we set tag margins from left of screen, we shift the whole tags to left so that
        //it looks more realistic and symmetric relative to center of screen in X direction
        shiftLeft = (int) (Math.min(centerX * 0.15f, centerY * 0.15f));

        // initialize the TagCloud from a list of tags
        //Filter() func. screens tagList and ignores Tags with same text (Case Insensitive)
        mTagCloud = new TagCloud(Filter(tagList), (int) radius,
                textSizeMin,
                textSizeMax
        );
        float[] tempColor1 = {0.9412f, 0.7686f, 0.2f, 1}; //rgb Alpha
        //{1f,0f,0f,1}  red       {0.3882f,0.21568f,0.0f,1} orange
        //{0.9412f,0.7686f,0.2f,1} light orange
        float[] tempColor2 = {1f, 0f, 0f, 1}; //rgb Alpha
        //{0f,0f,1f,1}  blue      {0.1294f,0.1294f,0.1294f,1} grey
        //{0.9412f,0.7686f,0.2f,1} light orange
        mTagCloud.setTagColor1(tempColor1);//higher color
        mTagCloud.setTagColor2(tempColor2);//lower color
        mTagCloud.setRadius((int) radius);
        mTagCloud.create(true); // to put each Tag at its correct initial location


        //update the transparency/scale of tags
        mTagCloud.setAngleX(mAngleX);
        mTagCloud.setAngleY(mAngleY);
        mTagCloud.update();

        mTextView = new ArrayList<TextView>();
        mParams = new ArrayList<LayoutParams>();
        //Now Draw the 3D objects: for all the tags in the TagCloud
        Iterator it = mTagCloud.iterator();
        Tag tempTag;

        for (int i = 0; i < mTagCloud.getSize(); i++) {
            tempTag = mTagCloud.get(i);
            tempTag.setParamNo(i); //store the parameter No. related to this tag

            mTextView.add(new TextView(this.mContext));
            mTextView.get(i).setText(tempTag.getText());

            mParams.add(new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                    )
            );
            mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mParams.get(i).setMargins(
                    (int) (centerX - shiftLeft + tempTag.getLoc2DX()),
                    (int) (centerY + tempTag.getLoc2DY()),
                    0,
                    0);
            mTextView.get(i).setLayoutParams(mParams.get(i));

            mTextView.get(i).setSingleLine(true);
            int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
                    (int) (tempTag.getColorR() * 255),
                    (int) (tempTag.getColorG() * 255),
                    (int) (tempTag.getColorB() * 255));
            mTextView.get(i).setTextColor(mergedColor);
            mTextView.get(i).setTextSize((int) (tempTag.getTextSize() * tempTag.getScale()));
      //      mTextView.get(i).setTag(tempTag);
            addView(mTextView.get(i));

            final int pos = i;
            mTextView.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTagClickListener.onTagClick(TagCloudView.this, view, pos);
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        mAngleX = (y) * tspeed * TRACKBALL_SCALE_FACTOR;
        mAngleY = (-x) * tspeed * TRACKBALL_SCALE_FACTOR;

        mTagCloud.setAngleX(mAngleX);
        mTagCloud.setAngleY(mAngleY);
        mTagCloud.update();

        Iterator it = mTagCloud.iterator();
        Tag tempTag;
        while (it.hasNext()) {
            tempTag = (Tag) it.next();
            mParams.get(tempTag.getParamNo()).setMargins(
                    (int) (centerX - shiftLeft + tempTag.getLoc2DX()),
                    (int) (centerY + tempTag.getLoc2DY()),
                    0,
                    0);
            mTextView.get(tempTag.getParamNo()).setTextSize((int) (tempTag.getTextSize() * tempTag.getScale()));
            int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
                    (int) (tempTag.getColorR() * 255),
                    (int) (tempTag.getColorG() * 255),
                    (int) (tempTag.getColorB() * 255));
            mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
            mTextView.get(tempTag.getParamNo()).bringToFront();
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = x;
                oldY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //rotate elements depending on how far the selection point is from center of cloud
                float dx = (x - oldX) * 2;
                float dy = (y - oldY) * 2;

                mAngleX = (dy / radius) * tspeed * TOUCH_SCALE_FACTOR;
                mAngleY = (-dx / radius) * tspeed * TOUCH_SCALE_FACTOR;

                mTagCloud.setAngleX(mAngleX);
                mTagCloud.setAngleY(mAngleY);
                mTagCloud.update();

                Iterator it = mTagCloud.iterator();
                Tag tempTag;
                while (it.hasNext()) {
                    tempTag = (Tag) it.next();
                    mParams.get(tempTag.getParamNo()).setMargins(
                            (int) (centerX - shiftLeft + tempTag.getLoc2DX()),
                            (int) (centerY + tempTag.getLoc2DY()),
                            0,
                            0);
                    mTextView.get(tempTag.getParamNo()).setTextSize((int) (tempTag.getTextSize() * tempTag.getScale()));
                    int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
                            (int) (tempTag.getColorR() * 255),
                            (int) (tempTag.getColorG() * 255),
                            (int) (tempTag.getColorB() * 255));
                    mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
                    mTextView.get(tempTag.getParamNo()).bringToFront();
                }

                break;
        }

        return true;

    }


    //the filter function makes sure that there all elements are having unique Text field:
    List<Tag> Filter(List<Tag> tagList) {
        //current implementation is O(n^2) but since the number of tags are not that many,
        //it is acceptable.
        List<Tag> tempTagList = new ArrayList();
        Iterator itr = tagList.iterator();
        Iterator itrInternal;
        Tag tempTag1, tempTag2;
        //for all elements of TagList
        while (itr.hasNext()) {
            tempTag1 = (Tag) (itr.next());
            boolean found = false;
            //go over all elements of temoTagList
            itrInternal = tempTagList.iterator();
            while (itrInternal.hasNext()) {
                tempTag2 = (Tag) (itrInternal.next());
                if (tempTag2.getText().equalsIgnoreCase(tempTag1.getText())) {
                    found = true;
                    break;
                }
            }
            if (found == false)
                tempTagList.add(tempTag1);
        }
        return tempTagList;
    }


    public TagCloud getTagCloud() {
        return this.mTagCloud;
    }

    private final float TOUCH_SCALE_FACTOR = .8f;
    private final float TRACKBALL_SCALE_FACTOR = 10;
    private float tspeed;
    private TagCloud mTagCloud;
    private float mAngleX = 0;
    private float mAngleY = 0;
    private float centerX, centerY;
    private float radius;
    private Context mContext;
    private int textSizeMin, textSizeMax;
    private List<TextView> mTextView;
    private List<LayoutParams> mParams;
    private int shiftLeft;

    private OnTagClickListener onTagClickListener;

    public interface OnTagClickListener {
        public void onTagClick(TagCloudView tagCloudView, View tag, int position);
    }
}
