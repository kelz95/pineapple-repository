package com.pineapplesupermarket.tiendaapi.controllers;

import java.security.Principal;
import java.util.Date;

//import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pineapplesupermarket.tiendaapi.dto.FilterProductoDTO;
import com.pineapplesupermarket.tiendaapi.dto.ResponseDTO;
import com.pineapplesupermarket.tiendaapi.enums.ResponseCodeEnum;
import com.pineapplesupermarket.tiendaapi.exception.DuplicateEntryException;
import com.pineapplesupermarket.tiendaapi.exception.EntityNotFoundException;
import com.pineapplesupermarket.tiendaapi.exception.FailUploadedException;
import com.pineapplesupermarket.tiendaapi.models.Product;
import com.pineapplesupermarket.tiendaapi.services.IProductoService;
import com.pineapplesupermarket.tiendaapi.services.IUserService;
import com.pineapplesupermarket.tiendaapi.util.ExportarInventario;
import com.pineapplesupermarket.tiendaapi.util.JsonUtils;
import com.pineapplesupermarket.tiendaapi.util.LoggerUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *Controlador del producto
 *@author Raquel de la Rosa 
 *@version 1.0
 */

@RestController
@RequestMapping("/api/v1/products")
@Api(value = "Product Controller")
public class ProductoController {

	private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ExportarInventario exportarInventario;
	
	/**
	 * End point que obtiene un producto
	 * @param id
	 * @param principal
	 * @return ResponseEntity<ResponseDTO>
	 * @exception EntityNotFoundException, Exception
	 */
	@GetMapping("/{id}")
	@ApiOperation(response = Product.class, value = "Find a product by id")
	public ResponseEntity<?> getProduct(@PathVariable(value="id") long id, Principal principal){
		String username = userService.getPrincipalUsername(principal);
		LoggerUtils.logRequest(logger, "Consult product", username);
		try {
			Product producto = productoService.findOne(id);
			LoggerUtils.logResponse(logger, HttpStatus.OK.toString());
			
		    return new ResponseEntity<>(producto, HttpStatus.OK);
		}catch(EntityNotFoundException e) {
			LoggerUtils.logException(logger, HttpStatus.NOT_FOUND.toString(), e.getMessage());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_ENCONTRADO.getCodigo(), 
		        		ResponseCodeEnum.NO_ENCONTRADO.getMensaje()), HttpStatus.NOT_FOUND);
			
		} catch(Exception e) {
			LoggerUtils.logException(logger, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
	        return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
	        		ResponseCodeEnum.NO_PROCESADO.getMensaje()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**End point que obtiene una lista de productos
	 * @param page
	 * @param size
	 * @param name
	 * @param categoria
	 * @param fechaCreacion
	 * @param principal
	 * @return Page<Product> listAllProduct
	 * @exception DuplicateEntryException, EntityNotFoundException, JsonProcessingException,Exception
	 */
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "List of products")
	public Page<Product> listAllProduct(@RequestParam(defaultValue="0") int page,
				@RequestParam(defaultValue = "10") int size,
				@RequestParam(required = false) String name, 
				@RequestParam(required = false) String categoria,
				@RequestParam(required = false) 
				@DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaCreacion, Principal principal){
		String username = userService.getPrincipalUsername(principal);
		LoggerUtils.logRequest(logger, "Search product", username);

		FilterProductoDTO filters = new FilterProductoDTO();
		filters.setName(name);
		filters.setCategoria(categoria);
		filters.setFechaCreacion(fechaCreacion);
		filters.setPage(page);
		filters.setSize(size);

		Page<Product> productos = productoService.getProductos(filters);
		LoggerUtils.logResponse(logger, HttpStatus.OK.toString());
		return productos;
	}
	

	/**End point que crea un producto
	 * @param producto
	 * @param picture
	 * @param principal
	 * @return ResponseEntity<?> 
	 * @exception DuplicateEntryException, EntityNotFoundException, JsonProcessingException, Exception
	 */
	@PostMapping(value="",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(response = Product.class, value = "Create a product")
	public ResponseEntity<?> create(@Valid @RequestPart String producto,
			@RequestPart(value="picture", required = false) MultipartFile picture, Principal principal) { 
		String username = userService.getPrincipalUsername(principal);
		LoggerUtils.logRequest(logger, "Create product", username);
		
		try {
			Product productObject = JsonUtils.convertFromJsonToObject(producto, Product.class);
			try {
				if(productObject.getCode().isBlank() ||
					productObject.getName().isBlank() || 
					productObject.getProductCategory().getCode().isBlank() ||
					productObject.getQuantity() == null ||
					productObject.getUnitPrice() == null) { 
					LoggerUtils.logResponse(logger, HttpStatus.BAD_REQUEST.toString(), "Parametros vacios");
					return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
				        		"Parametros vacios"), HttpStatus.BAD_REQUEST);
				}
			}catch(NullPointerException e) {
				LoggerUtils.logResponse(logger, HttpStatus.BAD_REQUEST.toString(), "Parametros vacios");
				return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
			        		"Parametros vacios"), HttpStatus.BAD_REQUEST);
			}
			
	        Product productCreated = productoService.create(productObject, picture);
			LoggerUtils.logResponse(logger, HttpStatus.CREATED.toString(), "Product id: " + productCreated.getIdProduct());
	        return new ResponseEntity<>(productCreated, HttpStatus.CREATED);
	        
		} catch(DuplicateEntryException e) {
			LoggerUtils.logException(logger, HttpStatus.BAD_REQUEST.toString(), e.getMessage());
			 return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.DUPLICADO.getCodigo(), 
		        		ResponseCodeEnum.DUPLICADO.getMensaje()), HttpStatus.BAD_REQUEST);
		} catch(EntityNotFoundException e) {
			 LoggerUtils.logException(logger, HttpStatus.NOT_FOUND.toString(), e.getMessage());
			 return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_ENCONTRADO.getCodigo(), 
		        		ResponseCodeEnum.NO_ENCONTRADO.getMensaje()), HttpStatus.NOT_FOUND);
		} catch(JsonProcessingException e) {
			LoggerUtils.logException(logger, HttpStatus.BAD_REQUEST.toString(), e.getMessage());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
		        		ResponseCodeEnum.NO_PROCESADO.getMensaje()), HttpStatus.BAD_REQUEST);	
		} catch(Exception e) {
			LoggerUtils.logException(logger, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
	        		ResponseCodeEnum.NO_PROCESADO.getMensaje()), HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	}
	/**End point que sube una imagen
	 * @param producto
	 * @param picture
	 * @param principal
	 * @return ResponseEntity<?>
	 * @exception FailUploadedException, EntityNotFoundException, Exception
	 */
	@PutMapping("/{id}/upload")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "Upload a product image")
	public ResponseEntity<ResponseDTO> upload(@PathVariable(value="id") long id,
			@RequestParam("picture") MultipartFile picture,
			Principal principal) { 
		String username = userService.getPrincipalUsername(principal);
		LoggerUtils.logRequest(logger, "Upload product picture", username);
		
		try {
			this.productoService.upload(id,picture);
			
			LoggerUtils.logResponse(logger, HttpStatus.CREATED.toString());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.PROCESADO.getCodigo(), 
	        		ResponseCodeEnum.PROCESADO.getMensaje()),HttpStatus.CREATED);
		} catch(FailUploadedException e) {
			LoggerUtils.logException(logger, HttpStatus.NOT_MODIFIED.toString(), e.getMessage());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
		        		ResponseCodeEnum.NO_PROCESADO.getMensaje()), HttpStatus.NOT_MODIFIED);
		}catch(EntityNotFoundException e) {
			LoggerUtils.logException(logger, HttpStatus.NOT_FOUND.toString(), e.getMessage());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_ENCONTRADO.getCodigo(), 
		        		ResponseCodeEnum.NO_ENCONTRADO.getMensaje()), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			LoggerUtils.logException(logger, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
	        return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
	        		ResponseCodeEnum.NO_PROCESADO.getMensaje()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**End point que actualiza un producto
	 * @param productoUpdate
	 * @param id
	 * @param principal
	 * @return ResponseEntity<?> 
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(response = Product.class, value = "Update a product")
	public ResponseEntity<?> update(@Valid @RequestBody Product productoUpdate,
			@PathVariable Long id, Principal principal){
		String username = userService.getPrincipalUsername(principal);
		LoggerUtils.logRequest(logger, "Update product", username);

		try {
			Product productoSaved = this.productoService.update(id, productoUpdate);
			LoggerUtils.logResponse(logger, HttpStatus.CREATED.toString(), "Product id: " + productoSaved.getIdProduct());
		       return new ResponseEntity<>(productoSaved, HttpStatus.CREATED);
		}catch(EntityNotFoundException e) {
			LoggerUtils.logException(logger, HttpStatus.NOT_FOUND.toString(), e.getMessage());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_ENCONTRADO.getCodigo(), 
		        		ResponseCodeEnum.NO_ENCONTRADO.getMensaje()), HttpStatus.NOT_FOUND);
			
		} catch(DuplicateEntryException e) {
			LoggerUtils.logException(logger, HttpStatus.BAD_REQUEST.toString(), e.getMessage());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.DUPLICADO.getCodigo(), 
		        		ResponseCodeEnum.DUPLICADO.getMensaje()), HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			LoggerUtils.logException(logger, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
	        return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
	        		ResponseCodeEnum.NO_PROCESADO.getMensaje()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**End point que elimina un producto
	 * @param id
	 * @param principal
	 * @return ResponseEntity<ResponseDTO>
	 * @exception EntityNotFoundException, Exception
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "Delete a product")
	public ResponseEntity<ResponseDTO> delete(@PathVariable(value="id") long id, Principal principal){
		String username = userService.getPrincipalUsername(principal);
		LoggerUtils.logRequest(logger, "Delete product", username);

		try {
			this.productoService.delete(id);
			LoggerUtils.logResponse(logger, HttpStatus.NO_CONTENT.toString(), "Product id: " + id);
		        return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.PROCESADO.getCodigo(), 
		        		ResponseCodeEnum.PROCESADO.getMensaje()), HttpStatus.NO_CONTENT);
		}catch(EntityNotFoundException e) {
			LoggerUtils.logException(logger, HttpStatus.NOT_FOUND.toString(), e.getMessage());
			return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_ENCONTRADO.getCodigo(), 
		        		ResponseCodeEnum.NO_ENCONTRADO.getMensaje()), HttpStatus.NOT_FOUND);
			
		} catch(Exception e) {
			LoggerUtils.logException(logger, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
	        return new ResponseEntity<>(new ResponseDTO(ResponseCodeEnum.NO_PROCESADO.getCodigo(), 
	        		ResponseCodeEnum.NO_PROCESADO.getMensaje()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	/**End point que exporta el inventario
	 * @param principal
	 * @return ExportarInventario
	 */
	@GetMapping("/exportar")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(response = ExportarInventario.class, value = "Export products to Excel")
	public ExportarInventario exportar(Principal principal){
		String username = userService.getPrincipalUsername(principal);
		LoggerUtils.logRequest(logger, "Export products to Excel", username);
		return exportarInventario;
	}
	
}
