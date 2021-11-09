package com.pineapplesupermarket.tiendaapi.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.pineapplesupermarket.tiendaapi.models.ProductCategory;
import com.pineapplesupermarket.tiendaapi.services.IProductoCategoriaService;
import com.pineapplesupermarket.tiendaapi.services.IUserService;

@RestController
@RequestMapping("/api/v1/categoria")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ProductoCategoriaController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductoCategoriaController.class);

	@Autowired
	private IProductoCategoriaService categoriaService;
	
	@Autowired
	private IUserService userService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ProductCategory>> listAllProduct(Principal principal){
		String username = userService.getPrincipalUsername(principal);
		logger.info("Req:[List categories] by " + username);
		
		List<ProductCategory> listCategorias = this.categoriaService.listAll();

		logger.info(HttpStatus.OK.toString());
		return new ResponseEntity<>(listCategorias, HttpStatus.OK);
	}
}
