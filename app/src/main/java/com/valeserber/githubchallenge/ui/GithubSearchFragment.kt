package com.valeserber.githubchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.valeserber.githubchallenge.R
import com.valeserber.githubchallenge.databinding.FragmentGithubSearchBinding
import com.valeserber.githubchallenge.viewmodels.GithubSearchViewModel

class GithubSearchFragment : Fragment() {


    private val viewModel: GithubSearchViewModel by lazy {
        ViewModelProviders.of(this).get(GithubSearchViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentGithubSearchBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_github_search,
            container,
            false)

        return binding.root
    }
}