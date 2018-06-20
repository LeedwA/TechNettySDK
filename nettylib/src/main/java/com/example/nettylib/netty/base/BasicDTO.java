package com.example.nettylib.netty.base;

import com.example.nettylib.netty.base.HeadDTO;

import java.io.Serializable;


/**
 *
 * 通讯格式
 */
public class BasicDTO extends HeadDTO implements Serializable {


    @TechFled("opCode")
    public int opCode;//操作码
    @TechFled("ver")
    public int ver;//版本号
    @TechFled("param")
    public int param;//业务参数
    @TechFled("dataLen")
    public int dataLen; //业务数据长度


}
