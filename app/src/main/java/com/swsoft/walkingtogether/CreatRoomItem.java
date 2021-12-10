package com.swsoft.walkingtogether;

public class CreatRoomItem {

    public String roomTitle;
    public String roomHour;
    public String roomMinute;
    public String roomLatitude;
    public String roomLongitude;
    public String roomchattitle;
    public int roomuser;

    public CreatRoomItem() {}

    public CreatRoomItem(String roomTitle, String roomHour, String roomMinute, String roomLatitude, String roomLongitude, String roomchattitle, int roomuser) {
        this.roomTitle = roomTitle;
        this.roomHour = roomHour;
        this.roomMinute = roomMinute;
        this.roomLatitude = roomLatitude;
        this.roomLongitude = roomLongitude;
        this.roomchattitle = roomchattitle;
        this.roomuser = roomuser;
    }
}//CreateRoomItem
