package com.example.gitview.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitview.databinding.FragmentSearchBinding
import com.example.gitview.utils.BookmarkPrefs
import com.example.gitview.utils.onTextChangedDebounce
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SearchUserAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var bookmarkPrefs: BookmarkPrefs

    private var currentQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        bookmarkPrefs = BookmarkPrefs(requireContext())

        adapter = SearchUserAdapter(bookmarkPrefs) { username ->
            val action = SearchFragmentDirections.actionSearchFragmentToUserDetailFragment(username)
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }

        binding.editTextSearch.onTextChangedDebounce(viewLifecycleOwner.lifecycleScope) { query ->
            currentQuery = query.trim()
            binding.swipeRefreshLayout.isEnabled = currentQuery.isNotEmpty()
            viewModel.searchUsers(currentQuery)
        }

        viewModel.usersFlow.observe(viewLifecycleOwner) { pagingData ->
            if (binding.editTextSearch.text.isNullOrBlank()) {
                adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
                binding.textViewNoData.visibility = View.VISIBLE
            } else {
                adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (currentQuery.isNotEmpty()) adapter.refresh()
            else binding.swipeRefreshLayout.isRefreshing = false
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadStates ->
                val refreshState = loadStates.refresh
                binding.swipeRefreshLayout.isRefreshing = refreshState is LoadState.Loading

                if (refreshState is LoadState.Error) {
                    Toast.makeText(
                        requireContext(),
                        refreshState.error.localizedMessage ?: "Unknown error",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.textViewNoData.visibility =
                    if (refreshState is LoadState.NotLoading && adapter.itemCount == 0) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
