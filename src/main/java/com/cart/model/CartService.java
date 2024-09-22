package com.cart.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;
@Component
public class CartService {

    public String addToCart(Cart cartItem, Integer memberId) {

        // 拿到前端的購物項 先轉成JSON物件
        Gson gson = new Gson();
        String dtoString = gson.toJson(cartItem);
//        System.out.println("dtoString = " + dtoString);

        JsonObject newItem = gson.fromJson(dtoString, JsonObject.class);



        Jedis jedis = null;
        try {
            jedis = new Jedis("localhost", 6379);


            // 確認是否已有購物車存在(以會員ID判斷)
            String cartStr = jedis.get("memberId:" + memberId);


            JsonArray cart = null;
            if (cartStr == null) {
                // 如果購物車不存在
                cart = new JsonArray();

                // 把購物項加入
                cart.add(newItem);

                // 購物車包成一個字串
                cartStr = cart.toString();

                // 放入redis資料庫
                jedis.set("memberId:" + memberId, cartStr);
            } else {
                // 如果購物車存在

                //  String cartStr變成JSONArray
                cart = gson.fromJson(cartStr, JsonArray.class);

                // 檢查商品是不是已經在購物車
                boolean itemExists = false;
                for (JsonElement element : cart) {
                    // 取出購物車原有的購物項
                    JsonObject oldItem = element.getAsJsonObject();

                    // 如果productId一樣，增加商品數量
                    if (oldItem.get("productId").getAsInt() == newItem.get("productId").getAsInt()) {
                        Integer oldQuantity = oldItem.get("quantity").getAsInt();
                        Integer newQuantity = newItem.get("quantity").getAsInt();
                        Integer totalQuantity = oldQuantity + newQuantity;
                        oldItem.addProperty("quantity", totalQuantity);
                        itemExists = true; // 標記商品已存在購物車中
                        break;
                    }

                }

                // 如果商品不在，加進購物車
                if (!itemExists) {
                    cart.add(newItem);
                }

                // 更新購物車字串
                cartStr = cart.toString();

                // 存入Redis
                jedis.set("memberId:" + memberId, cartStr);

            }

//            System.out.println(cartStr);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return "成功加入";

    }



    public List<Cart> findAllItem(Integer memberId){
        List<Cart> responseList = new ArrayList<>();
        Jedis jedis = null;
        Gson gson = new Gson();

        try {
            jedis = new Jedis("localhost", 6379);

            // 確認是否已有購物車存在(以會員ID判斷)
            String cartStr = jedis.get("memberId:" + memberId);

            //如果購物車存在
            if(cartStr != null) {
                JsonArray cart = gson.fromJson(cartStr, JsonArray.class);

                // 取出裡面每個JsonObject
                for (JsonElement element : cart) {
                    JsonObject oldItem = element.getAsJsonObject();

                    // 迴圈每次都建立一個新物件
                    Cart cartObject = new Cart();
                    cartObject.setProductId(oldItem.get("productId").getAsInt());
                    cartObject.setProductName(oldItem.get("productName").getAsString());
                    cartObject.setQuantity(oldItem.get("quantity").getAsInt());
                    cartObject.setPrice(oldItem.get("price").getAsInt());

                    // 加進要回應的List裡
                    responseList.add(cartObject);
                }
            }

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return responseList;
    }





    public void updateOneItem(Integer memberId, Cart cartItem) {

        // 拿到前端的購物項 先轉成JSON物件
        Gson gson = new Gson();
        String dtoString = gson.toJson(cartItem);
//        System.out.println(dtoString);

        JsonObject newItem = gson.fromJson(dtoString, JsonObject.class);

        Jedis jedis = null;
        try {
            jedis = new Jedis("localhost", 6379);

            // 確認是否已有購物車存在(以會員ID判斷)
            String cartStr = jedis.get("memberId:" + memberId);

            JsonArray cart = null;

            if (cartStr == null) {
                // 如果購物車不存在
                cart = new JsonArray();

                // 把購物項加入
                cart.add(newItem);

                // 購物車包成一個字串
                cartStr = cart.toString();

                // 放入redis資料庫
                jedis.set("memberId:" + memberId, cartStr);
            } else {
                // 如果購物車存在

                //  String cartStr變成JSONArray
                cart = gson.fromJson(cartStr, JsonArray.class);

                // 檢查商品是不是已經在購物車
                boolean itemExists = false;
                for (JsonElement element : cart) {
                    // 取出購物車內部物件
                    JsonObject oldItem = element.getAsJsonObject();

                    // 如果productId一樣，增加數量
                    if (oldItem.get("productId").getAsInt() == newItem.get("productId").getAsInt()) {
                        int newQuantity = newItem.get("quantity").getAsInt();
                        //修改數量為新的數量
                        oldItem.addProperty("quantity", newQuantity);
                        itemExists = true; // 標記商品已存在購物車中
                        break;
                    }
                }

                // 如果商品不在，加進購物車
                if (!itemExists) {
                    cart.add(newItem);
                }

                // 更新購物車字串
                cartStr = cart.toString();

                // 存入Redis
                jedis.set("memberId:" + memberId, cartStr);

            }
//            System.out.println(cartStr);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }





    public void removeOneItem(Integer productId, Integer memberId) {

        Gson gson = new Gson();

        Jedis jedis = null;
        try {
            jedis = new Jedis("localhost", 6379);

            // 確認是否已有購物車存在(以會員ID判斷)
            String cartStr = jedis.get("memberId:" + memberId);


            if (cartStr != null) {
                // 如果購物車存在

                //  String cartStr變成JSONArray
                JsonArray cart = gson.fromJson(cartStr, JsonArray.class);

                // 檢查商品是不是已經在購物車
                for (JsonElement element : cart) {

                    // 取出購物車原有的購物項
                    JsonObject oldItem = element.getAsJsonObject();

                    // 如果productId一樣，移除該商品
                    if (oldItem.get("productId").getAsInt() == productId) {
                        cart.remove(oldItem);
                        break;
                    }
                }
                // 更新購物車字串
                cartStr = cart.toString();

                // 存入Redis
                jedis.set("memberId:" + memberId, cartStr);

            }
//            System.out.println(cartStr);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }





    public void cleanAllCart(Integer memberId) {

        // 找出會員的購物車，把JsonArray裡的物件清空
        Jedis jedis = null;
        try {
            jedis = new Jedis("localhost", 6379);

            // 確認是否已有購物車存在(以會員ID判斷)
            jedis.del("memberId:" + memberId);

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    //拿取總數
    public Integer getCartNumber(Integer memberId){
        Jedis jedis = null;
        Gson gson = new Gson();

        Integer count = 0;

        try {
            jedis = new Jedis("localhost", 6379);

            // 確認是否已有購物車存在(以會員ID判斷)
            String cartStr = jedis.get("memberId:" + memberId);

            //如果購物車存在
            if(cartStr != null) {
                JsonArray cart = gson.fromJson(cartStr, JsonArray.class);

                // 取出裡面每個JsonObject
                for (JsonElement element : cart) {
                    //每取出一個便+1
                    count++;
                }
            }

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return count;
    }

}