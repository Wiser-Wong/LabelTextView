package com.wiser.label;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

/**
 * @author Wiser
 *
 * 标签View
 */
public class LabelTextView extends AppCompatTextView {

    private int layoutId;

    private int textId;

    public LabelTextView(Context context) {
        super(context);
        init(null);
    }

    public LabelTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if (attrs != null) {
            TypedArray td = getContext().obtainStyledAttributes(attrs,R.styleable.LabelTextView);
            layoutId = td.getResourceId(R.styleable.LabelTextView_ltv_labelLayoutId,0);
            textId = td.getResourceId(R.styleable.LabelTextView_ltv_labelTextId,0);
            td.recycle();
        }
    }

    public void setLabels(String content,List<String> list,List<Integer> resources){
        if (list == null || list.size() == 0 || resources == null || resources.size() == 0 || list.size() != resources.size()) return;
        if (!checkId()) throw new IllegalArgumentException("请设置label标签的布局Id以及标签文本的Id");
        setViews(content,list,resources);
    }

    private boolean checkId(){
        return layoutId > 0 && textId > 0;
    }

    private void setView(String content,List<String> list){
//        for (int i = 0; i < list.size(); i++) {
//            SpannableString spanString = new SpannableString(list.get(i) + " ");
////            int start = content.indexOf(list.get(i));
////            int end = start + list.get(i).length();
////            spanString.setSpan(new BackgroundColorSpan(Color.RED),start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spanString.setSpan(new RadiusBackgroundSpan(Color.RED,20),0,list.get(i).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            append(spanString);
//        }
//        append(content);

    }

    /**
     * 设置标签View
     * @param content 文本内容
     * @param labels 标签列表
     * @param resources  标签对应的背景列表
     */
    public void setViews(String content, List<String> labels, List<Integer> resources) {
        StringBuilder content_buffer = new StringBuilder();
        for (String item : labels) {//将每个tag的内容添加到content后边，之后将用drawable替代这些tag所占的位置
            content_buffer.append(item);
        }
        content_buffer.append(content);
        SpannableString spannableString = new SpannableString(content_buffer);
        for (int i = 0; i < labels.size(); i++) {
            String item = labels.get(i);
            View view = LayoutInflater.from(getContext()).inflate(layoutId, null);//R.layout.tag是每个标签的布局
            TextView tvLabel = view.findViewById(textId);
            tvLabel.setText(item);
            tvLabel.setBackgroundResource(resources.get(i));

            Bitmap bitmap = convertViewToBitmap(view);
            Drawable drawable = new BitmapDrawable(getResources(),bitmap);
            drawable.setBounds(0, 0, tvLabel.getMeasuredWidth() + dp2px(3), tvLabel.getMeasuredHeight());//缺少这句的话，不会报错，但是图片不回显示
            CenterImageSpan span = new CenterImageSpan(drawable);//图片将对齐底部边线
            int startIndex;
            int endIndex;
            startIndex = getLastLength(labels, i);
            endIndex = startIndex + item.length();
            spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    private int getLastLength(List<String> list, int maxLength) {
        int length = 0;
        for (int i = 0; i < maxLength; i++) {
            length += list.get(i).length();
        }
        return length;
    }

    // 将View转换成Bitmap
    private static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
