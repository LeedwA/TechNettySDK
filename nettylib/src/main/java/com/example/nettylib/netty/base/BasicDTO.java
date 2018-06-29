package com.example.nettylib.netty.base;

import com.example.nettylib.netty.base.HeadDTO;
import com.google.protobuf.ByteString;

import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * 通讯格式
 */
public class BasicDTO extends HeadDTO implements Serializable {


    @TechFled("opCode")
    public int opCode;//操作码
    @TechFled("baseVer")
    public int baseVer;//版本号
    @TechFled("param")
    public int param;//业务参数
    @TechFled("dataLen")
    public int dataLen; //业务数据长度
//    @TechFled("data")
//    public ArrayList<ByteString> data; //业务数据长度


}
