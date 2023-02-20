package com.widget.anim.ghosting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mHandler = Handler(Looper.getMainLooper())
    private var mScore = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPlayAnim.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                mScore = 0
                mHandler.removeCallbacksAndMessages(null)
                showScore()
            }
        })
    }

    fun showScore(){
        mScore +=2
        mScoreAnimV.showScore(mScore)
        if(mScore < 3){
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_GOOD)
        } else if(mScore >= 3 && mScore <6){
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_GREAT)
        } else if(mScore >= 6) {
            mMTrapezoidV.setColor(MultiTrapezoidView.COLOR_LEVEL_PERFECT)
        }
        mHandler.postDelayed({
            if(mScore < 13) {
                showScore()
            }
        }, 1500)
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacksAndMessages(null)
    }
}