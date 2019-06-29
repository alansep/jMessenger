/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fema.jmessenger.jMessenger.entidades;

import edu.fema.jmessenger.jMessenger.visual.ChatActive;

/**
 *
 * @author alansep
 */
public class ObjetoChatActive extends Thread {

    private String ip;
    private String nome;

    public ObjetoChatActive(String ip, String nome) {
        this.ip = ip;
        this.nome = nome;
    }

    public String getTitulo() {
        return ip;
    }

    public void setTitulo(String titulo) {
        this.ip = titulo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void run() {
        ChatActive chatActive = new ChatActive(ip, nome);
        chatActive.setTitle("Conversa");
        chatActive.setVisible(true);
    }
}
