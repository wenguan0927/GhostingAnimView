package com.widget.anim.ghosting

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.graphics.LinearGradient
import androidx.core.graphics.ColorUtils

class MultiTrapezoidView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    companion object {
        val COLOR_LEVEL_GREAT = Color.parseColor("#35FFD7")
        val COLOR_LEVEL_GOOD = Color.parseColor("#21A8FF")
        val COLOR_LEVEL_PERFECT = Color.parseColor("#00FF6C")
        val COLOR_LEVEL_DEFAULT = Color.parseColor("#00FF6C")
    }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var overLayMargin: Float = 12F
    private val COLOR_DEFAULT = Color.parseColor("#00FF6C")
    private val COLOR_FADE = Color.parseColor("#00000000")
    private var cornerEffect = CornerPathEffect(5f)
    private val mTriangleFirst = Path()
    private val mTriangleSecond = Path()
    private val mTriangleThird = Path()
    private var mFirstLinGradient: LinearGradient? = null
    private var mSecondLinGradient: LinearGradient? = null
    private var mThirdLinGradient: LinearGradient? = null

    init {
        val typeArry =
            context.obtainStyledAttributes(attrs, R.styleable.TriangleView, defStyleAttr, 0)
        overLayMargin = typeArry.getDimension(R.styleable.TriangleView_overlay_margin, 12F)
        typeArry?.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initParam()
    }

    private fun initParam() {
        mPaint.pathEffect = cornerEffect

        // 最底部的三角形
        mTriangleFirst.moveTo(
            paddingLeft + overLayMargin * 2,
            (measuredHeight - paddingBottom).toFloat()
        )
        mTriangleFirst.lineTo(
            measuredWidth - paddingRight - overLayMargin * 2,
            (measuredHeight - paddingBottom).toFloat()
        )
        mTriangleFirst.lineTo(measuredWidth / 2.0f, overLayMargin * 2)
        mTriangleFirst.close()

        mTriangleSecond.moveTo(
            paddingLeft + overLayMargin,
            measuredHeight - paddingBottom - overLayMargin
        )
        mTriangleSecond.lineTo(
            measuredWidth - paddingRight - overLayMargin,
            measuredHeight - paddingBottom - overLayMargin
        )
        mTriangleSecond.lineTo(measuredWidth / 2.0f, overLayMargin)
        mTriangleSecond.close()

        mTriangleThird.moveTo(
            paddingLeft.toFloat(),
            measuredHeight - paddingBottom - overLayMargin * 2
        )
        mTriangleThird.lineTo(
            (measuredWidth - paddingRight).toFloat(),
            measuredHeight - paddingBottom - overLayMargin * 2
        )
        mTriangleThird.lineTo(measuredWidth / 2.0f, 0f)
        mTriangleThird.close()

        setColor(COLOR_DEFAULT, false)
    }

    /**
     * 改变三层叠加颜色
     */
    fun setColor(color: Int, invalidate: Boolean = true) {
        mFirstLinGradient = LinearGradient(
            measuredWidth / 2.0f,
            (measuredHeight - paddingBottom).toFloat(),
            measuredWidth / 2.0f,
            overLayMargin * 2 + measuredHeight * 2 / 3,
            ColorUtils.setAlphaComponent(color, 26),
            COLOR_FADE,
            Shader.TileMode.CLAMP
        )

        mSecondLinGradient = LinearGradient(
            measuredWidth / 2.0f,
            measuredHeight - paddingBottom - overLayMargin,
            measuredWidth / 2.0f,
            overLayMargin + measuredHeight * 2 / 3,
             ColorUtils.setAlphaComponent(color, 77),
            COLOR_FADE,
            Shader.TileMode.CLAMP
        )

        mThirdLinGradient = LinearGradient(
            measuredWidth / 2.0f,
            measuredHeight - paddingBottom - overLayMargin * 2,
            measuredWidth / 2.0f,
            +measuredHeight * 2.0f / 3,
            color,
            COLOR_FADE,
            Shader.TileMode.CLAMP
        )
        if(invalidate){
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mPaint.shader = mFirstLinGradient
        canvas?.drawPath(mTriangleFirst, mPaint)
        mPaint.shader = mSecondLinGradient
        canvas?.drawPath(mTriangleSecond, mPaint)
        mPaint.shader = mThirdLinGradient
        canvas?.drawPath(mTriangleThird, mPaint)
    }
}
