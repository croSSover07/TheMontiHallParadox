package com.example.developer.themontihallparadox

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager.LayoutParams
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val DOOR_IMAGE_VIEW_IDS = arrayListOf(R.id.imageView_first_door, R.id.imageView_second_door, R.id.imageView_third_door)
    }

    //  TODO: Naming. Можно было использовать массив.
//    private var doorImageViews: ArrayList<ImageView> = arrayListOf()
    private lateinit var doorImageViews: Array<ImageView>
    private lateinit var newGameButton: Button
    private lateinit var actionsTextView: TextView

// TODO: незачем хранить индекс если мы используем enum
//    private var winNumberDoor: Int = 0

    private var chosenDoor: Door? = null
    private var winDoor = Door.ONE

    private var random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        actionsTextView = findViewById(R.id.textView_actions)

        doorImageViews = DOOR_IMAGE_VIEW_IDS.map {
            findViewById<ImageView>(it).apply {
                setOnClickListener(this@MainActivity)
            }
        }.toTypedArray()

//        TODO: Можно еще упростить получение рефренсов на вьюхи дверей, используя Kotlin Android Extensions
//        https://kotlinlang.org/docs/tutorials/android-plugin.html
        doorImageViews = arrayOf(imageView_first_door, imageView_second_door, imageView_first_door)

        newGameButton = findViewById(R.id.button_new_game)
        newGameButton.setOnClickListener(this)

        startNewGame()
    }

//    TODO: не зачем выносить в отдельный метод.
    private fun initViews() {
        actionsTextView = findViewById(R.id.textView_actions)

//      TODO: Можно упростить
//        doorImageViews.apply {
//            add(findViewById(R.id.imageView_first_door))
//            add(findViewById(R.id.imageView_second_door))
//            add(findViewById(R.id.imageView_third_door))
//            forEach { it.setOnClickListener(this@MainActivity) }
//        }
        doorImageViews = DOOR_IMAGE_VIEW_IDS.map {
            findViewById<ImageView>(it).apply {
                setOnClickListener(this@MainActivity)
            }
        }.toTypedArray()

        newGameButton = findViewById(R.id.button_new_game)
//        TODO: Можно использовать текущую активити, а не создавать анонимный класс.
//        newGameButton.setOnClickListener { startNewGame() }
        newGameButton.setOnClickListener(this)
    }

//    TODO: Так как теперь onClick хендлит и другой тип view кроме дверей, то нам необходимо это учесть
//    override fun onClick(view: View) {
//        TODO: данная оперция уже будет не нужна, если мы уберем родиетьский ConstraintLayout у ImageView, background можно задавать самой ImageView.
//        (view.parent as View).setBackgroundColor(Color.YELLOW)
//        actionsTextView.visibility = View.INVISIBLE
//        getAvailableDoor(doorImageViews.indexOf(view as ImageView))
//    }

    //    TODO: Naming
//    private fun startNewGame() {
    private fun startNewGame() {
        winDoor = Door.values()[random.nextInt(Door.values().size)]

        Door.values().forEach { updateDoor(it, true) }

        actionsTextView.setText(R.string.action_choose_the_door)
        newGameButton.visibility = View.INVISIBLE
        actionsTextView.visibility = View.VISIBLE
    }

//    private fun setDefaultImage() {
//        doorImageViews.forEach {
//            it.apply {
//                setImageResource(R.drawable.ic_close_door)
//                setBackgroundColor(Color.TRANSPARENT)
//                (parent as View).setBackgroundColor(Color.TRANSPARENT)
//                isClickable = true
//            }
//        }
//    }

    // TODO: бесполезный метод, каждый раз создается новый объект Random
//    private fun generateWinDoor() {
//        val COUNT_DOOR = 3  //  three doors
//        winNumberDoor = Random().nextInt(COUNT_DOOR)
//    }

    override fun onClick(view: View) = when (view.id) {
        newGameButton.id -> startNewGame()
        else -> chooseDoorWihId(view.id)
    }

    private fun chooseDoorWihId(id: Int) {
        val chosenDoor = Door.doorForViewWithId(id) ?: return
        val openedDoor = Door.values().first { it != winDoor && it != chosenDoor }

        this.chosenDoor = chosenDoor

        updateDoor(openedDoor, true, false)

        askToChangeChoice(chosenDoor, openedDoor)
    }

    private fun updateDoor(door: Door, isOpened: Boolean, isWiningDoor: Boolean = false) {
        doorImageViews[door.ordinal].apply {
            // TODO: Когда переделаешь layout, сдлеай несколько бэкграундов, что бы использовать их как фон, но с очертанием вокруг view (green red, для индикации).
            if (isOpened) {
                setImageResource(if (isWiningDoor) R.drawable.ic_door_car else R.drawable.ic_door_goat)
                setBackgroundColor(Color.GRAY)
            } else {
                setImageResource(R.drawable.ic_close_door)
                setBackgroundColor(Color.GRAY)
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

//    TODO: Очень сложная и не понятная логика, много кода повторяется.
//    TODO: Naming.
//    private fun getAvailableDoor(chosenNumberDoor: Int) {
//        var openDoor = Door.ONE
//        var chosenDoor = Door.ONE
//        var anotherDoor = Door.ONE
//
//        if (winNumberDoor == chosenNumberDoor) {
//            when (chosenNumberDoor) {
//                Door.ONE.ordinal -> {
//                    chosenDoor = Door.ONE
//                    openDoor = Door.TWO
//                    anotherDoor = Door.THREE
//                }
//                Door.TWO.ordinal -> {
//                    openDoor = Door.ONE
//                    anotherDoor = Door.THREE
//                    chosenDoor = Door.TWO
//                }
//                Door.THREE.ordinal -> {
//                    openDoor = Door.ONE
//                    anotherDoor = Door.TWO
//                    chosenDoor = Door.THREE
//                }
//            }
//        } else {
//            when (chosenNumberDoor) {
//                Door.ONE.ordinal -> {
//                    chosenDoor = Door.ONE
//                    if (winNumberDoor == Door.TWO.ordinal) {
//                        openDoor = Door.THREE
//                        anotherDoor = Door.TWO
//                    } else {
//                        openDoor = Door.TWO
//                        anotherDoor = Door.THREE
//                    }
//                }
//                Door.TWO.ordinal -> {
//                    chosenDoor = Door.TWO
//                    if (winNumberDoor == Door.ONE.ordinal) {
//                        openDoor = Door.THREE
//                        anotherDoor = Door.ONE
//                    } else {
//                        openDoor = Door.ONE
//                        anotherDoor = Door.THREE
//                    }
//                }
//                Door.THREE.ordinal -> {
//                    chosenDoor = Door.THREE
//                    if (winNumberDoor == Door.ONE.ordinal) {
//                        openDoor = Door.TWO
//                        anotherDoor = Door.ONE
//                    } else {
//                        openDoor = Door.ONE
//                        anotherDoor = Door.TWO
//                    }
//                }
//            }
//        }
////        Log.i("TAG", winNumberDoor.toString() + " " + openDoor + " " + anotherDoor)
//        doorImageViews[openDoor.ordinal].apply {
//            setImageResource(R.drawable.ic_door_goat)
//            setBackgroundColor(Color.GRAY)
//        }
//        showChangeChoiceAlertDialog(chosenDoor, openDoor, anotherDoor)
//    }

//    private fun showChangeChoiceAlertDialog(chosenDoor: Door, doorOpen: Door, anotherDoor: Door) {
//        AlertDialog.Builder(this@MainActivity, R.style.AppTheme_AlertDialog)
//                .setTitle(getString(R.string.action_door_number_is_open, getString(doorOpen.getTitleResId())))
//                .setMessage(getString(
//                        R.string.question_change_your_choice,
//                        getString(chosenDoor.getTitleResId()),
//                        getString(anotherDoor.getTitleResId())))
//                .setPositiveButton(R.string.yes, { _, _ ->
//                    (doorImageViews[chosenDoor.ordinal].parent as View).setBackgroundColor(Color.TRANSPARENT)
//                    checkForWin(anotherDoor.ordinal)
//                })
//                .setNegativeButton(R.string.no, { _, _ ->
//                    checkForWin(chosenDoor.ordinal)
//                })
//                .setCancelable(false)
//                .setOnDismissListener { newGameButton.visibility = View.VISIBLE }
//                .show()
//    }
//
//    private fun checkForWin(chosenIntDoor: Int) {
//        doorImageViews.forEach { it.isClickable = false }
//        if (winNumberDoor == chosenIntDoor) {
//            doorImageViews[winNumberDoor].apply {
//                setImageResource(R.drawable.ic_door_car)
//                setBackgroundColor(Color.GRAY)
//                (parent as View).setBackgroundColor(Color.GREEN)
//            }
//            Toast.makeText(this, "YOU WIN", Toast.LENGTH_SHORT).show()
//        } else {
//            doorImageViews[chosenIntDoor].apply {
//                setImageResource(R.drawable.ic_door_goat)
//                setBackgroundColor(Color.GRAY)
//                (parent as View).setBackgroundColor(Color.RED)
//
//            }
//            // TODO: Захардкоженный текст. Текст всегда нужно выносить в ресурсы.
//            Toast.makeText(this, "YOU LOSE", Toast.LENGTH_SHORT).show()
//            doorImageViews[winNumberDoor].apply {
//                setBackgroundColor(Color.GRAY)
//                setImageResource(R.drawable.ic_door_car)
//            }
//        }
//    }

    private fun showResults(chosenDoor: Door) {
        this.chosenDoor = chosenDoor

        Door.values().forEach {
            updateDoor(it, true, it == winDoor)
        }

        Toast.makeText(this,  if (chosenDoor == winDoor) R.string.you_win else R.string.you_lose, Toast.LENGTH_SHORT).show()
    }
}