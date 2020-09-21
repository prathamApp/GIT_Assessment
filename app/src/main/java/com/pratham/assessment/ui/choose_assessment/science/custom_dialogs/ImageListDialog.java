package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.gif_viewer.GifViewZoom;
import com.pratham.assessment.custom.zoom_image.ZoomageView;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.UpdateImageListListener;
import com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.ImageAnswerFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.ImageAnswerFragment.SHOW_DIALOG;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileExtension;

@EActivity(R.layout.image_list_dialog)
public class ImageListDialog extends AppCompatActivity {

    @ViewById(R.id.btn_ok_img)
    ImageButton btn_ok;
    @ViewById(R.id.btn_delete_img)
    ImageButton btn_delete_img;
    @ViewById(R.id.ib_prev)
    ImageButton ib_prev;
    @ViewById(R.id.ib_next)
    ImageButton ib_next;
    @ViewById(R.id.tv_img_label)
    TextView tv_img_label;

    @ViewById(R.id.iv_captured_image)
    ImageView iv_captured_image;
    boolean showDeleteBtn;
    private List imageList;
    UpdateImageListListener listListener;
    private int currentCnt = 0;
    ImageAnswerFragment fragment;
    /*public ZoomImageDialog(@NonNull Context context, String path, String localPath) {
//        super(context,android.R.style.Theme_NoTitleBar_Fullscreen);
//        super(context, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
//        super(context,android.R.style.Theme_Holo_NoActionBar_Fullscreen);
        this.context = context;
        this.path = path;
        this.localPath = localPath;
    }*/

    /*public ZoomImageDialog(Context context, String path, String qtid, String localPath) {
        super(context);
        this.context = context;
        this.path = path;
        this.qtid = qtid;
        this.localPath = localPath;
}*/

    @AfterViews
    public void init() {

        this.imageList = getIntent().getParcelableArrayListExtra("imageList");
        showDeleteBtn = getIntent().getBooleanExtra("showDeleteButton", false);
        showImages();
    }

   /* public ImageListDialog(Context context, List<String> imageList) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);

    }*/

    private void showImages() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ScienceAssessmentActivity.dialogOpen = true;
        if (!showDeleteBtn) {

            ib_next.setVisibility(View.INVISIBLE);
            if (showDeleteBtn) {
                btn_delete_img.setVisibility(View.VISIBLE);
                ib_prev.setVisibility(View.INVISIBLE);
                ib_next.setVisibility(View.INVISIBLE);
            } else {
                btn_delete_img.setVisibility(View.INVISIBLE);
                ib_prev.setVisibility(View.VISIBLE);
                ib_next.setVisibility(View.VISIBLE);
            }
        } else {
            ib_prev.setVisibility(View.INVISIBLE);
            ib_next.setVisibility(View.INVISIBLE);
        }
        setInitImage();

    }

    private void setInitImage() {
        if (imageList.size() > 0) {
            if (!showDeleteBtn) {
                if (imageList.size() == 1) ib_prev.setVisibility(View.INVISIBLE);
                tv_img_label.setText(getString(R.string.image) + imageList.size() + "/" + imageList.size());
            }
            currentCnt = imageList.size() - 1;
            setUri(imageList.get(currentCnt));
            showPrevNext();
        } else {
            updateList();
           /* ImageAnswerFragment.updateList(imageList);
            finish();*/
        }
    }

    private void setUri(Object image) {
        Glide.with(this)
                .load(image)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(iv_captured_image);
    }

    @Click(R.id.ib_next)
    public void nextImg() {
        currentCnt++;
        if (currentCnt < imageList.size()) {
            setUri(imageList.get(currentCnt));
        } else {
            currentCnt = imageList.size();
        }
        showPrevNext();

    }

    @Click(R.id.ib_prev)
    public void prevImg() {
        currentCnt--;
        if (currentCnt > -1) {
            setUri(imageList.get(currentCnt));
        } else currentCnt = 0;

        showPrevNext();

    }

    private void showPrevNext() {
        if (!showDeleteBtn)
            tv_img_label.setText(getString(R.string.image) + (currentCnt + 1) + "/" + imageList.size());

        if (!showDeleteBtn)
            if (currentCnt > -1 && currentCnt < imageList.size() - 1)
                ib_next.setVisibility(View.VISIBLE);
            else ib_next.setVisibility(View.INVISIBLE);

        if (!showDeleteBtn)
            if (currentCnt > 0)
                ib_prev.setVisibility(View.VISIBLE);
            else ib_prev.setVisibility(View.INVISIBLE);
    }


    @Click(R.id.btn_ok_img)
    public void closeDialog() {
        ScienceAssessmentActivity.dialogOpen = false;
        if (showDeleteBtn)
            updateList();
        else finish();

//        ImageAnswerFragment.updateList(imageList);
    }

    private void updateList() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("imageList", (ArrayList<? extends Parcelable>) imageList);
        setResult(SHOW_DIALOG, intent);
        finish();
    }

    @Click(R.id.btn_delete_img)
    public void deleteImage() {
        imageList.remove(currentCnt);
//        ImageAnswerFragment.updateList(imageList);
        updateList();
//        setInitImage();
//        finish();
    }
  /* @Click(R.id.btn_confirm)
    public void confirm() {
        imageList.remove(currentCnt);
//        ImageAnswerFragment.updateList(imageList);
        updateList();
//        setInitImage();
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        ScienceAssessmentActivity.dialogOpen = false;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ScienceAssessmentActivity.dialogOpen = false;
//        ImageAnswerFragment.updateList(imageList);
//        updateList();
    }
}

