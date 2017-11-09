package com.example.developer.themontihallparadox

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager.LayoutParams
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var doorImageViews: Array<ImageView>
    private lateinit var newGameButton: Button
    private lateinit var actionsTextView: TextView

    private var chosenDoor: Door? = null
    private var winDoor = Door.ONE

    private var random = Random()

    private val valueAnimator = ValueAnimator.ofArgb(Color.TRANSPARENT, Color.YELLOW).apply {
        duration = 1000L
        repeatCount = ValueAnimator.INFINITE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        actionsTextView = findViewById(R.id.textView_actions)

        doorImageViews = Door.values().map {
            findViewById<ImageView>(it.getViewId()).apply { setOnClickListener(this@MainActivity) }
        }.toTypedArray()

        newGameButton = findViewById(R.id.button_new_game)
        newGameButton.setOnClickListener(this)

        startNewGame()
    }

    private fun startNewGame() {
        winDoor = Door.values()[random.nextInt(Door.values().size)]

        chosenDoor = null

        valueAnimator.cancel()
        valueAnimator.removeAllUpdateListeners()

        Door.values().forEach { updateDoor(it, false) }

        actionsTextView.setText(R.string.action_choose_the_door)
        newGameButton.visibility = View.INVISIBLE
        actionsTextView.visibility = View.VISIBLE
    }

    override fun onClick(view: View) = when (view.id) {
        newGameButton.id -> startNewGame()
        else -> chooseDoorWihId(view.id)
    }

    private fun chooseDoorWihId(id: Int) {
        if (chosenDoor != null) return

        val chosenDoor = Door.doorForViewWithId(id) ?: return
        val openedDoor = Door.values().first { it != winDoor && it != chosenDoor }

        this.chosenDoor = chosenDoor
        updateDoor(chosenDoor, false, false)
        updateDoor(openedDoor, true, false)

        askToChangeChoice(chosenDoor, openedDoor)
    }

    private fun updateDoor(door: Door, isOpened: Boolean, isWiningDoor: Boolean = false) {

        doorImageViews[door.ordinal].apply {
            setBackgroundColor(Color.TRANSPARENT)
            if (isOpened) {
                if (isWiningDoor) {
                    setImageResource(R.drawable.ic_door_car)
                } else {
                    setImageResource(R.drawable.ic_door_goat)
                }
            } else {
                if (door == chosenDoor) {
                    startAnimation(this,
                            ContextCompat.getColor(this@MainActivity, R.color.transparent),
                            ContextCompat.getColor(this@MainActivity, R.color.choose))
                } else {
                    setImageResource(R.drawable.ic_close_door)
                }
            }
        }
    }

    private fun askToChangeChoice(chosenDoor: Door, openedDoor: Door) {
        val closedDoor = Door.values().first { it != chosenDoor && it != openedDoor }

        AlertDialog.Builder(this@MainActivity, R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.action_door_number_is_open, getString(openedDoor.getTitleResId())))
                .setMessage(getString(R.string.question_change_your_choice, getString(chosenDoor.getTitleResId()), getString(closedDoor.getTitleResId())))
                .setPositiveButton(R.string.yes, { _, _ ->
                    showResults(closedDoor)
                })
                .setNegativeButton(R.string.no, { _, _ ->
                    showResults(chosenDoor)
                })
                .setCancelable(false)
                .setOnDismissListener { newGameButton.visibility = View.VISIBLE }
                .show()
    }

    private fun showResults(chosenDoor: Door) {
        valueAnimator.cancel()
        valueAnimator.removeAllUpdateListeners()

        Door.values().forEach {
            updateDoor(it, true, it == winDoor)
        }

        if (chosenDoor == winDoor) {
            startAnimation(doorImageViews[chosenDoor.ordinal],
                    ContextCompat.getColor(this, R.color.transparent),
                    ContextCompat.getColor(this, R.color.win))
        } else {
            startAnimation(doorImageViews[chosenDoor.ordinal],
                    ContextCompat.getColor(this, R.color.transparent),
                    ContextCompat.getColor(this, R.color.lose))
        }

        Toast.makeText(this, if (chosenDoor == winDoor) R.string.you_win else R.string.you_lose, Toast.LENGTH_SHORT).show()
    }

    private fun startAnimation(view: View, startColor: Int, endColor: Int) {
        valueAnimator.apply {
            setIntValues(startColor, endColor)
            valueAnimator.addUpdateListener { view.setBackgroundColor(it.animatedValue as Int) }
        }
        valueAnimator.start()
    }
}