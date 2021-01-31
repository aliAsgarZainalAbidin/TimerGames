package com.deval.event.fragment.detail

import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.deval.event.R
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*


class DetailFragment : Fragment(), View.OnClickListener {

    companion object{
        val ID = "ID"
        val NAMA = "NAMA"
        val DESC = "DESC"
        val BG = "BG"
    }

    private var seconds = 0

    // Is the stopwatch running?
    private val running = false

    private val wasRunning = false

    private var disposable: Disposable? = null
    private lateinit var nama : String
    private lateinit var id : String
    private lateinit var description : String
    private lateinit var bg : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.show_on_top)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments?.getString(ID).toString()

        btn_detail_stop.setOnClickListener(this)
        btn_detail_start.setOnClickListener(this)
        btn_detail_reset.setOnClickListener(this)
    }

    private fun runTimer() {

        // Get the text view.
        val timeView = tv_detail_time

        // Creates a new Handler
        val handler = Handler()

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(object : Runnable {
            override fun run() {
                val hours: Int = seconds / 3600
                val minutes: Int = seconds % 3600 / 60
                val secs: Int = seconds % 60

                // Format the seconds into hours, minutes,
                // and seconds.
                val time: String = java.lang.String
                    .format(
                        Locale.getDefault(),
                        "%d:%02d:%02d", hours,
                        minutes, secs
                    )

                // Set the text view text.
                timeView.text = time

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_detail_start -> {
                runTimer()
            }

            R.id.btn_detail_stop -> {

            }

            R.id.btn_detail_reset -> {

            }
        }
    }

}