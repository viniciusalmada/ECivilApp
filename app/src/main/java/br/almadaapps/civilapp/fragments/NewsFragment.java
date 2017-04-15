package br.almadaapps.civilapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.NoSuchElementException;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.adapters.NewsImagesSmallerAdapter;
import br.almadaapps.civilapp.adapters.NewsNonImageAdapter;
import br.almadaapps.civilapp.domains.News;
import br.almadaapps.civilapp.interfaces.HandlerDownloadImpl;
import br.almadaapps.civilapp.utils.AlertLinkExternal;
import br.almadaapps.civilapp.utils.GeneralMethods;
import br.almadaapps.civilapp.utils.HandlerJsoup;

/**
 * Created by vinicius-almada on 19/03/17.
 */

public class NewsFragment extends Fragment implements BaseSliderView.OnSliderClickListener, View.OnClickListener, HandlerDownloadImpl {
    public static final String TAG = "NewsFragment";
    private static final String URL_IFMA = "http://portal.ifma.edu.br/";
    private Document doc;
    private SliderLayout slNewsDynamics;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        hideNews();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        HandlerJsoup handlerJsoup = new HandlerJsoup(this);
        handlerJsoup.execute(URL_IFMA);

//        scrollLayout();
        if (slNewsDynamics != null) slNewsDynamics.startAutoCycle();
    }

    @Override
    public void onStop() {
        if (slNewsDynamics != null)
            slNewsDynamics.stopAutoCycle();
        super.onStop();
    }

    private void showNews() {
        rootView.findViewById(R.id.rl_container).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.pb_news).setVisibility(View.GONE);
        rootView.findViewById(R.id.tv_err).setVisibility(View.GONE);
    }

    private void hideNews() {
        rootView.findViewById(R.id.rl_container).setVisibility(View.GONE);
        rootView.findViewById(R.id.pb_news).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.tv_err).setVisibility(View.GONE);

    }

    private void showErrorConnection() {
        rootView.findViewById(R.id.rl_container).setVisibility(View.GONE);
        rootView.findViewById(R.id.pb_news).setVisibility(View.GONE);
        rootView.findViewById(R.id.tv_err).setVisibility(View.VISIBLE);
    }

    private void initViews() {
        slNewsDynamics = (SliderLayout) rootView.findViewById(R.id.slider_news_dynamics);
        ImageView ivNewsFeatured = (ImageView) rootView.findViewById(R.id.iv_news_featured);
        TextView tvNewsFeatured = (TextView) rootView.findViewById(R.id.tv_title_news_featured);
        CardView cvNewsFeatured = (CardView) rootView.findViewById(R.id.cv_news_featured);
        RecyclerView rvNewsNonImage = (RecyclerView) rootView.findViewById(R.id.rv_news_non_image);
        RecyclerView rvNewsImageSmaller = (RecyclerView) rootView.findViewById(R.id.rv_news_image_smaller);

        try {
            initNews(ivNewsFeatured, tvNewsFeatured, cvNewsFeatured,
                    rvNewsNonImage, rvNewsImageSmaller);

        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException e) {
            hideNews();
            Log.w(TAG, "initViews: ", e);
            e.printStackTrace();

        }
    }

    private void initNews(ImageView ivNewsFeatured, TextView tvNewsFeatured,
                          CardView cardView, RecyclerView rvNewsNonImage, RecyclerView rvNewsImageSmaller) {
        initNewsDynamics(slNewsDynamics);
        initNewsFeatured(ivNewsFeatured, tvNewsFeatured, cardView);
        initNewsNonImage(rvNewsNonImage);
        initNewsImagesSmaller(rvNewsImageSmaller);
    }

    private void initNewsImagesSmaller(RecyclerView rvNewsImageSmaller) throws NullPointerException {
        List<News.NSmallImages> nSmallImages = HandlerJsoup.getNewsSmallImages(doc);
        Log.d(TAG, "initNewsImagesSmaller: " + String.valueOf(nSmallImages != null));
        if (nSmallImages != null) {
            NewsImagesSmallerAdapter adapter = new NewsImagesSmallerAdapter(nSmallImages, getActivity());

            rvNewsImageSmaller.setAdapter(adapter);
            rvNewsImageSmaller.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            rvNewsImageSmaller.setLayoutManager(llm);
//            scrollLayout();
            showNews();
        } else {
            Log.d(TAG, "initNewsImagesSmaller: null -> called");
            hideNews();
        }
    }

    private void initNewsNonImage(RecyclerView rvNewsNonImage) throws NullPointerException {
        List<News.NNonImage> nonImageList = HandlerJsoup.getNewsNonImage(doc);
        Log.d(TAG, "initNewsNonImage: " + String.valueOf(nonImageList != null));
        if (nonImageList != null) {
            NewsNonImageAdapter adapter = new NewsNonImageAdapter(nonImageList, getActivity());

            rvNewsNonImage.setAdapter(adapter);
            rvNewsNonImage.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rvNewsNonImage.setLayoutManager(llm);
            showNews();
//            scrollLayout();
        } else {
            Log.d(TAG, "initNewsNonImage: null -> called");
            hideNews();
        }
    }

    private void initNewsFeatured(ImageView ivNewsFeatured, TextView tvNewsFeatured, CardView cardView) throws IllegalArgumentException, NullPointerException {
        News.NFeatured nFeatured = HandlerJsoup.getNewsFeatured(doc);
        Log.d(TAG, "initNewsFeatured: " + String.valueOf(nFeatured != null));
        if (nFeatured != null) {
            Picasso.with(getActivity()).load(nFeatured.getLinkImg()).into(ivNewsFeatured);
            tvNewsFeatured.setText(nFeatured.getText());

            ivNewsFeatured.setOnClickListener(this);
            tvNewsFeatured.setOnClickListener(this);
            cardView.setOnClickListener(this);
            showNews();
//            scrollLayout();
        } else {
            Log.d(TAG, "initNewsFeatured: null -> called");
            hideNews();
        }
    }

    private void initNewsDynamics(SliderLayout slNewsDynamics) throws NullPointerException, NoSuchElementException {
        List<News.NDynamic> nDynamicsList = HandlerJsoup.getNewsDynamics(doc);
        Log.d(TAG, "initNewsDynamics: " + String.valueOf(nDynamicsList != null));
        if (nDynamicsList != null) {
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
            showNews();
//            scrollLayout();
        } else {
            Log.d(TAG, "initNewsDynamics: null -> called");
            hideNews();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        String url = slider.getBundle().getString("link");
        AlertLinkExternal.openAlertDialog(url, getActivity(), true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_news_featured || v.getId() == R.id.tv_title_news_featured || v.getId() == R.id.cv_news_featured) {
            String url = HandlerJsoup.getNewsFeatured(doc).getLink();
            AlertLinkExternal.openAlertDialog(url, getActivity(), true);
        }
    }

    @Override
    public void onJsoupDocumentGet(Document doc) {
        hideNews();
        if (GeneralMethods.isConnected(getContext())) {
            this.doc = doc;
            Log.d(TAG, "onJsoupDocumentGet: " + doc.title());
            initViews();
            if (doc.title().equals(HandlerJsoup.SOCKET_TIMEOUT_EXCEPTION))
                showErrorConnection();
        } else {
            showErrorConnection();
        }
    }
}
