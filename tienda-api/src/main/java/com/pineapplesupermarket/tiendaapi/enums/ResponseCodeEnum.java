package com.pineapplesupermarket.tiendaapi.enums;
/**
 *Mensajes de respuesta
 *@author Raquel de la Rosa 
 *@version 1.0
 */
public enum ResponseCodeEnum {

	PROCESADO("00", "Procesado correctamente"),
	NO_ENCONTRADO("01", "No registrado en el catalogo"),
	DUPLICADO("02", "Ya registrado previamente"),
	NO_PROCESADO("03", "Error al procesar"),
	NO_AUTORIZADO("04", "No autorizado para el recurso solicitado");
	
	private String codigo;
	private String mensaje;
	

	private ResponseCodeEnum(String codigo, String mensaje) {
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	
}
