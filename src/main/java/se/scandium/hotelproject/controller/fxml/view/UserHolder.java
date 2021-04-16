package se.scandium.hotelproject.controller.fxml.view;

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

  @Override
  public String toString() {
    return "UserHolder{" +
            "userView=" + userView +
            '}';
  }
}