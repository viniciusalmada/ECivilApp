package br.com.viniciusalmada.civilapp.extras;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import br.com.viniciusalmada.civilapp.domains.News;

/**
 * Created by vinicius-almada on 21/03/17.
 */

public class HandlerJsoup extends AsyncTask<String, Void, Document> {
//    private Document doc;

    /*public HandlerJsoup(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    public static List<News.NDynamic> getNewsDynamics(Document doc) {
        Elements divDN = doc.select("#cycloneslider-home-slider-1 > div.cycloneslider-slides.cycle-slideshow > div");
        ListIterator<Element> divsDN = divDN.listIterator();
        List<News.NDynamic> nDynamics = new ArrayList<News.NDynamic>();
        while (true) {
            Element e = divsDN.next();
            String link = e.select("a").attr("href");
            String linkImg = e.select("a > img").attr("src");
            nDynamics.add(new News.NDynamic(link, linkImg));
            if (!divsDN.hasNext())
                break;
        }
        return nDynamics;
    }

    public static News.NFeatured getNewsFeatured(Document doc) {
        Elements divFN = doc.select("#noticia-destaque");
        String linkFN = divFN.select("h1 > a").attr("href");
        String linkImgFN = getURL(divFN.select("a > div").attr("style"));
        String textFN = divFN.select("h1 > a").text();
        return new News.NFeatured(textFN, linkFN, linkImgFN);
    }

    public static List<News.NNonImage> getNewsNonImage(Document doc) {
        Elements h2NIN = doc.select("#noticia-sem-foto > h2");
        Elements divNIN = doc.select("#noticia-sem-foto > div");
        ListIterator<Element> h2sNIN = h2NIN.listIterator();
        ListIterator<Element> divsNIN = divNIN.listIterator();
        List<News.NNonImage> list = new ArrayList<News.NNonImage>();
        while (true) {
            Element h2 = h2sNIN.next();
            String link = h2.select("a").attr("href");
            String text = h2.select("a").text();
            Element div = divsNIN.next();
            String comment = div.select("a").text();
            News.NNonImage nni = new News.NNonImage(link, text, comment);
            list.add(nni);
            if (!h2sNIN.hasNext())
                break;

        }
        return list;
    }

    public static List<News.NSmallImages> getNewsSmallImages(Document doc) {
        Elements div = doc.select("#noticia-foto-pequena");
        ListIterator<Element> listIterator = div.listIterator();
        Element e = listIterator.next();
        List<News.NSmallImages> list = new ArrayList<News.NSmallImages>();
        for (int i = 0; i < 3; i++) {
            String link = e.child(i).select("a").attr("href");
            String linkImg = getURL(e.child(i).select("div").attr("style"));
            String text = e.child(i).select("h3 > a").text();
            list.add(new News.NSmallImages(link, linkImg, text));
        }
        return list;
    }

    private static String getURL(String s) {
        int begin = s.lastIndexOf("('");
        int end = s.indexOf("')");
        return s.substring(begin + 2, end);
    }

    @Override
    protected Document doInBackground(String... params) {
        try {
            return Jsoup.connect(params[0]).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
