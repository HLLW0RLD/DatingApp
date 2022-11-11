package com.example.datingapp.view

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.datingapp.databinding.FragmentMainBinding
import com.example.datingapp.model.data.User
import com.example.datingapp.view.adapter.LoaderStateAdapter
import com.example.datingapp.view.adapter.MainAdapter
import com.example.datingapp.viewmodel.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Observer


class MainFragment : Fragment() {

    companion object {
        const val DIALOG_EXIT = 1
        fun newInstance() = MainFragment()
    }

    val viewModel: MainViewModel by viewModels<MainViewModel>()
    var flip = true
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!
    private val adapter = MainAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            imageRecycler.layoutManager =
                GridLayoutManager(context, 2)
            imageRecycler.adapter =
                adapter.withLoadStateHeaderAndFooter(
                header = LoaderStateAdapter(),
                footer = LoaderStateAdapter()
            )
            adapter.apply {
                addLoadStateListener { state ->
                    imageRecycler.isVisible = state.refresh != LoadState.Loading
                    mainLoadingLayout.isVisible = state.refresh == LoadState.Loading
                }
                setOnItemClickListener {
                    val animation: ObjectAnimator =
                        ObjectAnimator.ofFloat(
                            it,
                            View.ROTATION_Y,
                            0.0f, 180f
                        )
                    animation.apply {
                        setDuration(400)
                        setInterpolator(AccelerateDecelerateInterpolator());
                        start()
                    }
                }
                setOnIdClickListener {
                    val dialogFragment = DialogFragment(it)
                    val manager = activity?.supportFragmentManager
                    val transaction: FragmentTransaction = manager!!.beginTransaction()
                    dialogFragment.show(transaction, "dialog")
                }
            }
            swipeRefreshLayout.apply {
                setColorSchemeColors(
                    Color.RED,
                    Color.GREEN,
                    Color.BLUE,
                    Color.CYAN
                )
                setOnRefreshListener {
                    viewModel.data.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
                    viewModel.getUsers()
                    isRefreshing = false
                }
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
        viewModel.getUsers()
    }
}