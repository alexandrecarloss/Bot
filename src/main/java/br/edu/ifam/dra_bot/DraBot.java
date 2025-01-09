package br.edu.ifam.dra_bot;

// Importações necessárias para o funcionamento do bot
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe principal do bot que estende TelegramLongPollingBot
 * e implementa as funcionalidades básicas de um bot Telegram.
 */
public class DraBot extends TelegramLongPollingBot {

    /**
     * Retorna o nome do bot definido em DadosBot.
     * @return Nome do bot.
     */
    @Override
    public String getBotUsername(){
        return DadosBot.BOT_USER_NAME;
    }

    /**
     * Retorna o token do bot definido em DadosBot.
     * @return Token do bot.
     */
    @Override
    public String getBotToken(){
        return DadosBot.BOT_TOKEN;
    }

    /**
     * Método auxiliar para obter a data atual no formato "dd/MM/yyyy".
     * @return String contendo a data atual.
     */
    private String getData(){
        // Define o formato da data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Retorna a data formatada
        return "A data atual é: " + sdf.format(new Date());
    }

    /**
     * Método responsável por gerar a resposta para o usuário com base no comando recebido.
     * @param update Objeto Update recebido do Telegram.
     * @return Objeto SendMessage com a resposta.
     */
    public SendMessage responder(Update update){
        // Obtém o texto da mensagem e converte para minúsculas
        String textoMensagem = update.getMessage().getText().toLowerCase();
        // Obtém o ID do chat
        String chatId = update.getMessage().getChatId().toString();

        // Variável para armazenar a resposta
        String resposta = "";

        // Verifica o comando recebido e define a resposta
        if(textoMensagem.startsWith("/data")){
            // Retorna a data atual
            resposta = getData();
        } else if(textoMensagem.startsWith("/help")){
            // Retorna os comandos disponíveis
            resposta = "Utilize um dos comandos:\ndata";
        } else if(textoMensagem.startsWith("/start")){
            // Mensagem de boas-vindas
            resposta = "Olá! Sou o Bot DRA 1.\n" +
                    "Sou um exemplo de aplicação de Bot no Telegram com Java.\n" +
                    "No momento só tenho uma função: te dizer a data atual.\n" +
                    "Para isso, digite /data.";
        } else {
            // Mensagem padrão para comandos desconhecidos
            resposta = "Não entendi!\nDigite /help para ver os comandos disponíveis.";
        }

        // Retorna o objeto SendMessage configurado
        return SendMessage.builder().text(resposta).chatId(chatId).build();
    }

    /**
     * Método que é chamado automaticamente quando o bot recebe uma mensagem.
     * @param update Objeto Update contendo os dados da mensagem recebida.
     */
    public void onUpdateReceived(Update update){
        // Verifica se a mensagem contém texto
        if(update.hasMessage() && update.getMessage().hasText()){
            // Gera a resposta com base no comando
            SendMessage mensagem = responder(update);

            try {
                // Envia a mensagem de resposta
                execute(mensagem);
            } catch(TelegramApiException e){
                // Trata possíveis exceções durante o envio da mensagem
                e.printStackTrace();
            }
        }
    }
}
