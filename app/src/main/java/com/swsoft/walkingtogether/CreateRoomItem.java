package com.swsoft.walkingtogether;

public class CreateRoomItem {

    public String roomTitle;
    public String roomHour;
    public String roomMinute;
    public String roomLatitude;
    public String roomLongitude;
    public String roomchattitle;

    public CreateRoomItem () {}

    public CreateRoomItem(String roomTitle, String roomHour, String roomMinute, String roomLatitude, String roomLongitude, String roomchattitle) {
        this.roomTitle = roomTitle;
        this.roomHour = roomHour;
        this.roomMinute = roomMinute;
        this.roomLatitude = roomLatitude;
        this.roomLongitude = roomLongitude;
        this.roomchattitle = roomchattitle;
    }
}//CreateRoomItem
