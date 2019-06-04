package cn.raise.app.print.common1;

import java.io.IOException;
import java.util.List;

import cn.raise.app.print.common.PrintCallback;
import cn.raise.app.print.model.BasePrintModel;

/**
 * 描述: 打印
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/11 11:38
 */
public interface IPrinter1 {

    int PAPER_58 = 1;
    int PAPER_80 = 2;

    void print(BasePrintModel basePrintModel) throws IOException;

    void print(List<BasePrintModel> basePrintModel) throws IOException;

    boolean closeConnect();

    void setPrintCallback(PrintCallback callback);
}
