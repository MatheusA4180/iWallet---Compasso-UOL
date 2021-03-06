package com.example.iwallet.features.resume.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.iwallet.databinding.FragmentNewsBinding
import com.example.iwallet.features.resume.adapter.ViewPagerNewsAdapter.Companion.POSITION_VIEW_PAGER_NEWS
import com.example.iwallet.features.resume.viewmodel.NewsViewModel
import com.example.iwallet.utils.model.resume.NewsEntity
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding: FragmentNewsBinding get() = _binding!!
    private val viewModel: NewsViewModel by viewModel()
    private var listNews: List<NewsEntity> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val position = requireArguments().getInt(POSITION_VIEW_PAGER_NEWS)

        viewModel.requestNews()

        viewModel.listNews.observe(viewLifecycleOwner, {
            Picasso.with(binding.imageNew.context)
                .load(it[position].urlToImage)
                .into(binding.imageNew)
            binding.titleNews.text = it[position].title
            listNews = it
        })

        binding.cardNews.setOnClickListener {
            if (listNews.isNotEmpty()) {
                openWebPage(listNews[position].url)
            }
        }

        viewModel.responseErro.observe(viewLifecycleOwner, {
            AlertDialog.Builder(requireContext()).setMessage(it).show()
        })

        viewModel.showLoading.observe(viewLifecycleOwner, {
            binding.progressCircular.isVisible = it
        })

    }

    fun openWebPage(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
