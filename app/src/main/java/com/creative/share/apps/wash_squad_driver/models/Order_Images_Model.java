package com.creative.share.apps.wash_squad_driver.models;

import java.io.Serializable;
import java.util.List;

public class Order_Images_Model  implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
        {
            private int id;
                private String order_id;
                private String image;
            private String type;
            private String status;

            public int getId() {
                return id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public String getImage() {
                return image;
            }

            public String getType() {
                return type;
            }

            public String getStatus() {
                return status;
            }
        }
}
