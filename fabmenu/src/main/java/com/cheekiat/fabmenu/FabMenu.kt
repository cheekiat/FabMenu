package com.cheekiat.fabmenu

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.cheekiat.fabmenu.listener.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.layout_fabmenu.view.*
import kotlin.random.Random

/**
 * TODO: document your custom view class.
 */
class FabMenu : FrameLayout {


    var isShow = false
    var isClickAble = true
    var arrayItems = arrayListOf<FloatingActionButton>()
    var speed = 150L
    var space = 0
    lateinit var root: View

    var expandIcon: Drawable? = null
    var collapseIcon: Drawable? = null

     var fabBackgroundColor: Int = 0

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.FabMenu, defStyle, 0
        )

        space = a.getDimension(
            R.styleable.FabMenu_space,
            20f
        ).toInt()

        if (a.hasValue(R.styleable.FabMenu_expandIcon)) {
            expandIcon = a.getDrawable(
                R.styleable.FabMenu_expandIcon
            )
            expandIcon?.callback = this
        }

        if (a.hasValue(R.styleable.FabMenu_collapseIcon)) {
            collapseIcon = a.getDrawable(
                R.styleable.FabMenu_collapseIcon
            )
            collapseIcon?.callback = this
        }

        fabBackgroundColor = a.getColor(R.styleable.FabMenu_fabBackgroundColor, Color.BLUE);

        a.recycle()

        root = LayoutInflater.from(context).inflate(R.layout.layout_fabmenu, this, true)

    }

    fun setDuration(duration: Long) {
        speed = duration
    }

    fun addItem(@DrawableRes resId: Int, backgroundColor: Int) {
        var floatingActionButton = FloatingActionButton(context)
        floatingActionButton.id = 100001 + Random.nextInt(1000)
        root.constraintLayout.addView(floatingActionButton, 0)

        floatingActionButton.supportBackgroundTintList = ContextCompat.getColorStateList(context, backgroundColor)
        floatingActionButton.size = FloatingActionButton.SIZE_MINI;
        floatingActionButton.setImageResource(resId)

        val set = ConstraintSet()
        set.clone(constraintLayout)
        set.connect(floatingActionButton.getId(), ConstraintSet.TOP, mainFab.id, ConstraintSet.TOP, 0)
//            set.connect(floatingActionButton.getId(), ConstraintSet.BOTTOM, main.id, ConstraintSet.BOTTOM, 0)
        set.connect(floatingActionButton.getId(), ConstraintSet.LEFT, mainFab.id, ConstraintSet.LEFT, 0)
        set.connect(floatingActionButton.getId(), ConstraintSet.RIGHT, mainFab.id, ConstraintSet.RIGHT, 0)

        set.applyTo(constraintLayout)
        arrayItems.add(floatingActionButton)


        initMenu()

        root.mainFab.setOnClickListener {

            if (!isClickAble) {
                return@setOnClickListener
            }



            updateMainIcon()

            if (!isShow) {

                showMenu()
            } else {

                hideMenu()
            }
        }
    }

    fun initMenu() {

        updateMainIcon()

        var itemSize = resources.getDimensionPixelSize(com.google.android.material.R.dimen.design_fab_size_mini);

        itemSize += space

        val bouncer = AnimatorSet()
        var set: AnimatorSet.Builder? = null
        for (i in 0 until arrayItems.size) {

            if (set == null) {
                set = bouncer.play(
                    ObjectAnimator.ofFloat(
                        arrayItems[i],
                        "translationY",
                        -(itemSize.toFloat() * (i + 1))
                    ).apply {
                        duration = 0
                    })
            } else {
                set?.after(
                    ObjectAnimator.ofFloat(
                        arrayItems[i],
                        "translationY",
                        -(itemSize.toFloat() * (i + 1))
                    ).apply {
                        duration = 0
                    })
            }
        }
        bouncer.start()

        var animatorSet = AnimatorSet()
        var animationBuilder: AnimatorSet.Builder? = null
        for (i in 0 until arrayItems.size) {
            var itemAnimation = AnimatorSet()

            itemAnimation.play(ObjectAnimator.ofFloat(arrayItems[(arrayItems.size - 1) - i], "alpha", 1f, 0f).apply {
                duration = 0
            }).with(ObjectAnimator.ofFloat(arrayItems[(arrayItems.size - 1) - i], "scaleX", 0.0f).apply {
                duration = 0
            }).with(ObjectAnimator.ofFloat(arrayItems[(arrayItems.size - 1) - i], "scaleY", 0.0f).apply {
                duration = 0
            })

            if (animationBuilder == null) {
                animationBuilder = animatorSet.play(itemAnimation)
            } else {
                animationBuilder.with(itemAnimation)
            }
        }
        animatorSet.start()

//        root.mainFab.setBackgroundResource(fabBackgroundColor)

        var colorStateList = ColorStateList.valueOf(fabBackgroundColor);
        root.mainFab.supportBackgroundTintList = colorStateList
    }

    fun hideMenu() {
        isShow = false
        var animatorSet = AnimatorSet()
        var animationBuilder: AnimatorSet.Builder? = null
        for (i in 0 until arrayItems.size) {
            var itemAnimation = AnimatorSet()

            if (i != 0) {
                itemAnimation.startDelay = (speed / 5) * i
            }
            itemAnimation.play(ObjectAnimator.ofFloat(arrayItems[(arrayItems.size - 1) - i], "alpha", 1f, 0f).apply {
                duration = speed
            }).with(ObjectAnimator.ofFloat(arrayItems[(arrayItems.size - 1) - i], "scaleX", 0.0f).apply {
                duration = speed
            }).with(ObjectAnimator.ofFloat(arrayItems[(arrayItems.size - 1) - i], "scaleY", 0.0f).apply {
                duration = speed
            })

            if (animationBuilder == null) {
                animationBuilder = animatorSet.play(itemAnimation)
            } else {
                animationBuilder.with(itemAnimation)
            }
        }
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                isClickAble = true
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                isClickAble = false
            }

        })
        animatorSet.start()

    }

    fun showMenu() {
        isShow = true

        var animatorSet = AnimatorSet()
        var animationBuilder: AnimatorSet.Builder? = null
        for (i in 0 until arrayItems.size) {
            var itemAnimation = AnimatorSet()

            if (i != 0) {
                itemAnimation.startDelay = (speed / 5) * i
            }
            itemAnimation.play(ObjectAnimator.ofFloat(arrayItems[i], "alpha", 0f, 1f).apply {
                duration = speed
            }).with(ObjectAnimator.ofFloat(arrayItems[i], "scaleX", 1.0f).apply {
                duration = speed
            }).with(ObjectAnimator.ofFloat(arrayItems[i], "scaleY", 1.0f).apply {
                duration = speed
            })

            if (animationBuilder == null) {
                animationBuilder = animatorSet.play(itemAnimation)
            } else {
                animationBuilder.with(itemAnimation)
            }
        }
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                isClickAble = true
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                isClickAble = false
            }

        })
        animatorSet.start()

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        for (i in 0 until arrayItems.size) {
            arrayItems[(arrayItems.size - 1) - i].setOnClickListener {

                hideMenu()

                listener?.onItemClick(i)
            }
        }
    }

    fun getSubMenu(position : Int): FloatingActionButton {
       return arrayItems.get(position)
    }

    fun getMainMenu(): FloatingActionButton? {

        return root.mainFab
    }

    fun updateMainIcon() {
        if (isShow) {
            root.mainFab.setImageDrawable(expandIcon)
        } else {
            root.mainFab.setImageDrawable(collapseIcon)
        }
    }


//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//
//        val contentHeight = height - paddingTop - paddingBottom
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        setMeasuredDimension(2000,2000)
//    }
}
