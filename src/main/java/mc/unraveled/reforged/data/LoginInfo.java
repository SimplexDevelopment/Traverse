package mc.unraveled.reforged.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;

/**
 * Login Message Information Bean
 */
@Data
@NoArgsConstructor
public class LoginInfo {
    private Component loginMessage;
}
