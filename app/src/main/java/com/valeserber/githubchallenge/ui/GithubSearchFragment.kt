package com.valeserber.githubchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.valeserber.githubchallenge.R
import com.valeserber.githubchallenge.databinding.FragmentGithubSearchBinding
import com.valeserber.githubchallenge.domain.Repository
import com.valeserber.githubchallenge.viewmodels.GithubSearchViewModel

class GithubSearchFragment : Fragment() {


//    private val viewModel: GithubSearchViewModel by lazy {
//        ViewModelProviders.of(this).get(GithubSearchViewModel::class.java)
//    }

    private lateinit var viewModel: GithubSearchViewModel

    private var searchRepoAdapter: SearchRepositoryAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentGithubSearchBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_github_search,
            container,
            false
        )

        viewModel =
            ViewModelProviders.of(this).get(GithubSearchViewModel::class.java)

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
    }
}