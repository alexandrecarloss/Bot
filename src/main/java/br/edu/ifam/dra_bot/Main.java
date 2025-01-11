package br.edu.ifam.dra_bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {
    public static void main(String[] args) {

        try{
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new DraBot());
            System.out.println("Bot online!");
        }catch(TelegramApiException e){
            e.printStackTrace();
            System.out.println("Erro ao criar o bot");
        }
    }
}