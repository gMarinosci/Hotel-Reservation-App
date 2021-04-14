package se.scandium.hotelproject.controller.fxml.view;

import se.scandium.hotelproject.entity.User;

public final class UserHolder {
  
  private UserView userView;
  private final static UserHolder INSTANCE = new UserHolder();
  
  private UserHolder() {}
  
  public static UserHolder getInstance() {
    return INSTANCE;
  }

  public UserView getUserView() {
    return userView;
  }

  public void setUserView(UserView userView) {
    this.userView = userView;
  }
}