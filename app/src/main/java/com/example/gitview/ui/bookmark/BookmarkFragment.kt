package com.example.gitview.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitview.data.models.SearchUser
import com.example.gitview.databinding.FragmentBookmarkBinding
import com.example.gitview.utils.BookmarkPrefs

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarkPrefs: BookmarkPrefs
    private lateinit var adapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarkPrefs = BookmarkPrefs(requireContext())

        adapter = BookmarkAdapter(bookmarkPrefs) { username ->
            val action =
                BookmarkFragmentDirections.actionBookmarkFragmentToUserDetailFragment(username)
            findNavController().navigate(action)
        }

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BookmarkFragment.adapter
        }

        loadBookmarkedUsers()
    }

    private fun loadBookmarkedUsers() {
        val users = bookmarkPrefs.getAllBookmarks()
            .map { (username, avatarUrl) -> SearchUser(username, avatarUrl = avatarUrl) }

        with(binding) {
            textViewNoBookmarks.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(users)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
