package edu.fema.jmessenger.jMessenger.entidades;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensagem {

	private String nome;
	private String texto;
	private String dataHora;

	public Mensagem(String nome, String texto) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		this.nome = nome;
		this.texto = texto;
		this.dataHora = formato.format(new Date());
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getDataHora() {
		return dataHora;
	}

	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

	@Override
	public String toString() {
		return this.nome + "xXx" + this.texto + "xXx" + this.dataHora; 
	}
	
	

}
