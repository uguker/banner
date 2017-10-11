package com.uguke.code.banner;

import com.uguke.code.banner.transformer.AccordionTransformer;
import com.uguke.code.banner.transformer.BTFTransformer;
import com.uguke.code.banner.transformer.BaseTransformer;
import com.uguke.code.banner.transformer.DefaultTransformer;
import com.uguke.code.banner.transformer.DepthPageTransformer;
import com.uguke.code.banner.transformer.FTBTransformer;
import com.uguke.code.banner.transformer.RotateDownTransformer;
import com.uguke.code.banner.transformer.RotateUpTransformer;
import com.uguke.code.banner.transformer.ScaleTransformer;
import com.uguke.code.banner.transformer.ZoomInTransformer;
import com.uguke.code.banner.transformer.ZoomOutTransformer;
import com.uguke.code.banner.transformer.ZoomSlideTransformer;

import java.util.ArrayList;
import java.util.List;

class TransformerManager {

    //当前切换效果位置
    private int position;
    //切换效果列表
    private List<BaseTransformer> transformers;
    
    public TransformerManager(){
        //隐藏构造函数
        position = -1;
        transformers = new ArrayList<>();

    }

    public int size() {
        return transformers.size();
    }

    /**
     * 功能描述：设置切换效果
     * @param transformer
     */
    public void setTransformer(BaseTransformer transformer) {
        transformers.clear();
        transformers.add(transformer);
        position = 0;
    }

    /**
     * 功能描述：添加切换效果
     * @param transformer
     */
    public void addTransformer(BaseTransformer transformer) {
        transformers.add(transformer);
    }

    /**
     * 功能描述：设置默认的随机切换效果集合
     */
    public void setDefaultRandomTransformer() {
        transformers.clear();
        transformers.add(new AccordionTransformer());
        transformers.add(new BTFTransformer());
        transformers.add(new FTBTransformer());
        transformers.add(new DefaultTransformer());
        transformers.add(new DepthPageTransformer());
        transformers.add(new RotateDownTransformer());
        transformers.add(new RotateUpTransformer());
        transformers.add(new ScaleTransformer());
        transformers.add(new ScaleTransformer());
        transformers.add(new ZoomInTransformer());
        transformers.add(new ZoomOutTransformer());
        transformers.add(new ZoomSlideTransformer());
    }

    /**
     * 功能描述：获取下一个切换效果
     * @return
     */
    public BaseTransformer next() {
        if (size() == 0) {
            transformers.add(new DefaultTransformer());
            position = 0;
        } else if (position == size() - 1) {
            position = 0;
        } else {
            position ++;
        }
        return transformers.get(position);
    }

    /**
     * 功能描述：随机获取切换效果
     * @return
     */

    public BaseTransformer random() {
        if (size() == 0)
            setDefaultRandomTransformer();
        position = (int) (Math.random() * size());
        return transformers.get(position);
    }
}
