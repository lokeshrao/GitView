package com.example.gitview.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitview.databinding.FragmentUserDetailBinding
import com.example.gitview.utils.ImageLoader
import kotlinx.coroutines.launch

class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels()
    private val args: UserDetailFragmentArgs by navArgs()

    private lateinit var repoAdapter: UserRepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()

        viewModel.loadUserAndRepos(args.username)
    }

    private fun setupUI() = with(binding) {
        buttonBack.setOnClickListener { findNavController().navigateUp() }

        repoAdapter = UserRepoAdapter()
        recyclerRepos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = repoAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadUserAndRepos(args.username)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() = with(viewModel) {
        user.observe(viewLifecycleOwner) { user ->
            user?.let {
                with(binding) {
                    textUsername.text = it.login
                    textFollowers.text = "${it.followers} Followers"
                    textFollowing.text = "${it.following} Following"
                    ImageLoader.loadImage(it.avatarUrl, imageAvatar)
                }
            }
        }

        repos.observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                repoAdapter.submitData(pagingData)
            }
        }

        loading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
