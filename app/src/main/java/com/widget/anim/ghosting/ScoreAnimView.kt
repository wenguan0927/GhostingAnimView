package com.widget.anim.ghosting

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import kotlinx.android.synthetic.main.view_score_anim_view.view.*

/**
 * Tai得分动画
 */
class ScoreAnimView : FrameLayout {

    private var mPackAnimSet: AnimatorSet? = null
    private var mPackAnimSetShadow: AnimatorSet? = null
    private var mPackAnimSpecSetShadow: AnimatorSet? = null
    private var mScorePlusAnimSet: AnimatorSet? = null
    private var mScorePlusShadowAnimSet: AnimatorSet? = null
    private var mScoreAnimSet: AnimatorSet? = null
    private var mScoreAnimShadowSet: AnimatorSet? = null
    private var mScoreAnimShadowSpecSet: AnimatorSet? = null
    private var currentScore = 0
    private var mRepeatCount = 7
    private val mHandler = TaiHandler()
    private val MSG_BLINK = 2001
    private val MSG_HIDE = 2002
    private var mScoreAnimListener: ScoreAnimListener? = null
    private val X_TRANSLATION_Y = resources.getDimension(R.dimen.x_translation_y)
    private val SCORE_TRANSLATION_Y = resources.getDimension(R.dimen.score_translation_y)

    private val ANIM_DURATION_MS = 462L

    inner class TaiHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_BLINK -> {
                    if (mRepeatCount % 2 == 0) {
                        scoreNumShadow.alpha = 1.0f
                        scoreNumShadow.setStrokeWidth(5.0f)
                        scorePlusShadow.alpha = 1.0f
                        scorePlusShadow.setStrokeWidth(4.0f)
                    } else {
                        scoreNumShadow.alpha = 0.975f
                        scoreNumShadow.setStrokeWidth(1.5f)
                        scorePlusShadow.alpha = 0.975f
                        scorePlusShadow.setStrokeWidth(1.5f)
                    }
                    scoreNumShadow.invalidate()
                    scorePlusShadow.invalidate()
                    mRepeatCount--
                    if (mRepeatCount >= 0) {
                        val msg = obtainMessage()
                        msg.what = MSG_BLINK
                        mHandler.sendMessageDelayed(msg, 50)
                    } else {
                        resetScore()
                    }
                }
                MSG_HIDE -> {
                    val strokeWidth = resources.getDimension(R.dimen.score_anim_stroke_width)
                    scoreNumShadow.alpha = 1.0f
                    scoreNumShadow.setStrokeWidth(strokeWidth)
                    scorePlusShadow.alpha = 1.0f
                    scorePlusShadow.setStrokeWidth(strokeWidth)
                    mScoreAnimListener?.onAnimEnd()
                    resetScoreNum()
                }
            }
        }
    }

    constructor(context: Context) : super(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {

    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    init {
        View.inflate(context, R.layout.view_score_anim_view, this)
        initAnimSet()
    }

    private fun initAnimSet() {
        val scaleXAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNum, "scaleX", 0.3f, 1f)
        val scaleYAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNum, "scaleY", 0.3f, 1f)
        val alphaAnimator: ObjectAnimator = ObjectAnimator.ofFloat(scoreNum, "alpha", 0.3f, 1f)
        val transYAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNum, "translationY", SCORE_TRANSLATION_Y, 0f)

        mScoreAnimSet = AnimatorSet()
        mScoreAnimSet?.interpolator = AccelerateInterpolator()
        mScoreAnimSet?.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator, transYAnimator)
        mScoreAnimSet?.setDuration(ANIM_DURATION_MS)

        mScoreAnimSet?.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator) {
                mScoreAnimListener?.onAnimStart()
            }

            override fun onAnimationEnd(p0: Animator) {
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        val scaleXPlusAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scorePlus, "scaleX", 0.3f, 1f)
        val scaleYPlusAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scorePlus, "scaleY", 0.3f, 1f)
        val alphaPlusAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scorePlus, "alpha", 0.3f, 1f)
        val transYPlusAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scorePlus, "translationY", X_TRANSLATION_Y, 0f)

        val scaleXPlusShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scorePlusShadow, "scaleX", 0.3f, 1f)
        val scaleYPlusShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scorePlusShadow, "scaleY", 0.3f, 1f)
        val alphaPlusShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scorePlusShadow, "alpha", 0.3f, 1f)
        val transYPlusShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scorePlusShadow, "translationY", X_TRANSLATION_Y, 0f)

        mScorePlusAnimSet = AnimatorSet()
        mScorePlusAnimSet?.interpolator = AccelerateInterpolator()
        mScorePlusAnimSet?.setDuration(ANIM_DURATION_MS)
        mScorePlusAnimSet?.playTogether(
            scaleXPlusAnimator,
            scaleYPlusAnimator,
            alphaPlusAnimator,
            transYPlusAnimator
        )

        mScorePlusShadowAnimSet = AnimatorSet()
        mScorePlusShadowAnimSet?.interpolator = AccelerateInterpolator()
        mScorePlusShadowAnimSet?.setDuration(ANIM_DURATION_MS)
        mScorePlusShadowAnimSet?.playTogether(
            scaleXPlusShadowAnimator,
            scaleYPlusShadowAnimator,
            alphaPlusShadowAnimator,
            transYPlusShadowAnimator
        )

        val scaleXShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNumShadow, "scaleX", 0.3f, 1f)
        val scaleYShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNumShadow, "scaleY", 0.3f, 1f)
        val alphaShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNumShadow, "alpha", 0.3f, 1f)
        val shadowDxAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNumShadow, "shadowDx", 20f, 0f)
        val strokeShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNumShadow, "strokeWidth", 2f, 25f, 2f)
        val transYShadowAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(scoreNumShadow, "translationY", SCORE_TRANSLATION_Y, 0f)

        mScoreAnimShadowSet = AnimatorSet()
        mScoreAnimShadowSet?.interpolator = AccelerateInterpolator()
        mScoreAnimShadowSet?.playTogether(
            scaleXShadowAnimator,
            scaleYShadowAnimator,
            alphaShadowAnimator,
            transYShadowAnimator
        )
        mScoreAnimShadowSet?.setDuration(ANIM_DURATION_MS)
        mScoreAnimShadowSet?.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                resetScore()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        mScoreAnimShadowSpecSet = AnimatorSet()
        mScoreAnimShadowSpecSet?.interpolator = AccelerateInterpolator()
        mScoreAnimShadowSpecSet?.setDuration(ANIM_DURATION_MS)
        mScoreAnimShadowSpecSet?.playTogether(
            scaleXShadowAnimator,
            scaleYShadowAnimator,
            alphaShadowAnimator,
            shadowDxAnimator,
            strokeShadowAnimator,
            transYShadowAnimator
        )
        mScoreAnimShadowSpecSet?.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                mRepeatCount = 7
                mHandler.removeMessages(MSG_BLINK)
                val msg = mHandler.obtainMessage()
                msg.what = MSG_BLINK
                mHandler.sendMessage(msg)
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        //上层字体动画
        mPackAnimSet = AnimatorSet()
        mPackAnimSet?.playTogether(mScoreAnimSet, mScorePlusAnimSet)
        //底层重影叠加动画
        mPackAnimSetShadow = AnimatorSet()
        mPackAnimSetShadow?.playTogether(mScoreAnimShadowSet, mScorePlusShadowAnimSet)
        //底层重影叠加加重动画
        mPackAnimSpecSetShadow = AnimatorSet()
        mPackAnimSpecSetShadow?.playTogether(mScoreAnimShadowSpecSet, mScorePlusShadowAnimSet)
    }

    private fun resetScore() {
        val msg = mHandler.obtainMessage()
        msg.what = MSG_HIDE
        mHandler.sendMessageDelayed(msg, 500)
    }

    fun resetScoreNum() {
        scorePlus.setText("")
        scorePlusShadow.setText("")
        scoreNum.setText("")
        scoreNumShadow.setText("")
    }

    fun showScore(score: Int) {
        if (score > 99) {
            currentScore = 99
        } else {
            currentScore = score
        }
        scorePlus.setText("x")
        scoreNum.setText(currentScore.toString())

        mPackAnimSet?.start()
        mHandler.postDelayed({
            scorePlusShadow.setText("x")
            scoreNumShadow.setText(currentScore.toString())
            if (currentScore % 10 == 0 || currentScore == 99) {
                mPackAnimSpecSetShadow?.start()
            } else {
                mPackAnimSetShadow?.start()
            }
        }, 50)
    }

    fun setAnimListener(listener: ScoreAnimListener) {
        this.mScoreAnimListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
        mPackAnimSet?.cancel()
        mPackAnimSpecSetShadow?.cancel()
        mPackAnimSetShadow?.cancel()
        clearAnimation()
    }
}