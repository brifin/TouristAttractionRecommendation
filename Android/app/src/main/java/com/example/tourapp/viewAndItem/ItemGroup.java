package com.example.tourapp.viewAndItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tourapp.R;

public class ItemGroup extends FrameLayout {

    private LinearLayout itemGroupLayout; //组合控件的布局
    private ImageView img; //标题
    private TextView contentEdt; //输入框
    private ImageView jtRightIv; //向右的箭头

    public TextView getContentEdt() {
        return contentEdt;
    }

    public ItemGroup(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ItemGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public ItemGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context,attrs);
    }

    //初始化View
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_layout, null);
        itemGroupLayout = (LinearLayout) view.findViewById(R.id.item_group_layout);
        img = (ImageView) view.findViewById(R.id.picture_iv);
        contentEdt = (TextView) view.findViewById(R.id.content_edt);
        jtRightIv = (ImageView) view.findViewById(R.id.jt_right_iv);
        addView(view); //把自定义的这个组合控件的布局加入到当前FramLayout
    }
    /**
     * 初始化相关属性，引入相关属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        //标题的默认字体颜色
        int defaultTitleColor = context.getResources().getColor(R.color.gray);
        //输入框的默认字体颜色
        int defaultEdtColor = context.getResources().getColor(R.color.black);
        //输入框的默认的提示内容的字体颜色
        int defaultHintColor = context.getResources().getColor(R.color.gray_);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemGroup);
        int resourceId = typedArray.getResourceId(R.styleable.ItemGroup_img, R.drawable.dianzan);
        float paddingLeft = typedArray.getDimension(R.styleable.ItemGroup_paddingLeft, 15);
        float paddingRight = typedArray.getDimension(R.styleable.ItemGroup_paddingRight, 15);
        float paddingTop = typedArray.getDimension(R.styleable.ItemGroup_paddingTop, 5);
        float paddingBottom = typedArray.getDimension(R.styleable.ItemGroup_paddingTop, 5);
        String content = typedArray.getString(R.styleable.ItemGroup_edt_content);
        float contentSize = typedArray.getDimension(R.styleable.ItemGroup_edt_text_size, 13);
        int contentColor = typedArray.getColor(R.styleable.ItemGroup_edt_text_color, defaultEdtColor);
        String hintContent = typedArray.getString(R.styleable.ItemGroup_edt_hint_content);
        int hintColor = typedArray.getColor(R.styleable.ItemGroup_edt_hint_text_color, defaultHintColor);
        //默认输入框可以编辑
        boolean isEditable = typedArray.getBoolean(R.styleable.ItemGroup_isEditable, true);
        //向右的箭头图标是否可见，默认可见
        boolean showJtIcon = typedArray.getBoolean(R.styleable.ItemGroup_jt_visible, true);
        typedArray.recycle();

        //设置数据
        //设置item的内边距
        itemGroupLayout.setPadding((int) paddingLeft, (int) paddingTop, (int) paddingRight, (int) paddingBottom);
        img.setImageResource(resourceId);

        contentEdt.setText(content);
        contentEdt.setTextSize(contentSize);
        contentEdt.setTextColor(contentColor);
        contentEdt.setHint(hintContent);
        contentEdt.setHintTextColor(hintColor);
//        contentEdt.setFocusable(isEditable); //设置输入框是否可以编辑
//        contentEdt.setClickable(true);
//        contentEdt.setKeyListener(null);
        jtRightIv.setVisibility(showJtIcon ? View.VISIBLE : View.GONE);  //设置向右的箭头图标是否可见
    }
}
