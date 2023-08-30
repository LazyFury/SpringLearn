package io.lazyfury.mall.code.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleJsonConfig implements Serializable {
    ArrayList<String> keywords;
    String name;

    public String toJson(){
       try{
           return JSON.toJSONString(this);
       }catch (JSONException e){
           System.err.println(e);
           return "{}";
       }
    }
}


