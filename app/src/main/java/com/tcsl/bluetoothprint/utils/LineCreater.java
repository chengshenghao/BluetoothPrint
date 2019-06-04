package com.tcsl.bluetoothprint.utils;

import cn.raise.app.print.IPrinter;
import cn.raise.app.print.model.LineModel;

/**
 * 描述:根据纸张类型，设置横线的长度
 * <p/>作者：wjx
 * <p/>创建时间: 2017/5/27 10:59
 */
public class LineCreater {
    public static LineModel create(){
        if(LocalXml.getPrintPaperMode()== IPrinter.PAPER_58){
            return new LineModel("- - - - - - - - - - - - - - - - ");
        }else {
            return new LineModel();
        }
    }
}
