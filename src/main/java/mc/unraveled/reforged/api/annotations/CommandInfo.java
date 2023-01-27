package mc.unraveled.reforged.api.annotations;

public @interface CommandInfo {
    String name();

    String usage() default "/<command>";

    String description() default "No description provided.";

    String[] aliases() default {};
}
