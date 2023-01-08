package com.example.andrushchenkopractice_recyclerview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andrushchenkopractice_recyclerview.UserActionListener
import com.example.andrushchenkopractice_recyclerview.UsersAdapter
import com.example.andrushchenkopractice_recyclerview.databinding.FragmentUsersListBinding
import com.example.andrushchenkopractice_recyclerview.model.User

class UsersListFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UsersAdapter

    private val viewModel: UsersListViewModel by viewModels {factory()}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                navigator().showDetails(user)
            }
        })

        viewModel.users.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.users = it
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}