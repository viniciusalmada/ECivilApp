package br.almadaapps.civilapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;

/**
 * Created by vinicius-almada on 16/03/17.
 */

public class FacebookButton extends android.support.v7.widget.AppCompatButton {
    private Context context;

    public FacebookButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        Typeface font = Typeface.SANS_SERIF;
        Typeface font2 = Typeface.createFromAsset(context.getAssets(), "fonts/facebookfont3.ttf");
        SpannableStringBuilder string = new SpannableStringBuilder("Login via Facebook");
        string.setSpan(new CustomTypefaceSpan("", font), 0, 9, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        string.setSpan(new RelativeSizeSpan(0.85f), 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new CustomTypefaceSpan("", font2), 10, 18, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        setText(string);

        setText(string);
    }
}
