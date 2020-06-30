package com.laioffer.tinnews.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.FragmentSearchBinding;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.repository.NewsRepository;
import com.laioffer.tinnews.repository.NewsViewModelFactory;

import static android.widget.Toast.LENGTH_SHORT;

public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchNewsAdapter newsAdapter = new SearchNewsAdapter();
        newsAdapter.setLikeListener(new SearchNewsAdapter.LikeListener() {
            @Override
            public void onLike(Article article) {
                viewModel.setFavoriteArticleInput(article);
            }

            @Override
            public void onClick(Article article) {
                // TODO
//                NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.action_title_search_to_detail);
                SearchFragmentDirections.ActionTitleSearchToDetail actionTitleSearchToDetail = SearchFragmentDirections.actionTitleSearchToDetail();
                actionTitleSearchToDetail.setArticle(article);
                NavHostFragment.findNavController(SearchFragment.this).navigate(actionTitleSearchToDetail);

            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        gridLayoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return position == 0 ? 2 : 1;
                    }
                });

        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setAdapter(newsAdapter);

        binding.searchView.setOnEditorActionListener(
                (v, actionId, event) -> {
                    String searchText = binding.searchView.getText().toString();
                    if (actionId == EditorInfo.IME_ACTION_DONE && !searchText.isEmpty()) {
                        viewModel.setSearchInput(searchText);
                        return true;
                    } else {
                        return false;
                    }
                });

        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository)) // requireActivity() replace this
                .get(SearchViewModel.class);

        viewModel
                .searchNews()
                .observe(
                        getViewLifecycleOwner(),
                        newsResponse -> {
                            if (newsResponse != null) {
                                Log.d("SearchFragment", newsResponse.toString());
                                newsAdapter.setArticles(newsResponse.articles);
                            }
                        });

        viewModel.onFavorite().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                Toast.makeText(requireActivity(), "Success", LENGTH_SHORT).show();
                newsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireActivity(), "You might have liked before", LENGTH_SHORT).show();
            }
        });

//        viewModel.setSearchInput("Covid-19");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.onCancel();
    }
}