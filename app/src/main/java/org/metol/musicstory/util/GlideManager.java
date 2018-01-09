package org.metol.musicstory.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.metol.musicstory.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class GlideManager {
    public static void setBackgroundImageWithGaussianBlur(Context cnx, String url, ImageView imageView){
        Glide.with(cnx)
                .load(url)
                .crossFade()
                .placeholder(new ColorDrawable(cnx.getResources().getColor(R.color.app_theme)))
                .bitmapTransform(new BlurTransformation(cnx, 80))
                .into(imageView);
    }

    public static void setSongImage(final Context cnx, String url, final ImageView imageView){
        Glide.with(cnx)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(cnx.getResources().getDrawable(R.drawable.default_cover))
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(cnx.getResources(), resource);
                        roundedBitmapDrawable.setCornerRadius(24f);
                        imageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });
    }

    public static void setCardImage(Context cnx, String url, ImageView imageView){
        Glide.with(cnx)
                .load(url)
                .crossFade()
                .placeholder(cnx.getResources().getDrawable(R.drawable.default_cover))
                .into(imageView);
    }

    public static void setFBAvatarImage(Context cnx, String fbId, ImageView imageView){
        /**
         * type:small, normal, album, large, square
         * */
        Glide.with(cnx)
                .load("https://graph.facebook.com/"+fbId+"/picture?type=small")
                .crossFade()
                .placeholder(cnx.getResources().getDrawable(R.drawable.default_avatar))
                .transform(new CircleTransform(cnx))
                .into(imageView);
    }
}
