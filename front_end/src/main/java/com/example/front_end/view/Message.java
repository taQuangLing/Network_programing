package com.example.front_end.view;

import com.example.front_end.appUtils.AppUtils;
import javafx.scene.control.Alert;

public class Message {
    private AppUtils.MessageCode code;
    private String content;
    private int state;

    public Message(AppUtils.MessageCode code, String content) {
        this.code = code;
        this.content = content;
    }
    public Message(AppUtils.MessageCode code) {
        this.code = code;
    }
    public void alert(){
        Alert alert;
        switch (code){
            case SUCCESS:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Infomation");
                break;
            case WARNING:
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                break;
            case ERROR:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Hệ thống đang bảo trì");
                alert.show();
                return;
            default:
                alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Thông báo");

        }
        alert.setHeaderText("Thông báo");
        alert.setContentText(content);
        alert.show();
    }
}
