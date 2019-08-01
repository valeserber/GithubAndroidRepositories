package com.valeserber.githubchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.valeserber.githubchallenge.R
import com.valeserber.githubchallenge.databinding.FragmentGithubDetailBinding

class GithubDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentGithubDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_github_detail,
            container,
            false)

        return binding.root
    }

}