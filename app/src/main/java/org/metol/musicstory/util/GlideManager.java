package org.metol.musicstory.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import org.metol.musicstory.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class GlideManager {
    public class FbAvatarType{
        public static final String TYPE_SMALL = "small";
        public static final String TYPE_NORMAL = "normal";
        public static final String TYPE_ALBUM = "album";
        public static final String TYPE_LARGE = "large";
        public static final String TYPE_SQUARE = "square";
    }

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

    /**
     * @param type FbAvatarType
     * */
    public static void setFbAvatarImage(Context cnx, String fbId, String type, ImageView imageView){
        /**
         * type:small, normal, album, large, square
         * */
        Glide.with(cnx)
                .load("https://graph.facebook.com/"+fbId+"/picture?type="+ (TextUtils.isEmpty(type)?"normal":type))
                .crossFade()
                .placeholder(cnx.getResources().getDrawable(R.drawable.default_avatar))
                .transform(new CircleTransform(cnx))
                .into(imageView);
    }

    public static void getDrawableFbAvatarFromUrl(Context cnx, String fbId, String type, Callback callback) {
        getDrawableFromUrl(cnx, "https://graph.facebook.com/" + fbId + "/picture?type"+ (TextUtils.isEmpty(type)?"normal":type), callback);
    }
    public static void getDrawableFromUrl(Context cnx, String url, Callback callback) {
        Glide.with(cnx)
                .load(url)
                .asBitmap()
                .transform(new CircleTransform(cnx))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        callback.onBitmap(resource);
                        callback.onDrawable(new BitmapDrawable(cnx.getResources(), resource));
                    }
                });
    }

    public interface Callback{
        void onDrawable(Drawable drawable);
        void onBitmap(Bitmap bitmap);
    }
}
