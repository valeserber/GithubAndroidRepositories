package com.valeserber.githubchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.valeserber.githubchallenge.R
import com.valeserber.githubchallenge.databinding.FragmentGithubDetailBinding
import com.valeserber.githubchallenge.viewmodels.GithubDetailViewModel

class GithubDetailFragment : Fragment() {

    private lateinit var viewModel: GithubDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentGithubDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_github_detail,
            container,
            false)

        val application = requireNotNull(this.activity).application
        val arguments = GithubDetailFragmentArgs.fromBundle(arguments!!)

        viewModel =
            ViewModelProviders.of(this).get(GithubDetailViewModel::class.java)

        return binding.root
    }

}