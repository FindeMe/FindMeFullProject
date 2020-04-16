package Arabs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;


public class ArabButton extends
        AppCompatButton
{
    public ArabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ArabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArabButton(Context context) {
        super(context);
    }
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/GE_SS_Two_Bold.otf")/*, -1*/);
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/GE_SS_Two_Light.otf")/*, -1*/);
        }
       // setIconGravity(ICON_GRAVITY_TEXT_START);
    }
}
