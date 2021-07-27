package com.example.materyaldesig.picture

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BulletSpan
import android.text.style.ClickableSpan
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.load
import com.example.materyaldesig.MainActivity
import com.example.materyaldesig.R
import com.example.materyaldesig.databinding.FragmentMainBinding
import com.example.materyaldesig.databinding.FragmentMainStartBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentMainStartBinding? = null
    private val binding get() = _binding!!
//    private val bindingSh: BottomSheetLayoutBinding? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    /* Этот метод уже deprecation
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData().observe(
            viewLifecycleOwner,
            Observer<PictureOfTheDayData> { renderData(it) }
        )
    }
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(
            viewLifecycleOwner,
            Observer<PictureOfTheDayData> { renderData(it) }
        )
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedis.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        setBottomAppBar(view)

        activity?.let {
            binding.includeLayoutText.textView.typeface = Typeface.createFromAsset(it.assets, "DroidSerif-WmoY.ttf")
        }

        val subString = "ссылка"
        val originalString = requireContext().getString(R.string.some_big_text)

        val spannable = SpannableString(originalString)
        spannable.setSpan(
            BulletSpan(8, ContextCompat.getColor(requireContext(), R.color.colorAccent)),
            18,
            25,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(requireContext(), "You Click me!", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        spannable.setSpan(
            clickableSpan,
            Regex(subString).matchEntire(originalString)?.range?.start!!,
            Regex(subString).matchEntire(originalString)?.range?.endInclusive!!,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        with(binding.includeLayoutText.textView){
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //Ссылка пустая
                    toast("Link is empty / Ссылка пустая")
                } else {
                    //Отображаем фото
                    toast("Show photo / Отобразить фото")
                    /* Здесь у меня ошибка на image_view. Переделал на binding.imageView.load(url)
                        image_view.load(url) {
                            lifecycle(this@PictureOfTheDayFragment)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                        }
                    */
                    binding.imageView.load(url) {
                        placeholder(R.drawable.ic_no_photo_vector)
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                    }
                }
                serverResponseData.explanation?.let {
                    binding.includeLayoutText.textView.text = it
                }
            }
            is PictureOfTheDayData.Loading -> {
                //Отобразить загрузку
                toast("Loading picture / Загрузка картинки")
            }
            is PictureOfTheDayData.Error -> {
                //Отобразить ошибку
                toast(data.error.message)
            }
        }
    }

    private fun toast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favorite")
            R.id.app_bar_settings -> toast("Setting")
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment()
                        .show(
                            it.supportFragmentManager,
                            "tag"
                        )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(
            view.findViewById(R.id.bottom_app_bar)
        )
        setHasOptionsMenu(true)

        with(binding) {
            fab.setOnClickListener {
                if (isMain) {
                    isMain = false
                    bottomAppBar.navigationIcon = null
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    fab.setImageDrawable(ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    ))
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
                } else {
                    isMain = true
                    bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    fab.setImageDrawable(ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    ))
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                }
            }
        }
    }

    //Такой метод - onOptionsItemSelected - выделяется и его невозможно переопределить.
//    onOptionsItemSelected

    companion object {
        private var isMain = true
    }
}