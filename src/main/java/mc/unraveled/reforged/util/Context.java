package mc.unraveled.reforged.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Context<S> {
    private S context;
}
