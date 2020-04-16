package Arabs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;


public class ArabEditText extends AppCompatEditText {
    public ArabEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ArabEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArabEditText(Context context) {
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
