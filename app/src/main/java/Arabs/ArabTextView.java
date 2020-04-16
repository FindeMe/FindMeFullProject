package Arabs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;


public class ArabTextView extends AppCompatTextView {
    public ArabTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ArabTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArabTextView(Context context) {
        super(context);
    }


    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/GE_SS_Two_Bold.otf")/*, -1*/);
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/GE_SS_Two_Light.otf")/*, -1*/);
        }
    }
}
