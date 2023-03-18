package mc.unraveled.reforged.data;

import net.kyori.adventure.text.Component;

/**
 * Login Message Information Bean
 */
public class LoginInfo {
    private Component loginMessage;

    public LoginInfo() {
        this.loginMessage = Component.empty();
    }

    public Component getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(Component loginMessage) {
        this.loginMessage = loginMessage;
    }
}
