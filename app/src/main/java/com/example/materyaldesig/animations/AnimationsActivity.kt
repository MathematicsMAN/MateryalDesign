package com.example.materyaldesig.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.example.materyaldesig.R
import com.example.materyaldesig.databinding.*

class AnimationsActivity : AppCompatActivity() {
//    private var _binding: ActivityAnimationsBinding? = null
//    private var _binding: ActivityAnimationsExplodeBinding? = null
//    private var _binding: ActivityAnimationsEnlargeBinding? = null
//    private var _binding: ActivityAnimationsPathTransitionsBinding? = null
    private var _binding: ActivityAnimationsFabBinding? = null
    private val binding get() = _binding!!

    private var textIsVisible = false
    private var isExpanded = false
    private var toRightAnimation = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_animations)
//        setContentView(R.layout.activity_animations_explode)
//        setContentView(R.layout.activity_animations_enlarge)
//        setContentView(R.layout.activity_animations_path_transitions)
        setContentView(R.layout.activity_animations_fab)
//        binding.recyclerView.adapter = Adapter()
        setFAB()

        binding.scrollView.setOnScrollChangeListener {_, _, _, _, _ ->
            binding.header.isSelected = binding.scrollView.canScrollVertically(-1)
        }

        /*
        binding.button.setOnClickListener {
            val changeBounds = ChangeBounds()
            changeBounds.setPathMotion(ArcMotion())
            changeBounds.duration = 500
            TransitionManager.beginDelayedTransition(
                binding.transitionsContainer,
                changeBounds
            )
            toRightAnimation = !toRightAnimation
            val params = binding.button.layoutParams as FrameLayout.LayoutParams
            params.gravity = if (toRightAnimation) Gravity.END or Gravity.BOTTOM
                                else Gravity.START or Gravity.TOP
            binding.button.layoutParams = params
        }
         */
        /*
        binding.imageView.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                binding.container,
                TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )
            val params: ViewGroup.LayoutParams = binding.imageView.layoutParams
            params.height = if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT
                                else ViewGroup.LayoutParams.WRAP_CONTENT
            binding.imageView.layoutParams = params
            binding.imageView.scaleType = if (isExpanded) ImageView.ScaleType.CENTER_CROP
                                            else ImageView.ScaleType.FIT_CENTER
        }
         */

/*        binding.button.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                binding.transitionsContainer
            )
            textIsVisible = !textIsVisible
            binding.text.visibility = if (textIsVisible) View.VISIBLE else View.GONE
        }
 */
    }

    private fun setFAB() {
        setInitialState()
        binding.fab.setOnClickListener {
            if (isExpanded) {
                collapseFab()
            } else {
                expandFAB()
            }
        }
    }

    private fun setInitialState() {
        binding.transparentBackground.apply {
            alpha = 0f
        }
        binding.optionOneContainer.apply {
            alpha = 0f
            isClickable = false
        }
        binding.optionTwoContainer.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun collapseFab() {
        isExpanded = false
        ObjectAnimator.ofFloat(
            binding.plusImageview,
            "rotation",
            0f,
            -180f
        ).start()
        ObjectAnimator.ofFloat(
            binding.optionOneContainer,
            "translationY",
            0f
        ).start()
        ObjectAnimator.ofFloat(
            binding.optionTwoContainer,
            "translationY",
            0f
        ).start()

        binding.optionOneContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionOneContainer.isClickable = false
                    binding.optionOneContainer.setOnClickListener(null)
                }
            })
        binding.optionTwoContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionTwoContainer.isClickable = false
                    binding.optionTwoContainer.setOnClickListener(null)
                }
            })
        binding.transparentBackground.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    binding.transparentBackground.isClickable = false
                }
            })
    }

    private fun expandFAB() {
        isExpanded = true
        ObjectAnimator.ofFloat(
            binding.plusImageview,
            "rotation",
            0f,
            225f
        ).start()
        ObjectAnimator.ofFloat(
            binding.optionOneContainer,
            "translationY",
            -250f
        ).start()
        ObjectAnimator.ofFloat(
            binding.optionTwoContainer,
            "translationY",
            -130f
        ).start()
        binding.optionOneContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionOneContainer.isClickable = true
                    binding.optionOneContainer.setOnClickListener {
                        Toast.makeText(
                            this@AnimationsActivity,
                            "Option 1",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        binding.optionTwoContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionTwoContainer.isClickable = true
                    binding.optionTwoContainer.setOnClickListener {
                        Toast.makeText(
                            this@AnimationsActivity,
                            "Option 2",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        binding.transparentBackground.animate()
            .alpha(0.9f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    binding.transparentBackground.isClickable = true
                }
            })
    }

    /*
        private fun explode(clickedView: View) {
            val viewRect = Rect()
            clickedView.getGlobalVisibleRect(viewRect)
            val explode = Explode()
            explode.epicenterCallback = object : Transition.EpicenterCallback() {
                override fun onGetEpicenter(transition: Transition): Rect {
                    return viewRect
                }
            }
    //        explode.duration = 1000
            explode.excludeTarget(clickedView, true)
            val set = TransitionSet()
                .addTransition(explode)
                .addTransition(Fade().addTarget(clickedView))
                .addListener(object : TransitionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition) {
                        transition.removeListener(this)
                        onBackPressed()
                    }
                })
    //        TransitionManager.beginDelayedTransition(binding.recyclerView, explode)
            TransitionManager.beginDelayedTransition(binding.recyclerView, set)
            binding.recyclerView.adapter = null
        }

        inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.activity_animations_explode_recycle_view_item,
                        parent,
                        false
                    ) as View
                )
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.itemView.setOnClickListener {
                    explode(it)
                }
            }

            override fun getItemCount(): Int {
                return 16
            }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}