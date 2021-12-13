package me.dannly.selecaomaisprati.presentation.util;

public enum Action {

    CREATE("Criar"), LIST("Listar"), UPDATE("Atualizar"), DELETE("Deletar"), TERMINATE("Encerrar");

    private final String actionName;

    Action(String actionName) {
        this.actionName = actionName;
    }

    public static Action findByActionName(String actionName) {
        for (Action value : values())
            if (value.actionName.equalsIgnoreCase(actionName))
                return value;
        return null;
    }

    public String getActionName() {
        return actionName;
    }
}
