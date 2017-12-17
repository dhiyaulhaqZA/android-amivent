package id.ac.amikom.avent.utility;

import android.content.Context;
import android.widget.ImageView;

import id.ac.amikom.avent.R;


/**
 * Created by dhiyaulhaqza on 12/2/17.
 */

public final class ImageUtil {
    private ImageUtil() {

    }

    public static void loadImageFromUrl(ImageView imgView, String imgUrl) {
        GlideApp.with(imgView.getContext())
                .load(imgUrl)
                .placeholder(android.R.color.darker_gray)
                .error(R.drawable.side_nav_bar)
                .into(imgView);
    }
}
