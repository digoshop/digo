package com.bjut.servicedog.servicedog.model;

import java.util.List;

public class AddressDtailsEntity {
    public int SellerId;
    public int OrderId;
    public String RecipientName;
    public String Mobile;
    public String ZipCode;
    public String Province;
    public String City;
    public String Area;
    public String Address;
    public Data ProvinceItems;

    public class Data {
        public List<ProvinceEntity> Province;
    }

    public class ProvinceEntity {
        public String Name;
        public List<CityEntity> City;

        public class CityEntity {
            public String Name;
            public String nid;
            public List<AreaEntity> Area;
        }
        public class AreaEntity {
            public String name;
            public String nid;
            public String tag;
        }
    }
}
