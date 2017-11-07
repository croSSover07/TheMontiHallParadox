package com.example.developer.themontihallparadox

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var arrayListDoorImageView: ArrayList<ImageView> = arrayListOf()
    private lateinit var newGameButton: Button
    private lateinit var actionsTextView: TextView
    private var winNumberDoor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        initViews()
        newGame()
    }

    private fun initViews() {
        actionsTextView = findViewById(R.id.textView_actions)
        arrayListDoorImageView.apply {
            add(findViewById(R.id.imageView_first_door))
            add(findViewById(R.id.imageView_second_door))
            add(findViewById(R.id.imageView_third_door))
            forEach { it.setOnClickListener(this@MainActivity) }
        }
        newGameButton = findViewById(R.id.button_new_game)
        newGameButton.setOnClickListener { newGame() }
    }

    override fun onClick(view: View) {
        (view.parent as View).setBackgroundColor(Color.YELLOW)
        actionsTextView.visibility = View.INVISIBLE
        getAvailableDoor(arrayListDoorImageView.indexOf(view as ImageView))
    }

    private fun getAvailableDoor(chosenNumberDoor: Int) {
        var openDoor = DoorEnum.ONE
        var chosenDoor = DoorEnum.ONE
        var anotherDoor = DoorEnum.ONE

        if (winNumberDoor == chosenNumberDoor) {
            when (chosenNumberDoor) {
                DoorEnum.ONE.ordinal -> {
                    chosenDoor = DoorEnum.ONE
                    openDoor = DoorEnum.TWO
                    anotherDoor = DoorEnum.THREE
                }
                DoorEnum.TWO.ordinal -> {
                    openDoor = DoorEnum.ONE
                    anotherDoor = DoorEnum.THREE
                    chosenDoor = DoorEnum.TWO
                }
                DoorEnum.THREE.ordinal -> {
                    openDoor = DoorEnum.ONE
                    anotherDoor = DoorEnum.TWO
                    chosenDoor = DoorEnum.THREE
                }
            }
        } else {
            when (chosenNumberDoor) {
                DoorEnum.ONE.ordinal -> {
                    chosenDoor = DoorEnum.ONE
                    if (winNumberDoor == DoorEnum.TWO.ordinal) {
                        openDoor = DoorEnum.THREE
                        anotherDoor = DoorEnum.TWO
                    } else {
                        openDoor = DoorEnum.TWO
                        anotherDoor = DoorEnum.THREE
                    }
                }
                DoorEnum.TWO.ordinal -> {
                    chosenDoor = DoorEnum.TWO
                    if (winNumberDoor == DoorEnum.ONE.ordinal) {
                        openDoor = DoorEnum.THREE
                        anotherDoor = DoorEnum.ONE
                    } else {
                        openDoor = DoorEnum.ONE
                        anotherDoor = DoorEnum.THREE
                    }
                }
                DoorEnum.THREE.ordinal -> {
                    chosenDoor = DoorEnum.THREE
                    if (winNumberDoor == DoorEnum.ONE.ordinal) {
                        openDoor = DoorEnum.TWO
                        anotherDoor = DoorEnum.ONE
                    } else {
                        openDoor = DoorEnum.ONE
                        anotherDoor = DoorEnum.TWO
                    }
                }
            }
        }
//        Log.i("TAG", winNumberDoor.toString() + " " + openDoor + " " + anotherDoor)
        arrayListDoorImageView[openDoor.ordinal].apply {
            setImageResource(R.drawable.ic_door_goat)
            setBackgroundColor(Color.GRAY)
        }
        showChangeChoiceAlertDialog(chosenDoor, openDoor, anotherDoor)
    }

    private fun showChangeChoiceAlertDialog(chosenDoor: DoorEnum, doorOpen: DoorEnum, anotherDoor: DoorEnum) {
        AlertDialog.Builder(this@MainActivity, R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.action_door_number_is_open, getString(doorOpen.getIdString())))
                .setMessage(getString(
                        R.string.question_change_your_choice,
                        getString(chosenDoor.getIdString()),
                        getString(anotherDoor.getIdString())))
                .setPositiveButton(R.string.yes, { _, _ ->
                    (arrayListDoorImageView[chosenDoor.ordinal].parent as View).setBackgroundColor(Color.TRANSPARENT)
                    checkForWin(anotherDoor.ordinal)
                })
                .setNegativeButton(R.string.no, { _, _ ->
                    checkForWin(chosenDoor.ordinal)
                })
                .setCancelable(false)
                .setOnDismissListener { newGameButton.visibility = View.VISIBLE }
                .show()
    }

    private fun checkForWin(chosenIntDoor: Int) {
        arrayListDoorImageView.forEach { it.isClickable = false }
        if (winNumberDoor == chosenIntDoor) {
            arrayListDoorImageView[winNumberDoor].apply {
                setImageResource(R.drawable.ic_door_car)
                setBackgroundColor(Color.GRAY)
                (parent as View).setBackgroundColor(Color.GREEN)
            }
            Toast.makeText(this, "YOU WIN", Toast.LENGTH_SHORT).show()
        } else {
            arrayListDoorImageView[chosenIntDoor].apply {
                setImageResource(R.drawable.ic_door_goat)
                setBackgroundColor(Color.GRAY)
                (parent as View).setBackgroundColor(Color.RED)

            }
            Toast.makeText(this, "YOU LOSE", Toast.LENGTH_SHORT).show()
            arrayListDoorImageView[winNumberDoor].apply {
                setBackgroundColor(Color.GRAY)
                setImageResource(R.drawable.ic_door_car)
            }
        }
    }

    private fun newGame() {
        setDefaultImage()
        actionsTextView.setText(R.string.action_choose_the_door)
        generateWinDoor()
        newGameButton.visibility = View.INVISIBLE
        actionsTextView.visibility = View.VISIBLE
    }

    private fun setDefaultImage() {
        arrayListDoorImageView.forEach {
            it.apply {
                setImageResource(R.drawable.ic_close_door)
                setBackgroundColor(Color.TRANSPARENT)
                (parent as View).setBackgroundColor(Color.TRANSPARENT)
                isClickable = true
            }
        }
    }

    private fun generateWinDoor() {
        val COUNT_DOOR = 3  //  three doors
        winNumberDoor = Random().nextInt(COUNT_DOOR)
    }
}