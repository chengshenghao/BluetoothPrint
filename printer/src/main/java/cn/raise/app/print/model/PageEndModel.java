package cn.raise.app.print.model;

/**
 * 描述: 页末走纸
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/13 16:29
 */
public class PageEndModel extends BasePrintModel {

    public PageEndModel() {
        // 默认值
        setPageGo(3);
        setEnter(true);
        setCutPage(true);
    }
}
