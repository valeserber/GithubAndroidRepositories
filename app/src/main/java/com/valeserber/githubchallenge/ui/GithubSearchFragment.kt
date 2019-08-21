package com.valeserber.githubchallenge.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.valeserber.githubchallenge.R
import com.valeserber.githubchallenge.databinding.FragmentGithubSearchBinding
import com.valeserber.githubchallenge.domain.NetworkStatus
import com.valeserber.githubchallenge.util.Injection
import com.valeserber.githubchallenge.viewmodels.GithubSearchViewModel

class GithubSearchFragment : Fragment() {

    private lateinit var viewModel: GithubSearchViewModel

    private var searchRepoAdapter: SearchRepositoryAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentGithubSearchBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_github_search,
            container,
            false
        )

        //TODO check requireContext
        viewModel = ViewModelProviders.of(this, Injection.provideGithubSearchViewModelFactory(this.requireContext()))
            .get(GithubSearchViewModel::class.java)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        searchRepoAdapter = SearchRepositoryAdapter(SearchRepositoryListener { repositoryId ->
            viewModel.onSearchRepositoryClicked(repositoryId)
        })
        binding.searchRecyclerView.adapter = searchRepoAdapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.repositories.observe(viewLifecycleOwner, Observer {
            it?.let {
                searchRepoAdapter?.submitList(it)
            }

        })

        viewModel.navigateToRepositoryDetail.observe(viewLifecycleOwner, Observer { repositoryId ->

            repositoryId?.let {
                this.findNavController().navigate(GithubSearchFragmentDirections.actionToDetailFragment(repositoryId))
                viewModel.onRepositoryDetailNavigated()
            }

        })

        viewModel.networkStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    //TODO change to UI update
                    NetworkStatus.ERROR -> Log.i("GithubSearchRepos", "in fragment error")
                    NetworkStatus.DONE -> Log.i("GithubSearchRepos", "in fragment done")
                    NetworkStatus.LOADING -> Log.i("GithubSearchRepos", "in fragment loading")
                }
            }
        })
    }
}