package cn.raise.app.print.common;


import java.util.List;

import cn.raise.app.print.IPrinter;
import cn.raise.app.print.model.BasePrintModel;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/19.
 */

public interface ICommonPrinter extends IPrinter {
    int MSG_CONNECT_SUCCESS = 1;
    int MSG_CONNECT_ERROR = 2;
    int MSG_NO_DEVICE = 3;
    int MSG_PRINT_ERROR = 4;
    int MSG_PRINT_CMD_DONE = 5;
    void closeConnect();
    Observable<String> printList(final List<BasePrintModel> list, final String mAddress);
}
