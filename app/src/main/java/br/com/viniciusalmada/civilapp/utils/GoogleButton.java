package br.com.viniciusalmada.civilapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;

/**
 * Created by vinicius-almada on 16/03/17.
 */

public class GoogleButton extends android.support.v7.widget.AppCompatButton {
    private Context context;

    public GoogleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        Typeface font = Typeface.SANS_SERIF;
        Typeface font2 = Typeface.createFromAsset(context.getAssets(), "fonts/googlefont.ttf");
        SpannableStringBuilder string = new SpannableStringBuilder("Login via Google");
        string.setSpan(new CustomTypefaceSpan("", font), 0, 9, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        string.setSpan(new RelativeSizeSpan(0.85f), 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new CustomTypefaceSpan("", font2), 10, 15, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        setText(string);

        setText(string);
    }

}
