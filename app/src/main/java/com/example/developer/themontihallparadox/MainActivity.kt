package com.example.developer.themontihallparadox

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var firstDoorImageView: ImageView
    private lateinit var secondDoorImageView: ImageView
    private lateinit var thirdDoorImageView: ImageView
    private var isChosenDoor: Boolean = false
    private lateinit var actionsTextView: TextView
    private lateinit var chosenDoorImageView: ImageView
    private lateinit var winDoorImageView: ImageView
    private var isStartNewGame = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        newGame()
    }

    private fun initViews() {
        firstDoorImageView = findViewById(R.id.imageView_first_door)
        secondDoorImageView = findViewById(R.id.imageView_second_door)
        thirdDoorImageView = findViewById(R.id.imageView_third_door)

        firstDoorImageView.setOnClickListener(this)
        secondDoorImageView.setOnClickListener(this)
        thirdDoorImageView.setOnClickListener(this)

        actionsTextView = findViewById(R.id.textView_actions)
    }

    override fun onClick(view: View) {
        if (isStartNewGame) {
            newGame()
        } else {
            val localView = view as ImageView
            if (isChosenDoor) {
                chosenDoorImageView.setBackgroundColor(Color.TRANSPARENT)
                if (localView == winDoorImageView) {
                    actionsTextView.setText(R.string.you_win)
                    localView.setImageResource(R.drawable.ic_door_car)
                } else {
                    actionsTextView.setText(R.string.you_lose)
                    localView.setImageResource(R.drawable.ic_door_goat)
                }

                isStartNewGame = true
            } else {
                isChosenDoor = true
                actionsTextView.setText(R.string.action_change_your_choice)
                chosenDoorImageView = localView
                localView.setBackgroundColor(Color.YELLOW)
                openLoseDoor()
            }
        }
    }

    private fun openLoseDoor() {
        //TODO change to random open loseDoor
        when (chosenDoorImageView) {
            firstDoorImageView -> {
                if (secondDoorImageView == winDoorImageView) {
                    thirdDoorImageView.apply {
                        setImageResource(R.drawable.ic_door_goat)
                        isClickable = false
                    }
                } else {
                    secondDoorImageView.apply {
                        setImageResource(R.drawable.ic_door_goat)
                        isClickable = false
                    }
                }
            }
            secondDoorImageView -> {
                if (firstDoorImageView == winDoorImageView) {
                    thirdDoorImageView.apply {
                        setImageResource(R.drawable.ic_door_goat)
                        isClickable = false
                    }
                } else {
                    firstDoorImageView.apply {
                        setImageResource(R.drawable.ic_door_goat)
                        isClickable = false
                    }
                }
            }
            thirdDoorImageView -> {
                if (secondDoorImageView == winDoorImageView) {
                    firstDoorImageView.apply {
                        setImageResource(R.drawable.ic_door_goat)
                        isClickable = false
                    }
                } else {
                    secondDoorImageView.apply {
                        setImageResource(R.drawable.ic_door_goat)
                        isClickable = false
                    }
                }
            }
        }
    }

    private fun newGame() {
        isChosenDoor = false
        isStartNewGame = false
        actionsTextView.setText(R.string.action_choose_the_door)

        setDefaultImage()
        generateWinDoor()
    }

    private fun setDefaultImage() {
        firstDoorImageView.apply {
            setImageResource(R.drawable.ic_close_door)
            setBackgroundColor(Color.TRANSPARENT)
            isClickable = true
        }
        secondDoorImageView.apply {
            setImageResource(R.drawable.ic_close_door)
            setBackgroundColor(Color.TRANSPARENT)
            isClickable = true
        }
        thirdDoorImageView.apply {
            setImageResource(R.drawable.ic_close_door)
            setBackgroundColor(Color.TRANSPARENT)
            isClickable = true
        }
    }

    private fun generateWinDoor() {
        val COUNT_DOOR = 3  //  (1,2,3)
        when (Random().nextInt(COUNT_DOOR)) {
            0 -> winDoorImageView = firstDoorImageView
            1 -> winDoorImageView = secondDoorImageView
            2 -> winDoorImageView = thirdDoorImageView
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_game -> newGame()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}
