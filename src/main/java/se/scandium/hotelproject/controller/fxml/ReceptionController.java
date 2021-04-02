package se.scandium.hotelproject.controller.fxml;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import se.scandium.hotelproject.entity.User;


@Controller
public class ReceptionController {

    private User viewObject;

    public void setViewObject(User viewObject) {
        if(viewObject != null){
        }
        this.viewObject = viewObject;
    }


}
