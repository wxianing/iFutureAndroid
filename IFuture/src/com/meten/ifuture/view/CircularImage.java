package com.meten.ifuture.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.lidroid.xutils.BitmapUtils;
import com.meten.ifuture.R;

public class CircularImage extends MaskedImage {
	
	private float headBorder;
    private float defaultBorderSize;
    private boolean hasBorder;
	
	public CircularImage(Context paramContext) {
		this(paramContext,null);
	}

	public CircularImage(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet,0);
	}

	public CircularImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		defaultBorderSize = paramContext.getResources().getDimension(R.dimen.headBorder);
	}
	
	public void setBorderSize(float size){
		if(size > 0){
            headBorder = size;
            hasBorder = true;
		}else{
            hasBorder = false;
        }
	}

    public void setHasBorder(boolean hasBorder){
        this.hasBorder = hasBorder;
    }
	
	public void setImageUrl(String url){
        BitmapUtils bitmapUtils = new BitmapUtils(getContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.head_default);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.head_default);
        bitmapUtils.display(this,url);
	}

	public Bitmap createMask() {
        if(hasBorder ){
            if(headBorder == 0){
                headBorder = defaultBorderSize;
            }
        }else{
            headBorder = 0;
        }
		int i = getWidth();
		int j = getHeight();
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
		Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint(1);
		localPaint.setColor(-16777216);
		float f1 = getWidth();
		float f2 = getHeight();
		
		RectF localRectF = new RectF(headBorder, headBorder, localBitmap.getWidth()-headBorder, localBitmap.getHeight()-headBorder);
		localCanvas.drawOval(localRectF, localPaint);
		return localBitmap;
	}
}
