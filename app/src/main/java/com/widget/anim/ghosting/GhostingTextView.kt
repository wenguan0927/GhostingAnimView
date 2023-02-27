package com.widget.anim.ghosting

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator

class GhostingTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    private var mStrokeWidth: Float = 2f
    private var mStrokeColor: Int = 0

    // 结束时候是否需要抖动效果
    private var mIsNeedShake = false
    private var mScaleAnimSet: AnimatorSet? = null
    private var mScaleSpecAnimSet: AnimatorSet? = null
    private var mPackAnimSet: AnimatorSet? = null
    private var mPackSpecAnimSet: AnimatorSet? = null
    private var mShakeAnimSet: AnimatorSet? = null
    private var mAnimTextSize = 0f
    private var mIsAnimEnd = false
    private val GHOSTING_NUMBER = 7
    private var mGhostingSize = GHOSTING_NUMBER
    private var mScaleRatio = 0.0f

    // 从下往上变化的高度
    private var TRANSLATION_Y = 0f
    private var TEXT_SIZE = 0f
    private var mHandler = Handler(Looper.getMainLooper())
    private var mEndingAnimator: ObjectAnimator? = null
    private var VIEW_STROKE_WIDTH = 2f
    private var mIsShakeCancel = false

    init {
        val typeArry =
            context.obtainStyledAttributes(attrs, R.styleable.ScoreTextView, defStyleAttr, 0)
        VIEW_STROKE_WIDTH = typeArry.getDimension(R.styleable.ScoreTextView_stroke_width, 2.0f)
        mStrokeColor = typeArry.getColor(
            R.styleable.ScoreTextView_stroke_color,
            Color.parseColor("#77E41E")
        )
        TRANSLATION_Y = textSize * 3 / 4
        TEXT_SIZE = textSize
        mIsNeedShake = typeArry.getBoolean(R.styleable.ScoreTextView_need_shake, false)
        typeArry?.recycle()
        val fontScore =
            Typeface.createFromAsset(context.getAssets(), "Montserrat-ExtraBoldItalic.ttf")
        setTypeface(fontScore)
        paint.style = Paint.Style.STROKE
        paint.color = mStrokeColor
        paint.strokeWidth = VIEW_STROKE_WIDTH
        mStrokeWidth = VIEW_STROKE_WIDTH
        var scaleAnimator = ObjectAnimator.ofFloat(this, "scaleShift", 0.3f, 1.0f)
        val transYAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(this, "translationY", TRANSLATION_Y, 0f)
        val strokeAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(
                this,
                "strokeWidth",
                VIEW_STROKE_WIDTH,
                VIEW_STROKE_WIDTH * 5,
                VIEW_STROKE_WIDTH
            )
        mEndingAnimator =
            ObjectAnimator.ofInt(this, "ghostingSize", GHOSTING_NUMBER, 0)
        mEndingAnimator?.interpolator = AccelerateInterpolator()
        mEndingAnimator?.setDuration(120)
        val scaleTransXAnim: ObjectAnimator =
            ObjectAnimator.ofFloat(
                this,
                "translationX",
                8f,
                -8f,
                8f,
                -8f,
                8f,
                -8f,
                8f,
                -8f,
                8f,
                -8f,
                8f,
                -8f,
                8f,
                -8f,
                8f,
                -8f,
                8f,
                -8f,
                8f,
                -8f,
                0f
            )
        val translateXAnim: ObjectAnimator =
            ObjectAnimator.ofFloat(
                this,
                "translationX",
                8f,
                0f,
                8f,
                0f,
                8f,
                0f,
                8f,
                0f,
                8f,
                0f,
                8f,
                0f,
                8f,
                0f,
                8f,
                0f,
                8f,
                0f,
                8f,
                0f
            )
        val translateYAnim: ObjectAnimator =
            ObjectAnimator.ofFloat(
                this,
                "translationY",
                4f,
                0f,
                4f,
                0f,
                4f,
                0f,
                4f,
                0f,
                4f,
                0f,
                4f,
                0f,
                4f,
                0f,
                4f,
                0f,
                4f,
                0f,
                4f,
                0f
            )

        mShakeAnimSet = AnimatorSet()
        mShakeAnimSet?.playTogether(translateXAnim, translateYAnim)
        mShakeAnimSet?.interpolator = AccelerateInterpolator()
        mShakeAnimSet?.setDuration(500)
        mShakeAnimSet?.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator) {
                mIsShakeCancel = false
                mStrokeWidth = 10f
                invalidate()
            }

            override fun onAnimationEnd(p0: Animator) {
                mStrokeWidth = 2f
                if (!mIsShakeCancel) {
                    visibility = INVISIBLE
                }
            }

            override fun onAnimationCancel(p0: Animator) {
                mIsShakeCancel = true
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })
        mEndingAnimator?.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                mHandler.postDelayed({
                    visibility = INVISIBLE
                }, 500)
            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }
        })

        mScaleAnimSet = AnimatorSet()
        mScaleAnimSet?.playTogether(scaleAnimator, transYAnimator)
        mScaleAnimSet?.interpolator = AccelerateInterpolator()
        mScaleAnimSet?.setDuration(300)

        mPackAnimSet = AnimatorSet()
        mPackAnimSet?.playSequentially(mScaleAnimSet, mEndingAnimator)

        mScaleSpecAnimSet = AnimatorSet()
        if (mIsNeedShake) {
            mScaleSpecAnimSet?.playTogether(mScaleAnimSet, strokeAnimator, scaleTransXAnim)
        } else {
            mScaleSpecAnimSet?.playTogether(mScaleAnimSet, strokeAnimator)
        }

        mScaleSpecAnimSet?.interpolator = AccelerateInterpolator()
        mScaleSpecAnimSet?.setDuration(300)

        mPackSpecAnimSet = AnimatorSet()
        if (mIsNeedShake) {
            mPackSpecAnimSet?.playSequentially(mScaleSpecAnimSet, mEndingAnimator, mShakeAnimSet)
        } else {
            mPackSpecAnimSet?.playSequentially(mScaleSpecAnimSet, mEndingAnimator)
        }
    }

    fun setGhostingSize(size: Int) {
        mIsAnimEnd = true
        mGhostingSize = size
        invalidate()
    }

    fun setStrokeWidth(strokeWidth: Float) {
        mStrokeWidth = strokeWidth
    }

    fun setScaleShift(ratio: Float) {
        mIsAnimEnd = false
        mScaleRatio = ratio
        mAnimTextSize = TEXT_SIZE * ratio
        invalidate()
    }

    fun setScore(score: CharSequence, isSpec: Boolean = false) {
        setText(score)
        startAnim(isSpec)
    }

    fun setScore(score: String, isSpec: Boolean = false) {
        setText(score)
        startAnim(isSpec)
    }

    fun startAnim(isSpec: Boolean) {
        visibility = VISIBLE
        mHandler.removeCallbacksAndMessages(null)
        translationX = 0f
        translationY = 0f
        if (isSpec) {
            mPackSpecAnimSet?.cancel()
            mPackAnimSet?.cancel()
            mPackSpecAnimSet?.start()
        } else {
            mPackSpecAnimSet?.cancel()
            mPackAnimSet?.cancel()
            mPackAnimSet?.start()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (mIsAnimEnd) {
            for (j in mGhostingSize downTo 0) {
                var itemAlpha = 255
                if (mGhostingSize > 0) {
                    itemAlpha = 255 - 255 / mGhostingSize * j
                }
                if (itemAlpha < 0) {
                    itemAlpha = 0
                }
                drawGhosting(mAnimTextSize - j * 20, itemAlpha, canvas)
            }
        } else {
            for (i in GHOSTING_NUMBER downTo 0) {
                var itemAlpha = 255 - 255 / GHOSTING_NUMBER * i
                if (itemAlpha < 0) {
                    itemAlpha = 0
                }
                drawGhosting(mAnimTextSize - i * 20, itemAlpha, canvas)
            }
        }
    }

    private fun drawGhosting(textSize: Float, alpha: Int, canvas: Canvas?) {
        if (textSize < 0) {
            return
        }
        paint.textSize = textSize
        paint.strokeWidth = mStrokeWidth
        paint.alpha = alpha
        val stringWidth: Float = paint.measureText(text.toString())
        val x = (width - stringWidth) / 2
        val fontMetrics: Paint.FontMetrics = paint.getFontMetrics()
        val y = height / 2 + (Math.abs(fontMetrics.ascent)) / 2
        canvas!!.drawText(text.toString(), x, y, paint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mShakeAnimSet?.removeAllListeners()
        mEndingAnimator?.removeAllListeners()
        mHandler.removeCallbacksAndMessages(null)
        mPackSpecAnimSet?.cancel()
        mPackAnimSet?.cancel()
        clearAnimation()
    }
}