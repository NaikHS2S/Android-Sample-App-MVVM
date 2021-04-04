package com.sample.app.android.ui.home.view.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sample.app.android.R
import com.sample.app.android.databinding.FragmentMyProfileBinding
import com.sample.app.android.ui.home.view.model.ProfileViewModel


class MyProfile : Fragment() {

    private val profileViewModel: ProfileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: FragmentMyProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        profileViewModel.navController = findNavController()
        binding.profileViewModel = profileViewModel
        return binding.root
    }
}
