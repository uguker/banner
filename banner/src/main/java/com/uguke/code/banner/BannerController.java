package com.uguke.code.banner;

import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uguke.code.banner.bean.IBannerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 功能描述：轮播图控制器
 * @author LeiJue
 * @date 2018/9/25
 */
public class BannerController {

    private TextView hint;
    private TextView title;
    private TextView number;

    private LinearLayout titleContainer;
    private LinearLayout indicatorContainer;
    /** 指示器列表 **/
    private List<View> indicatorList;

    /** 最后选择的位置 **/
    private int lastSelectPosition;

    private int itemCount;

    private BannerType type;
    private BannerAttr attr;

    Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            itemCount = ((int) arg) - 2;
            initIndicator();
        }
    };

    public BannerController(Banner banner) {
        LayoutInflater.from(banner.getContext()).inflate(R.layout.banner_controller, banner, true);
        this.hint = banner.findViewById(R.id.banner_hint);
        this.title = banner.findViewById(R.id.banner_title);
        this.number = banner.findViewById(R.id.banner_number);
        this.titleContainer = banner.findViewById(R.id.banner_title_container);
        this.indicatorContainer = banner.findViewById(R.id.banner_indicator_container);

        this.indicatorList = new ArrayList<>();
        this.type = BannerType.NUMBER;
        this.attr = banner.attr;
        initLayout();
    }
    
    private void initLayout() {

        // 标题
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                isShowTitle() ? attr.titleHeight : 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        titleContainer.setLayoutParams(params);
        titleContainer.setPadding(attr.titlePaddingLeft, 0, attr.titlePaddingRight, 0);
        title.setTextColor(attr.titleTextColor);
        title.setTextSize(attr.titleTextSize);
        ViewCompat.setBackground(titleContainer, attr.titleBackground);

        LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(
                type == BannerType.TITLE_NUMBER ? ViewGroup.LayoutParams.WRAP_CONTENT : 0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        hint.setGravity(Gravity.CENTER_VERTICAL);
        hint.setLayoutParams(hintParams);
        hint.setTextColor(attr.numberTextColor);
        hint.setTextSize(attr.numberTextSize);

        //设置指示器容器
        params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                isShowIndicator() ? ViewGroup.LayoutParams.WRAP_CONTENT : 0);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.leftMargin = attr.indicatorMargin;
        params.rightMargin = attr.indicatorMargin;
        params.bottomMargin = attr.indicatorMargin;
        // 左端指示器
        if (type == BannerType.LEFT || type == BannerType.TITLE_LEFT) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ABOVE, R.id.banner_title_container);
        } else if (type == BannerType.RIGHT || type == BannerType.TITLE_RIGHT) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ABOVE, R.id.banner_title_container);
        } else if (type == BannerType.CENTER || type == BannerType.TITLE_CENTER) {
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.ABOVE, R.id.banner_title_container);
        } else if (type == BannerType.TITLE_INNER) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.bottomMargin = (attr.titleHeight - attr.indicatorHeight) / 2;
            params.rightMargin = attr.titlePaddingRight;
        }

        indicatorContainer.setLayoutParams(params);

        //设置数字指示器属性
        params = new RelativeLayout.LayoutParams(attr.numberWidth, attr.numberHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ABOVE, R.id.banner_title_container);
        params.setMargins(attr.numberMargin, attr.numberMargin, attr.numberMargin, attr.numberMargin);

        number.setLayoutParams(params);
        number.setTextColor(attr.numberTextColor);
        number.setTextSize(attr.numberTextSize);
        number.setVisibility(isShowNumberIndicator() ? View.VISIBLE : View.GONE);
        ViewCompat.setBackground(number, attr.numberBackground);

    }

    /**
     * 功能描述：初始化指示器
     */
    private void initIndicator() {
        indicatorList.clear();
        indicatorContainer.removeAllViews();
        //选择时的背景
//        indicatorSelectedDrawable = ContextCompat.getDrawable(context,
//                attr.getIndicatorSelectedResId());
//        if (indicatorSelectedDrawable == null) {
//            indicatorSelectedDrawable = getDrawable(
//                    attr.getIndicatorSelectedShape(),
//                    attr.getIndicatorSelectedColor());
//        }
        //未选择时的背景
//        indicatorUnselectedDrawable = ContextCompat.getDrawable(context,
//                attr.getIndicatorUnselectedResId());
//        if (indicatorUnselectedDrawable == null) {
//            indicatorUnselectedDrawable = getDrawable(
//                    attr.getIndicatorUnselectedShape(),
//                    attr.getIndicatorUnselectedColor());
//        }

        for (int i = 0; i < itemCount; i++) {
            View view = new View(indicatorContainer.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    attr.indicatorWidth, attr.indicatorHeight);
            params.leftMargin = attr.indicatorPadding;
            params.rightMargin = i == itemCount - 1 ? attr.indicatorPadding : 0;
            ViewCompat.setBackground(view, i == 0 ?attr.indicatorSelected : attr.indicatorUnselected);
            view.setLayoutParams(params);
            indicatorContainer.addView(view);
            indicatorList.add(view);
        }
    }

    private boolean isShowIndicator() {
        return !(type == BannerType.NONE ||
                type == BannerType.NUMBER ||
                type == BannerType.TITLE_ONLY);
    }

    private boolean isShowNumberIndicator() {
        return type == BannerType.NUMBER;
    }

    private boolean isShowTitle() {
        return type.getNumber() > BannerType.NUMBER.getNumber();
    }

    public void update(IBannerItem item) {
        hint.setText(item.getHint());
        title.setText(item.getTitle());
        number.setText(item.getHint());
    }

    void u(int position) {
        if (position >= itemCount) {
            position = 0;
        } else if (position <= -1) {
            position = itemCount - 1;
        }

        //设置背景
        ViewCompat.setBackground(indicatorList.get(lastSelectPosition),
                attr.indicatorUnselected);
        ViewCompat.setBackground(indicatorList.get(position),
                attr.indicatorSelected);
        //indicatorNum.setText((position + 1) + "/" + itemCount);
        lastSelectPosition = position;
    }
}
