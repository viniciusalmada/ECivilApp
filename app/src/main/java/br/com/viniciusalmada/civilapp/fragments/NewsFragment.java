package br.com.viniciusalmada.civilapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.concurrent.ExecutionException;

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
    private static final String URL_IFMA = "http://portal.ifma.edu.br/";
    private Document doc;
    private SliderLayout slNewsDynamics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        initViews(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        slNewsDynamics.startAutoCycle();
        super.onStart();
    }

    @Override
    public void onStop() {
        slNewsDynamics.stopAutoCycle();
        super.onStop();
    }

    private void initViews(View rootView) {
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
//        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_news_featured || v.getId() == R.id.tv_title_news_featured || v.getId() == R.id.cv_news_featured) {
            String url = HandlerJsoup.getNewsFeatured(doc).getLink();
            AlertLinkExternal.openAlertDialog(url, getActivity());
        }
    }
}
