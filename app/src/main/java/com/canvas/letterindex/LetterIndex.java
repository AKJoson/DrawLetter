package com.canvas.letterindex;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
public class LetterIndex extends View {
    private int mletterNormColor = Color.BLUE;
    private int mLetterSelColor = Color.RED;
    private Paint mNorPaint,mSelPaint;
    private int letterSize = 16;
    String[] letter = new String[]{
            "A","B","C","D","E","F","G","H","I","J","K",
            "L","M","N","O","P","Q","R","S","T","U","V",
            "W","X","Y","Z","#"
    };
    private int eachHeight;
    private int position = -1;
    private int currentPostion = -1;

    public LetterIndex(Context context) {
        this(context,null);
    }

    public LetterIndex(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LetterIndex(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterIndex);
        mletterNormColor = typedArray.getColor(R.styleable.LetterIndex_normalColor,mletterNormColor);
        mLetterSelColor = typedArray.getColor(R.styleable.LetterIndex_selectColor,mLetterSelColor);
        letterSize = (int) typedArray.getDimension(R.styleable.LetterIndex_letterSize,letterSize);
        typedArray.recycle();
        mNorPaint = new Paint();
        mSelPaint = new Paint();
        mNorPaint.setAntiAlias(true);
        mNorPaint.setColor(mletterNormColor);
        mNorPaint.setTextSize(sp2px(letterSize));
        mSelPaint.setAntiAlias(true);
        mSelPaint.setColor(mLetterSelColor);
        mSelPaint.setTextSize(sp2px(letterSize));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //宽度应该是一个字母的宽加上左右padding值
//        int textWidths = mSelPaint.getTextWidths("A", new float[1]);
        int textWidth = (int) mSelPaint.measureText("A");// A字母的宽度
        setMeasuredDimension(textWidth+getPaddingLeft()+getPaddingRight(),height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //总高度/总字符 得到一个字符的空间,
        eachHeight = (getHeight()-getPaddingTop()-getPaddingBottom()) / letter.length;

        for (int i=0;i<letter.length;i++){
            int centerY = i* eachHeight + eachHeight /2+getPaddingTop();
            //计算基线
            Paint.FontMetrics fontMetrics = mNorPaint.getFontMetrics();
            int dy = (int) ((fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
//            int baseLine = (int) (eachHeight/2+dy);
            int baseLine = centerY+dy;
            int  textWidth = (int) mSelPaint.measureText(letter[i]);
            int x = getWidth()/2-textWidth/2;
            if (position==i){
                canvas.drawText(letter[i],x, baseLine,mSelPaint);
            }else {
                canvas.drawText(letter[i], x, baseLine, mNorPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //获取y
                position = (int) ((event.getY()/(eachHeight)));
                mShowLetter.setText(letter[position],true);
                if (position<0){
                    position=-1;
                }
                if (position>letter.length){
                    position = letter.length;
                }
                if (currentPostion==position){
                    Log.e("TAG","进入return");
                    return true;
                }
                currentPostion = position;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mShowLetter.setText(letter[position],false);
                position = -1;
                invalidate();
                break;
        }
        return true;
    }

    private float sp2px(int letterSize) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                letterSize,getResources().getDisplayMetrics());
    }
    private ShowSelectLetter mShowLetter;
    public interface ShowSelectLetter{
        void setText(String text,boolean isShow);
    }
    public void setListener(ShowSelectLetter showSelectLetter){
        this.mShowLetter = showSelectLetter;
    }
}
