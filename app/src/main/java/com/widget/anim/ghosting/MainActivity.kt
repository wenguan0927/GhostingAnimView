package com.widget.anim.ghosting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mHandler = Handler(Looper.getMainLooper())
    private var mScore = 7
    private var mScoreAbsoluteSizeSpan: AbsoluteSizeSpan? = null
    private var mScoreTailAbsoluteSizeSpan: AbsoluteSizeSpan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mScoreAbsoluteSizeSpan = AbsoluteSizeSpan(
            resources.getDimensionPixelSize(R.dimen.score_text_size),
            false
        )
        mScoreTailAbsoluteSizeSpan = AbsoluteSizeSpan(
            resources.getDimensionPixelSize(R.dimen.score_text_size) / 20,
            false
        )

        mPlayAnim.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mScore = 1
                mHandler.removeCallbacksAndMessages(null)
                setScore()
            }
        })
    }

    /* fun showScore(){
        mScore += 1
        mScoreAnimTv.showScore(mScore)
        if(mScore < 3){
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_GOOD)
        } else if(mScore >= 3 && mScore <6){
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_GREAT)
        } else if(mScore >= 6) {
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_PERFECT)
        }
        mHandler.postDelayed({
            if(mScore < 10) {
                showScore()
            }
        }, 2000)
    } */

    fun setScore() {
        mScore += 1

        if (mScore % 10 == 7) {
            //避免右上角被裁剪，使用小尺寸空格填充
            var str7 = " ${mScore} "
            var spannable7 = SpannableString(str7)
            spannable7.setSpan(
                mScoreTailAbsoluteSizeSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable7.setSpan(
                mScoreAbsoluteSizeSpan, 1, str7.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable7.setSpan(
                mScoreTailAbsoluteSizeSpan,
                str7.length - 1,
                str7.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            mGhostingTv.setScore(spannable7)
        } else {
            mGhostingTv.setScore(mScore.toString(), mScore % 10 == 0)
        }

        if (mScore < 3) {
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_GOOD)
        } else if (mScore >= 3 && mScore < 6) {
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_GREAT)
        } else if (mScore >= 6) {
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_PERFECT)
        }
        mHandler.postDelayed({
            if (mScore < 12) {
                setScore()
            }
        }, 1000)
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacksAndMessages(null)
    }
}
