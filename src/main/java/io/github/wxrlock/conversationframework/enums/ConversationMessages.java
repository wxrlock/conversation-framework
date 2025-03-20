package io.github.wxrlock.conversationframework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConversationMessages {

    CONVERSATION_ENDED("§cConversa encerrada."),
    CONVERSATION_CANCELLED("§cVocê cancelou a conversa."),
    ALREADY_IN_CONVERSATION("§cVocê já está em uma conversação!"),
    NOT_IN_CONVERSATION("§cVocê não está em uma conversação!"),
    INVALID_OPTION("§cEssa opção não é válida para a pergunta atual!"),
    TIMEOUT("§cTempo esgotado! Conversa cancelada."),
    NEGATIVE_NOT_ALLOWED("§cO número não pode ser negativo!"),
    BELOW_MINIMUM("§cO número deve ser maior ou igual a %s!"),
    ABOVE_MAXIMUM("§cO número deve ser menor ou igual a %s!"),
    INVALID_NUMBER("§cPor favor, digite um %s válido!"),
    CANCELLATION_TIP("§8Para cancelar, digite '§ccancelar§8' no chat.");

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }

}