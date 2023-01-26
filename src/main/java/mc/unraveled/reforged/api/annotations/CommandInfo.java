package mc.unraveled.reforged.api.annotations;

public @interface CommandInfo {
    String name();

    String permission() default "traverse.op";

    String usage() default "/<command>";

    String description() default "No description provided.";

    String[] aliases() default {};
}
