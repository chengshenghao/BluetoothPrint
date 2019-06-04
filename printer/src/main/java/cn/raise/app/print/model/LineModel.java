package cn.raise.app.print.model;

/**
 * Created by Administrator on 2017/3/19.
 */

public class LineModel extends BasePrintModel {
    private String line = "- - - - - - - - - - - - - - - - - - - - - - - - ";

    public LineModel() {
        setEnter(true);
    }

    public LineModel(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
