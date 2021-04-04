package com.sample.app.android.ui.home.view.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sample.app.android.R
import com.sample.app.android.databinding.FragmentTitleBinding
import com.sample.app.android.ui.home.view.model.HomeFragmentModel

class Title : Fragment() {

    private val viewModel: HomeFragmentModel by lazy { ViewModelProvider(this).get(HomeFragmentModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: FragmentTitleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.navController = findNavController()
        binding.homeViewModel = this.viewModel
        return binding.root

    }
}
