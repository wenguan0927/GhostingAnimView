package com.widget.anim.ghosting

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet

/**
 * 得分自定义TextView
 */
class ScoreTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    private var mStrokeWidth: Float = 0f
    private var mStrokeColor: Int = 0

    init {
        if (attrs != null) {
            val typeArry =
                context.obtainStyledAttributes(attrs, R.styleable.TaiScoreTextView, defStyleAttr, 0)
            mStrokeWidth = typeArry.getDimension(R.styleable.TaiScoreTextView_stroke_width, 2.0f)
            mStrokeColor = typeArry.getColor(
                R.styleable.TaiScoreTextView_stroke_color,
                Color.parseColor("#77E41E")
            )
            typeArry?.recycle()
        }
        val fontScore =
            Typeface.createFromAsset(context.getAssets(), "Montserrat-ExtraBoldItalic.ttf")
        setTypeface(fontScore)
    }

    fun setShadowDx(dx: Float){
        super.setShadowLayer(shadowRadius, dx, shadowDy, shadowColor)
    }

    fun setStrokeWidth(strokeWidth: Float){
        this.mStrokeWidth = strokeWidth
    }

    override fun onDraw(canvas: Canvas?) {
        paint.style = Paint.Style.STROKE
        paint.color = mStrokeColor
        paint.strokeWidth = mStrokeWidth
        super.onDraw(canvas)
    }
}