package com.erendogan.catchthebear

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.erendogan.catchthebear.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var runnable: Runnable = Runnable{}
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var score = 0
    private var max = 0
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.bear.visibility = View.INVISIBLE

        sharedPreferences = getSharedPreferences("com.erendogan.catchthebear", MODE_PRIVATE)
        max = sharedPreferences.getInt("max",0)
           binding.textView3.text = "Max Skor:$max"
    }



    fun start(view: View){
        binding.bear.visibility = View.VISIBLE
        binding.bear.isClickable = true
        binding.root.isEnabled = false

        object : CountDownTimer(15000,1000){
            override fun onTick(millisUntilFinished: Long) {
                binding.textView2.text= "Zaman:"+millisUntilFinished/1000
            }

            override fun onFinish() {
                handler.removeCallbacks(runnable)
                binding.bear.visibility = View.INVISIBLE
                binding.bear.isClickable = false
                if (score>max){
                    sharedPreferences.edit().putInt("max",score).apply()
                    binding.textView3.text = "Max Skor:$score"
                }
                val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                alertDialogBuilder.setTitle("Süreniz Bitti !")
                alertDialogBuilder.setMessage("Skorunuz: $score\nTekrar oynamak ister misiniz?")
                alertDialogBuilder.setPositiveButton("Evet"){ _, _ ->
                    val intent = intent
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                alertDialogBuilder.setNegativeButton("Hayır"){ _, _ ->
                    finish()
                }
                alertDialogBuilder.setOnCancelListener{
                    finish()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
            }.start()

        runnable = Runnable {
            val randX = kotlin.random.Random.nextInt(resources.displayMetrics.widthPixels-binding.bear.measuredWidth+5)
            val randY = kotlin.random.Random.nextInt(resources.displayMetrics.heightPixels-binding.bear.measuredHeight+5)
            binding.bear.x = randX.toFloat()
            binding.bear.y = randY.toFloat()
            handler.postDelayed(runnable,500)
        }
        handler.post(runnable)
    }
    fun skor (view:View){
        score++
        binding.textView.text = "Skor:$score"
    }
}