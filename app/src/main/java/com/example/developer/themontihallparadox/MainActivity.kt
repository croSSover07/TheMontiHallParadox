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
import com.example.developer.themontihallparadox.Common.Companion.getEnumNameByIndex
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
        var doorIntOpen = 0
        var chosenIntDoor = 0
        var anotherIntDoor = 0

        if (winNumberDoor == chosenNumberDoor) {
            when (chosenNumberDoor) {
                Enum.ONE.ordinal -> {
                    chosenIntDoor = Enum.ONE.ordinal
                    doorIntOpen = Enum.TWO.ordinal
                    anotherIntDoor = Enum.THREE.ordinal
                }
                Enum.TWO.ordinal -> {
                    doorIntOpen = Enum.ONE.ordinal
                    anotherIntDoor = Enum.THREE.ordinal
                    chosenIntDoor = Enum.TWO.ordinal
                }
                Enum.THREE.ordinal -> {
                    doorIntOpen = Enum.ONE.ordinal
                    anotherIntDoor = Enum.TWO.ordinal
                    chosenIntDoor = Enum.THREE.ordinal
                }
            }
        } else {
            when (chosenNumberDoor) {
                Enum.ONE.ordinal -> {
                    chosenIntDoor = Enum.ONE.ordinal
                    if (winNumberDoor == 1) {
                        doorIntOpen = Enum.THREE.ordinal
                        anotherIntDoor = Enum.TWO.ordinal
                    } else {
                        doorIntOpen = Enum.TWO.ordinal
                        anotherIntDoor = Enum.THREE.ordinal
                    }
                }
                Enum.TWO.ordinal -> {
                    chosenIntDoor = Enum.TWO.ordinal
                    if (winNumberDoor == 0) {
                        doorIntOpen = Enum.THREE.ordinal
                        anotherIntDoor = Enum.ONE.ordinal
                    } else {
                        doorIntOpen = Enum.ONE.ordinal
                        anotherIntDoor = Enum.THREE.ordinal
                    }
                }
                Enum.THREE.ordinal -> {
                    chosenIntDoor = Enum.THREE.ordinal
                    if (winNumberDoor == 0) {
                        doorIntOpen = Enum.TWO.ordinal
                        anotherIntDoor = Enum.ONE.ordinal
                    } else {
                        doorIntOpen = Enum.ONE.ordinal
                        anotherIntDoor = Enum.TWO.ordinal
                    }
                }
            }
        }
//        Log.i("TAG", winNumberDoor.toString() + " " + doorIntOpen + " " + anotherIntDoor)
        arrayListDoorImageView[doorIntOpen].apply {
            setImageResource(R.drawable.ic_door_goat)
            setBackgroundColor(Color.GRAY)
        }
        showChangeChoiceAlertDialog(chosenIntDoor, doorIntOpen, anotherIntDoor)
    }

    private fun showChangeChoiceAlertDialog(chosenIntDoor: Int, doorIntOpen: Int, anotherIntDoor: Int) {
        val anotherStringDoor = getEnumNameByIndex(anotherIntDoor)
        val doorStringOpen = getEnumNameByIndex(doorIntOpen)
        val chosenStringDoor = getEnumNameByIndex(chosenIntDoor)

        AlertDialog.Builder(this@MainActivity,R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.action_door_number_is_open, doorStringOpen))
                .setMessage(getString(R.string.question_change_your_choice, chosenStringDoor, anotherStringDoor))
                .setPositiveButton(R.string.yes, { _, _ ->
                    (arrayListDoorImageView[chosenIntDoor].parent as View).setBackgroundColor(Color.TRANSPARENT)
                    checkForWin(anotherStringDoor)
                })
                .setNegativeButton(R.string.no, { _, _ ->
                    checkForWin(chosenStringDoor)
                })
                .setCancelable(false)
                .setOnDismissListener { newGameButton.visibility = View.VISIBLE }
                .show()
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

    private fun checkForWin(chosenStringDoor: String) {
        val chosenIntDoor = Enum.valueOf(chosenStringDoor).ordinal
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
                //(parent as View).setBackgroundColor(Color.GREEN)
            }
        }
    }

    private fun generateWinDoor() {
        val COUNT_DOOR = 3  //  three doors
        winNumberDoor = Random().nextInt(COUNT_DOOR)
    }
}