package com.example.nettylib.netty.base;


import com.google.protobuf.ByteString;

import java.util.ArrayList;

public class HeadDTO {


    @TechFled("flag")
    public int flag;//起始标记
    
    @TechFled("token")
    public String token;//停车场/token

    @TechFled("msgId")
    public int msgId;//帧消息id

    @TechFled("mac")
    public String mac;//校验码
    
    @TechFled("status")
    public int status;

    @TechFled("totalLen")
    public int totalLen;//数据长度


    @TechFled("remark")
    public String remark; //业务数据长度

    @TechFled("funcCode")
    public int funcCode; //业务数据长度


    @TechFled("ver")
    public int ver;//版本号
//
//    @TechFled("data")
//    public ArrayList<ByteString> data; //业务数据长度



}
