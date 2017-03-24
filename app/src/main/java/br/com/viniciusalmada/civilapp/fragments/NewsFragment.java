package br.com.viniciusalmada.civilapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.viniciusalmada.civilapp.LoginActivity;
import br.com.viniciusalmada.civilapp.R;
import br.com.viniciusalmada.civilapp.adapters.NewsImagesSmallerAdapter;
import br.com.viniciusalmada.civilapp.adapters.NewsNonImageAdapter;
import br.com.viniciusalmada.civilapp.domains.News;
import br.com.viniciusalmada.civilapp.extras.HandlerJsoup;
import br.com.viniciusalmada.civilapp.utils.AlertLinkExternal;

/**
 * Created by vinicius-almada on 19/03/17.
 */

public class NewsFragment extends Fragment implements BaseSliderView.OnSliderClickListener, View.OnClickListener {
    public static final String TAG = "NewsFragment";
    private static final String URL_IFMA = "http://portal.ifma.edu.br/";
    private Document doc;
    private SliderLayout slNewsDynamics;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initJsoup();

        setHasOptionsMenu(true);
    }

    private void initJsoup() {
        HandlerJsoup handlerJsoup = new HandlerJsoup();
        handlerJsoup.execute(URL_IFMA);
        try {
            doc = handlerJsoup.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        slNewsDynamics.startAutoCycle();

        ((NestedScrollView) rootView.findViewById(R.id.root_nested)).smoothScrollTo(0, 0);
        ((NestedScrollView) rootView.findViewById(R.id.root_nested)).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG, "onScrollChange: " + scrollX + "\t" + scrollY);
            }
        });
    }

    @Override
    public void onStop() {
        if (slNewsDynamics != null)
            slNewsDynamics.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                initJsoup();
                initViews();
                return true;
            case R.id.action_signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        slNewsDynamics = (SliderLayout) rootView.findViewById(R.id.slider_news_dynamics);
        ImageView ivNewsFeatured = (ImageView) rootView.findViewById(R.id.iv_news_featured);
        TextView tvNewsFeatured = (TextView) rootView.findViewById(R.id.tv_title_news_featured);
        CardView cvNewsFeatured = (CardView) rootView.findViewById(R.id.cv_news_featured);
        RecyclerView rvNewsNonImage = (RecyclerView) rootView.findViewById(R.id.rv_news_non_image);
        RecyclerView rvNewsImageSmaller = (RecyclerView) rootView.findViewById(R.id.rv_news_image_smaller);

        initNewsDynamics(slNewsDynamics);
        initNewsFeatured(ivNewsFeatured, tvNewsFeatured, cvNewsFeatured);
        initNewsNonImage(rvNewsNonImage);
        initNewsImagesSmaller(rvNewsImageSmaller);
    }

    private void initNewsImagesSmaller(RecyclerView rvNewsImageSmaller) {
        List<News.NSmallImages> nSmallImages = HandlerJsoup.getNewsSmallImages(doc);
        NewsImagesSmallerAdapter adapter = new NewsImagesSmallerAdapter(nSmallImages, getActivity());

        rvNewsImageSmaller.setAdapter(adapter);
        rvNewsImageSmaller.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvNewsImageSmaller.setLayoutManager(llm);
    }

    private void initNewsNonImage(RecyclerView rvNewsNonImage) {
        List<News.NNonImage> nonImageList = HandlerJsoup.getNewsNonImage(doc);
        NewsNonImageAdapter adapter = new NewsNonImageAdapter(nonImageList, getActivity());

        rvNewsNonImage.setAdapter(adapter);
        rvNewsNonImage.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvNewsNonImage.setLayoutManager(llm);
    }

    private void initNewsFeatured(ImageView ivNewsFeatured, TextView tvNewsFeatured, CardView cardView) {
        News.NFeatured nFeatured = HandlerJsoup.getNewsFeatured(doc);

        Picasso.with(getActivity()).load(nFeatured.getLinkImg()).into(ivNewsFeatured);
        tvNewsFeatured.setText(nFeatured.getText());

        ivNewsFeatured.setOnClickListener(this);
        tvNewsFeatured.setOnClickListener(this);
        cardView.setOnClickListener(this);
    }

    private void initNewsDynamics(SliderLayout slNewsDynamics) {
        List<News.NDynamic> nDynamicsList = HandlerJsoup.getNewsDynamics(doc);
        for (News.NDynamic nd : nDynamicsList) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
            defaultSliderView.image(nd.getLinkImg())
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);

            Bundle bundle = new Bundle();
            defaultSliderView.bundle(bundle);
            defaultSliderView.getBundle().putString("link", nd.getLink());

            slNewsDynamics.addSlider(defaultSliderView);
        }

        slNewsDynamics.setPresetTransformer(SliderLayout.Transformer.Default);
        slNewsDynamics.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        slNewsDynamics.setCustomAnimation(new DescriptionAnimation());
        slNewsDynamics.setDuration(3000);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        String url = slider.getBundle().getString("link");
        AlertLinkExternal.openAlertDialog(url, getActivity());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_news_featured || v.getId() == R.id.tv_title_news_featured || v.getId() == R.id.cv_news_featured) {
            String url = HandlerJsoup.getNewsFeatured(doc).getLink();
            AlertLinkExternal.openAlertDialog(url, getActivity());
        }
    }
}
