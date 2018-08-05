package com.ratajczykdev.inventoryapp.about


import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.transition.Explode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import com.ratajczykdev.inventoryapp.R
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Presents about app content
 */
class AboutFragment : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        //  must be called here to animate Fragment's UI elements
        setExplodeEnterTransition()
    }

    private fun setExplodeEnterTransition() {
        val explode = Explode()
        val EXPLODE_ANIMATION_DURATION_IN_MS = 400L
        explode.duration = EXPLODE_ANIMATION_DURATION_IN_MS
        explode.interpolator = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in)

        enterTransition = explode
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  every view from Fragment can be accessed in [onViewCreated]
        //  https://stackoverflow.com/a/34543172
        setEasterEgg()
    }

    private fun setEasterEgg() {
        val isLongerEasterEgg = getEasterEggPreference()
        val rotationInDegrees: Int
        val rotationAnimationDurationInMs: Long
        when (isLongerEasterEgg) {
            true -> {
                rotationInDegrees = 3600
                rotationAnimationDurationInMs = 50000
            }
            false -> {
                rotationInDegrees = 360
                rotationAnimationDurationInMs = 5000
            }
        }

        store_imageview.setOnLongClickListener {
            store_imageview.animate()
                    .rotation(store_imageview.rotation + rotationInDegrees)
                    .setInterpolator(AnimationUtils.loadInterpolator(context, android.R.interpolator.cycle))
                    .setDuration(rotationAnimationDurationInMs)
                    .start()
            true
        }
    }

    private fun getEasterEggPreference(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val PREFERENCE_KEY = getString(R.string.preference_longer_easter_egg)
        val DEFAULT_VALUE = false
        return sharedPreferences.getBoolean(PREFERENCE_KEY, DEFAULT_VALUE)
    }
}
