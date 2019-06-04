package cn.raise.app.print.model;

/**
 * Created by Administrator on 2017/3/19.
 */

public abstract class BasePrintModel {
    private int space;//距离左边的绝对值长度
    private boolean isEnter;//enter
    private int pageGo;//走纸行数
    private boolean isCutPage;//是否切纸

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public boolean isEnter() {
        return isEnter;
    }

    public void setEnter(boolean enter) {
        isEnter = enter;
    }

    public int getPageGo() {
        return pageGo;
    }

    public void setPageGo(int pageGo) {
        this.pageGo = pageGo;
    }

    public boolean isCutPage() {
        return isCutPage;
    }

    public void setCutPage(boolean cutPage) {
        isCutPage = cutPage;
    }
}
