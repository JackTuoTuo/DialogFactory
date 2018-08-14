package com.tuo.dialog_factory;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * describe:
 * 作者：JackTuoTuo on 2017/7/18 20:35
 * 邮箱：839539179@qq.com
 */
public class DialogFactory {


    private TextView tvTitle;

    private EditText etInput;

    private TextView tvContent;

    private RelativeLayout rlContent;

    private TextView tvLeft;

    private TextView tvRight;

    private CheckBox cbNoTips;

    private Context mContext;
    private String mTitle;
    private String mMessage;

    private View mContentView;
    private int mLayoutId;
    private int mGravity;
    private int mThemeResId;

    private int mLayoutWidth;
    private int mLayoutHeight;

    private int mHoffset = 0;// 相对于屏幕中心点的 水平偏移

    private int mVoffset = 0;// 相对于屏幕中心点的 垂直偏移

    private boolean mCancelable;
    private boolean mCanceledOnTouchOutside;
    private boolean mShowNoTips; // 是否显示 不再提示的选项
    private String mIgnoreText;
    private String mLeftBtnText;
    private String mRightBtnText;

    private OnBtnClickListener mLeftBtClickListener;
    private OnBtnClickListener mRightBtClickListener;

    private Dialog mDialog;

    private DialogFactory(Builder builder) {
        this.mContext = builder.mContext;
        this.mTitle = builder.mTitle;
        this.mMessage = builder.mMessage;
        this.mLayoutId = builder.mLayoutId;
        this.mContentView = builder.mContentView;
        this.mGravity = builder.mGravity;
        this.mLayoutWidth = builder.mLayoutWidth;
        this.mLayoutHeight = builder.mLayoutHeight;
        this.mHoffset = builder.mHoffset;
        this.mVoffset = builder.mVoffset;
        this.mShowNoTips = builder.mShowNoTips;
        this.mIgnoreText = builder.mIgnoreText;
        this.mThemeResId = builder.mThemeResId;
        this.mCancelable = builder.mCancelable;
        this.mCanceledOnTouchOutside = builder.mCanceledOnTouchOutside;
        this.mLeftBtnText = builder.mLeftBtnText;
        this.mRightBtnText = builder.mRightBtnText;
        this.mLeftBtClickListener = builder.mLeftBtnClickListener;
        this.mRightBtClickListener = builder.mRightBtnClickListener;

        init();
        initWindows();
    }

    private void init() {
        mDialog = new Dialog(mContext, mThemeResId);

        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
        } else {
            // 当设置 contentView 需要修改默认布局
            mLayoutId = -1;
        }

        // 默认布局
        if (mLayoutId == R.layout.layout_dialog) {
            tvTitle = mContentView.findViewById(R.id.tv_title);
            etInput = mContentView.findViewById(R.id.et_input);
            tvLeft = mContentView.findViewById(R.id.tv_left);
            tvRight = mContentView.findViewById(R.id.tv_right);
            tvContent = mContentView.findViewById(R.id.tv_content);
            setDefault();
        } else {
            tvLeft = mContentView.findViewById(R.id.tv_left);
            tvRight = mContentView.findViewById(R.id.tv_right);
            setListener();
        }
        mDialog.setContentView(mContentView);

        mDialog.setCancelable(mCancelable);
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
    }

    private void initWindows() {
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // 垂直偏移量
        lp.y = dp2px(mContext, mVoffset);
        lp.x = dp2px(mContext, mHoffset);
        lp.width = mLayoutWidth;
        lp.height = mLayoutHeight;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(mGravity);
    }

    private void setDefault() {
        tvTitle.setText(mTitle);
        tvLeft.setText(mLeftBtnText);
        tvRight.setText(mRightBtnText);
        setContent();
        setListener();
    }

    private void setContent() {

        tvContent.setVisibility(View.GONE);
        etInput.setVisibility(View.GONE);
        cbNoTips.setVisibility(View.GONE);
        if (mMessage != null) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(mMessage);
        } else {
            etInput.setVisibility(View.VISIBLE);
        }
        if (mShowNoTips) {
            cbNoTips.setText(mIgnoreText);
            cbNoTips.setVisibility(View.VISIBLE);
        }

    }

    private void setListener() {
        if (tvLeft != null) {
            tvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLeftBtClickListener != null) {
                        mLeftBtClickListener.onClick(DialogFactory.this);
                    }
                }
            });
        }

        if (tvRight != null) {
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRightBtClickListener != null) {
                        mRightBtClickListener.onClick(DialogFactory.this);
                    }
                }
            });
        }
    }


    public boolean isShowing() {
        return mDialog.isShowing();
    }


    public void show() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public EditText getEtInput() {
        return etInput;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public RelativeLayout getLayoutContent() {
        return rlContent;
    }

    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    public View getContentView() {
        return mContentView;
    }

    public String getEtInputContent() {
        return etInput.getText().toString().trim();
    }

    /**
     * 返回 不再提示是否选中
     */
    public boolean isIgnoreChecked() {
        return cbNoTips.isChecked();
    }

    /**
     * Builder
     */
    public static class Builder {

        private Context mContext;
        private String mTitle;
        private String mMessage;

        private int mLayoutId = R.layout.layout_dialog; //默认的布局
        private View mContentView;
        private int mThemeResId = R.style.Dialog_Base; //默认主题
        private int mGravity = Gravity.CENTER; //默认居中
        private boolean mShowNoTips = false;// 是否需要显示  不再提示的选项 默认不提示
        private String mIgnoreText;
        private int mLayoutWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int mLayoutHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        private int mHoffset = 0;// 相对于屏幕中心点的 水平偏移
        private int mVoffset = 0;// 相对于屏幕中心点的 垂直偏移
        /**
         * 控制 点击返回键和屏幕外的时候  dialog是否会消失
         */
        private boolean mCancelable = true; //默认会消失
        /**
         * 返回键一定可以控制消失  设置屏幕外点击  dialog是否会消失
         */
        private boolean mCanceledOnTouchOutside = true; // 默认会消失

        private OnBtnClickListener mLeftBtnClickListener;
        private OnBtnClickListener mRightBtnClickListener;
        private String mLeftBtnText = "取消";
        private String mRightBtnText = "确定";

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder(Context context, String title) {
            this.mContext = context;
            this.mTitle = title;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            this.mLayoutId = layoutId;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.mContentView = contentView;
            return this;
        }

        public Builder setMessage(String content) {
            this.mMessage = content;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        public Builder setLayoutParams(int layoutWidth_dp, int layoutHeight_dp) {
            // 如果是wrap和match属性，则直接设值，不转dp
            if (layoutWidth_dp == ViewGroup.LayoutParams.WRAP_CONTENT
                    || layoutWidth_dp == ViewGroup.LayoutParams.MATCH_PARENT) {
                this.mLayoutWidth = layoutWidth_dp;
            } else {
                this.mLayoutWidth = dp2px(mContext, layoutWidth_dp);
            }
            // 如果是wrap和match属性，则直接设值，不转dp
            if (layoutHeight_dp == ViewGroup.LayoutParams.WRAP_CONTENT
                    || layoutHeight_dp == ViewGroup.LayoutParams.MATCH_PARENT) {
                this.mLayoutHeight = layoutHeight_dp;
            } else {
                this.mLayoutHeight = dp2px(mContext, layoutHeight_dp);
            }
            return this;
        }

        public Builder setHoffset(int hoffset_dp) {
            this.mHoffset = hoffset_dp;
            return this;
        }

        public Builder setVoffset(int voffset_dp) {
            this.mVoffset = voffset_dp;
            return this;
        }

        public Builder setThemeResId(int themeResId) {
            this.mThemeResId = themeResId;
            return this;
        }

        public Builder showNoTips(boolean showNoTips, String ignoreText) {
            this.mShowNoTips = showNoTips;
            this.mIgnoreText = ignoreText;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setLeftBtnText(String leftBtnText) {
            this.mLeftBtnText = leftBtnText;
            return this;
        }

        public Builder setRightBtnText(String rightBtnText) {
            this.mRightBtnText = rightBtnText;
            return this;
        }


        public Builder setLeftBtnListener(OnBtnClickListener leftButtonListener) {
            this.mLeftBtnClickListener = leftButtonListener;
            return this;
        }

        public Builder setRightBtnListener(OnBtnClickListener rightBtListener) {
            this.mRightBtnClickListener = rightBtListener;
            return this;
        }

        public DialogFactory create() {
            DialogFactory dialogFactory = new DialogFactory(this);
            return dialogFactory;
        }


    }

    public interface OnBtnClickListener {
        void onClick(DialogFactory dialogFactory);
    }

    public void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        mDialog.setOnShowListener(onShowListener);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

}
