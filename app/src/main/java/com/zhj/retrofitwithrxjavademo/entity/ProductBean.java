package com.zhj.retrofitwithrxjavademo.entity;

/**
 * 产品类
 * Created by hjzhang on 2016/4/20.
 */
public class ProductBean {
    private int Team_Id;//商品编号
    private int Team_Type_Id;//一级分类编号
    private int Team_SecType_Id;//二级分类编号
    private String Team_Title;//标题
    private String Team_Icon;//图片
    private float Team_Price;//原价
    private float Team_NowPrice;//现价
    private int Team_SaleCount;// 已售数量
    private int Team_Normal_Id;// 默认规格编号


    public int getTeam_Id() {
        return Team_Id;
    }

    public void setTeam_Id(int team_Id) {
        Team_Id = team_Id;
    }

    public int getTeam_Type_Id() {
        return Team_Type_Id;
    }

    public void setTeam_Type_Id(int team_Type_Id) {
        Team_Type_Id = team_Type_Id;
    }

    public int getTeam_SecType_Id() {
        return Team_SecType_Id;
    }

    public void setTeam_SecType_Id(int team_SecType_Id) {
        Team_SecType_Id = team_SecType_Id;
    }

    public String getTeam_Title() {
        return Team_Title;
    }

    public void setTeam_Title(String team_Title) {
        Team_Title = team_Title;
    }

    public String getTeam_Icon() {
        return Team_Icon;
    }

    public void setTeam_Icon(String team_Icon) {
        Team_Icon = team_Icon;
    }

    public float getTeam_Price() {
        return Team_Price;
    }

    public void setTeam_Price(float team_Price) {
        Team_Price = team_Price;
    }

    public float getTeam_NowPrice() {
        return Team_NowPrice;
    }

    public void setTeam_NowPrice(float team_NowPrice) {
        Team_NowPrice = team_NowPrice;
    }

    public int getTeam_SaleCount() {
        return Team_SaleCount;
    }

    public void setTeam_SaleCount(int team_SaleCount) {
        Team_SaleCount = team_SaleCount;
    }

    public int getTeam_Normal_Id() {
        return Team_Normal_Id;
    }

    public void setTeam_Normal_Id(int team_Normal_Id) {
        Team_Normal_Id = team_Normal_Id;
    }
}
